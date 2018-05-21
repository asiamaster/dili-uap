package com.dili.uap.service.impl;

import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.glossary.UserState;
import com.dili.uap.manager.MenuManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginService;
import com.dili.uap.utils.MD5Util;
import com.dili.uap.utils.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Component;


/**
 * 登录服务
 * Created by asiam on 2018/5/18 0018.
 */
@Component
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MD5Util md5Util;

    @Value("${pwd.error.check:false}")
    private Boolean pwdErrorCheck;

    //密码错误范围
    @Value("${pwd.error.range:600000}")
    private Long pwdErrorRange;

    @Value("${pwd.error.count:3}")
    private Integer pwdErrorCount;

    @Autowired
    private ManageRedisUtil redisUtil;

    @Autowired
    private MenuManager menuManager;

    @Override
    public BaseOutput<LoginResult> login(String userName, String password) {
        User record = new User();
        record.setUserName(userName);
        User user = this.userMapper.selectOne(record);
        if (user == null || !StringUtils.equalsIgnoreCase(user.getPassword(), this.encryptPwd(user.getPassword()))) {
            lockUser(user);
            return BaseOutput.failure("用户名或密码错误").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        //用户状态为锁定和禁用不允许登录
        if (user.getState().equals(UserState.DISABLED.getCode()) || user.getState().equals(UserState.LOCKED.getCode())) {
            return BaseOutput.failure("用户状态"+UserState.getUserState(user.getState()).getName()+", 不能进行登录!");
        }

        // 加载用户url
        this.menuManager.initUserMenuUrlsInRedis(user.getId());
//        // 加载用户resource
//        this.resourceManager.initUserResourceCodeInRedis(user.getId());
//        // 加载用户数据权限
//        this.dataAuthManager.initUserDataAuthsInRedis(user.getId());

        return null;
    }

    @Override
    public BaseOutput<Boolean> loginAndTag(String userName, String password) {
        BaseOutput<LoginResult> output = this.login(userName, password);
        if(!output.isSuccess()){
            return BaseOutput.failure(output.getResult()).setCode(output.getCode()).setData(false);
        }
        makeCookieTag(output.getData().getUser(), output.getData().getSessionId());
        return BaseOutput.success("登录成功");
    }

    private void makeCookieTag(User user, String sessionId) {
        String referer = WebUtil.fetchReferer(WebContent.getRequest());
        WebContent.setCookie(SessionConstants.COOKIE_SESSION_ID, sessionId);
        WebContent.setCookie("u", user.getId().toString());
        WebContent.setCookie("n", user.getUserName());
        WebContent.setCookie("loginPath", referer);
    }

    private String encryptPwd(String passwd) {
        return md5Util.getMD5ofStr(passwd).substring(6, 24);
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
            String s = ops.index(-1).toString();
            if (s == null) {
                break;
            }
            Long t = Long.valueOf(s);
            if (t == 0) {
                break;
            }
            Long nt = System.currentTimeMillis() - t;
            if (nt < pwdErrorRange) {
                break;
            }
            ops.rightPop();
        }
        ops.leftPush(String.valueOf(System.currentTimeMillis()));
        if (ops.size() < pwdErrorCount) {
            return;
        }
        //如果当前用户不是锁定状态，则进行锁定
        if (!user.getState().equals(UserState.LOCKED.getCode())) {
            User updateUser = DTOUtils.newDTO(User.class);
            updateUser.setId(user.getId());
            updateUser.setState(UserState.LOCKED.getCode());
            this.userMapper.updateByPrimaryKeySelective(updateUser);
        }
    }
}