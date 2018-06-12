package com.dili.uap.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.SystemConfigMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.System;
import com.dili.uap.domain.SystemConfig;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.glossary.UserState;
import com.dili.uap.glossary.Yn;
import com.dili.uap.manager.*;
import com.dili.uap.sdk.manager.SessionRedisManager;
import com.dili.uap.sdk.session.ManageConfig;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginLogService;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.SystemService;
import com.dili.uap.utils.MD5Util;
import com.dili.uap.utils.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 登录服务
 * Created by asiam on 2018/5/18 0018.
 */
@Component
public class LoginServiceImpl implements LoginService {

    private final static Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MD5Util md5Util;

    @Value("${pwd.error.check:false}")
    private Boolean pwdErrorCheck;

    //密码错误范围，默认十分钟
    @Value("${pwd.error.range:600000}")
    private Long pwdErrorRange;

    @Value("${pwd.error.count:3}")
    private Integer pwdErrorCount;

    @Autowired
    private ManageRedisUtil redisUtil;

    @Autowired
    private MenuManager menuManager;

    @Autowired
    private ResourceManager resourceManager;

    @Autowired
    private SystemManager systemManager;

    @Autowired
    private ManageConfig manageConfig;

    @Autowired
    private SessionRedisManager sessionRedisManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private DataAuthManager dataAuthManager;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private SystemService systemService;

    @Override
    public BaseOutput<LoginResult> login(LoginDto loginDto) {
        try {
            User record = DTOUtils.newDTO(User.class);
            record.setUserName(loginDto.getUserName());
            User user = this.userMapper.selectOne(record);
            //设置默认登录系统为UAP
            if(StringUtils.isBlank(loginDto.getSystemCode())){
                loginDto.setSystemCode(UapConstants.UAP_SYSTEM_CODE);
            }
            //判断密码不正确，三次后锁定用户、锁定后的用户12小时后自动解锁
            if (user == null || !StringUtils.equalsIgnoreCase(user.getPassword(), this.encryptPwd(loginDto.getPassword()))) {
                lockUser(user);
                logLogin(loginDto, false, "用户名或密码错误");
                return BaseOutput.failure("用户名或密码错误").setCode(ResultCode.NOT_AUTH_ERROR);
            }
            //记录用户id和市场编码，用于记录登录日志
            loginDto.setUserId(user.getId());
            loginDto.setFirmCode(user.getFirmCode());
            //登录成功后清除锁定计时
            clearUserLock(user.getId());
            if(user.getState().equals(UserState.LOCKED.getCode())){
                logLogin(loginDto, false, "用户已被锁定，请联系管理员");
                return BaseOutput.failure("用户已被锁定，请联系管理员").setCode(ResultCode.NOT_AUTH_ERROR);
            }
            //用户状态为锁定和禁用不允许登录
            if (user.getState().equals(UserState.DISABLED.getCode()) || user.getState().equals(UserState.LOCKED.getCode())) {
                return BaseOutput.failure("用户状态"+UserState.getUserState(user.getState()).getName()+", 不能进行登录!");
            }

            //加载用户系统
            this.systemManager.initUserSystemInRedis(user.getId());
            // 加载用户url
            this.menuManager.initUserMenuUrlsInRedis(user.getId());
            // 加载用户resource
            this.resourceManager.initUserResourceCodeInRedis(user.getId());
            // 加载用户数据权限
            this.dataAuthManager.initUserDataAuthesInRedis(user.getId());

            //原来的代码是更新用户的最后登录IP和最后登录时间，现在暂时不需要了
//        user.setLastLoginTime(new Date());
//        user.setLastLoginIp(dto.getIp());
//        if (this.userMapper.updateByPrimaryKey(user) <= 0) {
//            LOG.error("登录过程更新用户信息失败");
//            return BaseOutput.failure("用户已被禁用, 不能进行登录!").setCode(ResultCode.NOT_AUTH_ERROR);
//        }
            LOG.info(String.format("用户登录成功，用户名[%s] | 用户IP[%s]", loginDto.getUserName(), loginDto.getIp()));
            // 用户登陆 挤掉 旧登陆用户
            jamUser(user);
            String sessionId = UUID.randomUUID().toString();
            //缓存用户相关信息到Redis
            makeRedisTag(user, sessionId);
            //构建返回的登录信息
            LoginResult loginResult = DTOUtils.newDTO(LoginResult.class);
            //返回用户信息需要屏蔽用户的密码
            user.setPassword(null);
            loginResult.setUser(user);
            loginResult.setSessionId(sessionId);
            loginResult.setLoginPath(loginDto.getLoginPath());
            logLogin(loginDto, true, "登录成功");
            return BaseOutput.success("登录成功").setData(loginResult);
        } catch (Exception e) {
            logLogin(loginDto, false, e.getMessage());
            return BaseOutput.failure("登录失败").setResult(e.getMessage());
        }
    }

    @Override
    public BaseOutput<Boolean> loginAndTag(LoginDto loginDto) {
        BaseOutput<LoginResult> output = this.login(loginDto);
        if(!output.isSuccess()){
            return BaseOutput.failure(output.getResult()).setCode(output.getCode()).setData(false);
        }
        makeCookieTag(output.getData().getUser(), output.getData().getSessionId());
        return BaseOutput.success("登录成功");
    }

    // =================================   私有方法分割线   ====================================

    /**
     * 异步记录登录日志
     */
    private void logLogin(LoginDto loginDto, boolean isSuccess, String msg){
        new Thread(){
            @Override
            public void run() {
                LoginLog loginLog = DTOUtils.as(loginDto, LoginLog.class);
                loginLog.setSuccess(isSuccess ? Yn.YES.getCode() : Yn.NO.getCode());
                loginLog.setReason(msg);
                //设置系统名称
                if(StringUtils.isNotBlank(loginLog.getSystemCode())) {
                    System system = DTOUtils.newDTO(System.class);
                    system.setCode(loginDto.getSystemCode());
                    List<System> systemList = systemService.listByExample(system);
                    loginLog.setSystemName(systemList.isEmpty() ? loginDto.getSystemCode() : systemList.get(0).getName());
                }
                loginLogService.insertSelective(loginLog);
            }
        }.start();
    }

    /**
     * 用户登陆 挤掉 旧登陆用户
     *
     * @param user
     */
    private void jamUser(User user) {
        if (this.manageConfig.getUserLimitOne() && this.sessionRedisManager.existUserIdSessionIdKey(user.getId().toString())) {
            List<String> oldSessionIds = this.userManager.clearUserSession(user.getId());
            if(oldSessionIds == null) {
                return;
            }
            for(String oldSessionId : oldSessionIds) {
                // 为了提示
                this.sessionRedisManager.addKickSessionKey(oldSessionId);
            }
        }
    }

    /**
     * 记录用户信息和登录地址到Cookie
     * @param user
     * @param sessionId
     */
    private void makeCookieTag(User user, String sessionId) {
        String referer = WebUtil.fetchReferer(WebContent.getRequest());
        WebContent.setCookie(SessionConstants.COOKIE_SESSION_ID, sessionId);
        WebContent.setCookie("u", user.getId().toString());
        WebContent.setCookie("n", user.getUserName());
        WebContent.setCookie("loginPath", referer);
    }

    /**
     * 缓存用户相关信息到Redis
     * @param user
     * @param sessionId
     */
    private void makeRedisTag(User user, String sessionId) {
        Map<String, Object> sessionData = new HashMap<>(1);
        sessionData.put(SessionConstants.LOGGED_USER, JSON.toJSONString(user));

        LOG.debug("--- Save Session Data To Redis ---");
        // redis: ressionId - user 用于在SDK中根据sessionId获取用户信息，如果sessionId不存在，过期或者被挤，都会被权限系统拦截
        this.redisUtil.set(SessionConstants.SESSION_KEY_PREFIX + sessionId, JSON.toJSONString(sessionData), SessionConstants.SESSION_TIMEOUT);
        // redis: sessionId - userID
        this.sessionRedisManager.setSessionUserIdKey(sessionId, user.getId().toString());
        // redis: userID - sessionId
        this.sessionRedisManager.setUserIdSessionIdKey(user.getId().toString(), sessionId);
        LOG.debug("UserName: " + user.getUserName() + " | SessionId:" + sessionId + " | SessionData:" + sessionData);
    }

    /**
     * 加密
     * @param passwd
     * @return
     */
    private String encryptPwd(String passwd) {
        return md5Util.getMD5ofStr(passwd).substring(6, 24);
    }

    /**
     * 清空用户登录密码错误锁定次数
     * @param userId
     */
    private void clearUserLock(Long userId){
        String key = SessionConstants.USER_PWD_ERROR_KEY + userId;
        redisUtil.getRedisTemplate().delete(key);
    }
    /**
     * 锁定用户
     * @param user
     */
    private void lockUser(User user) {
        //判断是否要进行密码错误检查，不检查就不需要锁定用户了
        if (!pwdErrorCheck) {
            return;
        }
        if (user == null) {
            return;
        }
        String key = SessionConstants.USER_PWD_ERROR_KEY + user.getId();
        BoundListOperations<Object, Object> ops = redisUtil.getRedisTemplate().boundListOps(key);
        while (true) {
            Object s = ops.index(0);
            if (s == null) {
                break;
            }
            Long t = Long.valueOf(s.toString());
            if (t == 0) {
                break;
            }
            Long nt = java.lang.System.currentTimeMillis() - t;
            if (nt < pwdErrorRange) {
                break;
            }
            ops.rightPop();
        }
        ops.leftPush(String.valueOf(java.lang.System.currentTimeMillis()));
        //查询系统配置的密码错误锁定次数
        SystemConfig systemConfigCondition = DTOUtils.newDTO(SystemConfig.class);
        systemConfigCondition.setCode(UapConstants.LOGIN_FAILED_TIMES);
        systemConfigCondition.setSystemCode(UapConstants.UAP_SYSTEM_CODE);
        SystemConfig systemConfig = systemConfigMapper.selectOne(systemConfigCondition);
        //以系统变量配置为主，没有则使用配置文件中的配置
        if(StringUtils.isNotBlank(systemConfig.getValue())){
            pwdErrorCount = Integer.parseInt(systemConfig.getValue());
        }
        //在锁定次数范围内不锁定
        if (ops.size() < Integer.parseInt(systemConfig.getValue())) {
            return;
        }
        //如果当前用户不是锁定状态，则进行锁定
        if (!user.getState().equals(UserState.LOCKED.getCode())) {
            User updateUser = DTOUtils.newDTO(User.class);
            updateUser.setId(user.getId());
            updateUser.setState(UserState.LOCKED.getCode());
            this.userMapper.updateByPrimaryKeySelective(updateUser);
            //清空计时器
            redisUtil.getRedisTemplate().delete(key);
        }
    }
}