package com.dili.uap.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.SystemConfigMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.glossary.UserState;
import com.dili.uap.manager.*;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.manager.SessionRedisManager;
import com.dili.uap.sdk.session.DynaSessionConstants;
import com.dili.uap.sdk.session.ManageConfig;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.dili.uap.sdk.util.WebContent;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 登录服务 Created by asiam on 2018/5/18 0018.
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

	// 密码错误范围，默认十分钟
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
	private SystemService systemService;

	@Autowired
	private SessionRedisManager sessionManager;

	@Autowired
	private FirmMapper firmMapper;

	@Autowired
	private DynaSessionConstants dynaSessionConstants;

	@Override
	public BaseOutput<String> validate(LoginDto loginDto) {
		if (loginDto.getUserName().length() < 2 || loginDto.getUserName().length() > 20) {
			return BaseOutput.failure("用户名长度不能小于2位或大于20位!").setCode(ResultCode.PARAMS_ERROR);
		}
		if (loginDto.getPassword().length() < 6 || loginDto.getPassword().length() > 20) {
			return BaseOutput.failure("密码长度不能小于6位或大于20位!").setCode(ResultCode.PARAMS_ERROR);
		}
		User record = DTOUtils.newInstance(User.class);
		record.setUserName(loginDto.getUserName());
		User user = this.userMapper.selectOne(record);
		if (user == null) {
			return BaseOutput.failure("用户不存在");
		}
		// 用户状态为锁定和禁用不允许登录
		if (user.getState().equals(UserState.LOCKED.getCode())) {
			return BaseOutput.failure("用户已被锁定，请联系管理员").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		if (user.getState().equals(UserState.DISABLED.getCode())) {
			return BaseOutput.failure("用户已被禁用，请联系管理员!");
		}
		// 判断密码不正确，三次后锁定用户、锁定后的用户12小时后自动解锁
		if (!StringUtils.equals(user.getPassword(), this.encryptPwd(loginDto.getPassword()))) {
			lockUser(user);
			return BaseOutput.failure("用户名或密码错误").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		// 登录成功后清除锁定计时
		clearUserLock(user.getId());
		return BaseOutput.success("登录成功");
	}

	@Override
	public BaseOutput<String> validateSaveSession(LoginDto loginDto) {
		if (loginDto.getUserName().length() < 2 || loginDto.getUserName().length() > 20) {
			return BaseOutput.failure("用户名长度不能小于2位或大于20位!").setCode(ResultCode.PARAMS_ERROR);
		}
		if (loginDto.getPassword().length() < 6 || loginDto.getPassword().length() > 20) {
			return BaseOutput.failure("密码长度不能小于6位或大于20位!").setCode(ResultCode.PARAMS_ERROR);
		}
		User record = DTOUtils.newInstance(User.class);
		record.setUserName(loginDto.getUserName());
		User user = this.userMapper.selectOne(record);
		if (user == null) {
			return BaseOutput.failure("用户不存在");
		}
		// 用户状态为锁定和禁用不允许登录
		if (user.getState().equals(UserState.LOCKED.getCode())) {
			return BaseOutput.failure("用户已被锁定，请联系管理员").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		if (user.getState().equals(UserState.DISABLED.getCode())) {
			return BaseOutput.failure("用户已被禁用，请联系管理员!");
		}
		// 判断密码不正确，三次后锁定用户、锁定后的用户12小时后自动解锁
		if (!StringUtils.equals(user.getPassword(), this.encryptPwd(loginDto.getPassword()))) {
			lockUser(user);
			return BaseOutput.failure("用户名或密码错误").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		// 登录成功后清除锁定计时
		clearUserLock(user.getId());
		// 加载用户系统
		this.systemManager.initUserSystemInRedis(user.getId());
		// 加载用户url
		this.menuManager.initUserMenuUrlsInRedis(user.getId());
		// 加载用户resource
		this.resourceManager.initUserResourceCodeInRedis(user.getId());
		// 加载用户数据权限
		this.dataAuthManager.initUserDataAuthesInRedis(user.getId());

		LOG.info(String.format("用户登录成功，用户名[%s] | 用户IP[%s]", loginDto.getUserName(), loginDto.getIp()));
		// 用户登陆 挤掉 旧登陆用户
		jamUser(user);
		String sessionId = UUID.randomUUID().toString();
		// 缓存用户相关信息到Redis
		makeRedisTag(user, sessionId);
		return BaseOutput.success("登录成功").setData(sessionId);
	}

	@Override
	public BaseOutput<LoginResult> login(LoginDto loginDto) {
		try {
			if (loginDto.getUserName().length() < 2 || loginDto.getUserName().length() > 20) {
				return BaseOutput.failure("用户名长度不能小于2位或大于20位!").setCode(ResultCode.PARAMS_ERROR);
			}
			if (loginDto.getPassword().length() < 6 || loginDto.getPassword().length() > 20) {
				return BaseOutput.failure("密码长度不能小于6位或大于20位!").setCode(ResultCode.PARAMS_ERROR);
			}
			User record = DTOUtils.newInstance(User.class);
			record.setUserName(loginDto.getUserName());
			User user = this.userMapper.selectOne(record);
			if (user == null) {
				return BaseOutput.failure("用户名或密码错误");
			}
			// 设置默认登录系统为UAP
			if (StringUtils.isBlank(loginDto.getSystemCode())) {
				loginDto.setSystemCode(UapConstants.UAP_SYSTEM_CODE);
			}
			// 记录用户id和市场编码，用于记录登录日志
			loginDto.setUserId(user.getId());
			loginDto.setFirmCode(user.getFirmCode());
			// 用户状态为锁定和禁用不允许登录
			if (user.getState().equals(UserState.LOCKED.getCode())) {
				logLogin(user, loginDto, false, "用户已被锁定，请联系管理员");
				return BaseOutput.failure("用户已被锁定，请联系管理员").setCode(ResultCode.NOT_AUTH_ERROR);
			}
			if (user.getState().equals(UserState.DISABLED.getCode())) {
				logLogin(user, loginDto, false, "用户已被禁用，请联系管理员");
				return BaseOutput.failure("用户已被禁用，请联系管理员!");
			}
			// 判断密码不正确，三次后锁定用户、锁定后的用户12小时后自动解锁
			if (!StringUtils.equals(user.getPassword(), this.encryptPwd(loginDto.getPassword()))) {
				lockUser(user);
				logLogin(user, loginDto, false, "用户名或密码错误");
				return BaseOutput.failure("用户名或密码错误").setCode(ResultCode.NOT_AUTH_ERROR);
			}
			// 登录成功后清除锁定计时
			clearUserLock(user.getId());
			// 加载用户系统
			this.systemManager.initUserSystemInRedis(user.getId());
			// 加载用户url
			this.menuManager.initUserMenuUrlsInRedis(user.getId());
			// 加载用户resource
			this.resourceManager.initUserResourceCodeInRedis(user.getId());
			// 加载用户数据权限
			this.dataAuthManager.initUserDataAuthesInRedis(user.getId());

			// 原来的代码是更新用户的最后登录IP和最后登录时间，现在暂时不需要了
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
			// 缓存用户相关信息到Redis
			makeRedisTag(user, sessionId);
			// 构建返回的登录信息
			LoginResult loginResult = DTOUtils.newInstance(LoginResult.class);
			// 返回用户信息需要屏蔽用户的密码
			user.setPassword(null);
			loginResult.setUser(user);
			loginResult.setSessionId(sessionId);
			loginResult.setLoginPath(loginDto.getLoginPath());
			logLogin(user, loginDto, true, "登录成功");
			return BaseOutput.success("登录成功").setData(loginResult);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			if (loginDto.getUserId() != null) {
				logLogin(null, loginDto, false, e.getMessage());
			}
			return BaseOutput.failure("登录失败").setMessage("登录失败，参数不正确");
		}
	}

	@Override
	public BaseOutput<Boolean> loginAndTag(LoginDto loginDto) {
		BaseOutput<LoginResult> output = this.login(loginDto);
		if (!output.isSuccess()) {
			return BaseOutput.failure(output.getMessage()).setCode(output.getCode()).setData(false);
		}
		makeCookieTag(output.getData().getUser(), output.getData().getSessionId());
		return BaseOutput.success("登录成功");
	}

	@Override
	public BaseOutput<Boolean> loginBySession(String sessionId) {
		String userId = this.sessionManager.getUserIdBySessionId(sessionId);
		if (StringUtils.isBlank(userId)) {
			return BaseOutput.failure("用户未登录！");
		}
		User record = DTOUtils.newInstance(User.class);
		record.setId(Long.valueOf(userId));
		User user = this.userMapper.selectOne(record);
		Firm firmQuery = DTOUtils.newInstance(Firm.class);
		firmQuery.setCode(user.getFirmCode());
		Firm firm = this.firmMapper.selectOne(firmQuery);
		if (firm != null) {
			WebContent.put("firmId", firm.getId());
		}
		makeCookieTag(user, sessionId);
		return BaseOutput.success("登录成功");
	}

	// ================================= 私有方法分割线
	// ====================================

	/**
	 * 异步记录登录日志
	 */
	private void logLogin(User user, LoginDto loginDto, boolean isSuccess, String msg) {
		// 设置系统名称
		LoggerContext.put(LoggerConstant.LOG_SYSTEM_CODE_KEY, loginDto.getSystemCode());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, loginDto.getUserName());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, loginDto.getUserId());
		LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, loginDto.getUserId());
		if (user != null) {
			LoggerContext.put(LoggerConstant.LOG_NOTES_KEY, user.getRealName());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, user.getFirmCode());
		}
		LoggerContext.put("msg", msg);
	}

	/**
	 * 异步记录登出日志
	 */
	@Override
	public void logLogout(UserTicket user) {
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
		LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, user.getId());
		LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, user.getFirmId());
		LoggerContext.put(LoggerConstant.LOG_NOTES_KEY, user.getRealName());
		LoggerContext.put("msg", "系统登出");
	}

	/**
	 * 用户登陆 挤掉 旧登陆用户
	 *
	 * @param user
	 */
	private void jamUser(User user) {
		if (this.manageConfig.getUserLimitOne() && this.sessionRedisManager.existUserIdSessionIdKey(user.getId().toString())) {
			List<String> oldSessionIds = this.userManager.clearUserSession(user.getId());
			if (oldSessionIds == null) {
				return;
			}
			for (String oldSessionId : oldSessionIds) {
				// 为了提示
				this.sessionRedisManager.addKickSessionKey(oldSessionId);
			}
		}
	}

	/**
	 * 记录用户信息和登录地址到Cookie
	 * 
	 * @param user
	 * @param sessionId
	 */
	private void makeCookieTag(User user, String sessionId) {
		String referer = WebUtil.fetchReferer(WebContent.getRequest());
		WebContent.setCookie(SessionConstants.COOKIE_SESSION_ID, sessionId);
		WebContent.setCookie(SessionConstants.COOKIE_USER_ID_KEY, user.getId().toString());
		WebContent.setCookie(SessionConstants.COOKIE_USER_NAME_KEY, user.getUserName());
		WebContent.setCookie(SessionConstants.COOKIE_FIRM_ID_KEY, String.valueOf(WebContent.get("firmId")));
		WebContent.setCookie(SessionConstants.COOKIE_LOGIN_PATH_KEY, referer);
	}

	/**
	 * 缓存用户相关信息到Redis
	 * 
	 * @param user
	 * @param sessionId
	 */
	private void makeRedisTag(User user, String sessionId) {
		Map<String, Object> sessionData = new HashMap<>(1);
		// 根据firmCode查询firmId，放入UserTicket
		Firm condition = DTOUtils.newInstance(Firm.class);
		condition.setCode(user.getFirmCode());
		Firm firm = firmMapper.selectOne(condition);
		UserTicket userTicket = DTOUtils.asInstance(user, UserTicket.class);
		if (firm != null) {
			// 用于makeCookieTag中获取firmId
			WebContent.put("firmId", firm.getId());
			userTicket.setFirmId(firm.getId());
			userTicket.setFirmName(firm.getName());
		}
		sessionData.put(SessionConstants.LOGGED_USER, JSON.toJSONString(userTicket));

		LOG.debug("--- Save Session Data To Redis ---");
		// redis: ressionId - user
		// 用于在SDK中根据sessionId获取用户信息，如果sessionId不存在，过期或者被挤，都会被权限系统拦截
		this.redisUtil.set(SessionConstants.SESSION_KEY_PREFIX + sessionId, JSON.toJSONString(sessionData), dynaSessionConstants.getSessionTimeout());
		// redis: sessionId - userID
		this.sessionRedisManager.setSessionUserIdKey(sessionId, user.getId().toString());
		// redis: sessionId - userName
		this.sessionRedisManager.setSessionUserNameKey(sessionId, user.getUserName());
		// redis: userID - sessionId
		this.sessionRedisManager.setUserIdSessionIdKey(user.getId().toString(), sessionId);
		LOG.debug("UserName: " + user.getUserName() + " | SessionId:" + sessionId + " | SessionData:" + sessionData);
	}

	/**
	 * 加密
	 * 
	 * @param passwd
	 * @return
	 */
	private String encryptPwd(String passwd) {
		return md5Util.getMD5ofStr(passwd).substring(6, 24);
	}

	/**
	 * 清空用户登录密码错误锁定次数
	 * 
	 * @param userId
	 */
	private void clearUserLock(Long userId) {
		String key = SessionConstants.USER_PWD_ERROR_KEY + userId;
		redisUtil.getRedisTemplate().delete(key);
	}

	/**
	 * 锁定用户
	 * 
	 * @param user
	 */
	@Override
	public boolean lockUser(User user) {
		// 判断是否要进行密码错误检查，不检查就不需要锁定用户了
		if (!pwdErrorCheck) {
			return false;
		}
		if (user == null) {
			return false;
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
		// 查询系统配置的密码错误锁定次数
		SystemConfig systemConfigCondition = DTOUtils.newInstance(SystemConfig.class);
		systemConfigCondition.setCode(UapConstants.LOGIN_FAILED_TIMES);
		systemConfigCondition.setSystemCode(UapConstants.UAP_SYSTEM_CODE);
		SystemConfig systemConfig = systemConfigMapper.selectOne(systemConfigCondition);
		// 以系统变量配置为主，没有则使用配置文件中的配置
		if (StringUtils.isNotBlank(systemConfig.getValue())) {
			pwdErrorCount = Integer.parseInt(systemConfig.getValue());
		}
		// 在锁定次数范围内不锁定
		if (ops.size() < Integer.parseInt(systemConfig.getValue())) {
			return false;
		}
		// 如果当前用户不是锁定状态，则进行锁定
		if (!user.getState().equals(UserState.LOCKED.getCode())) {
			User updateUser = DTOUtils.newInstance(User.class);
			updateUser.setId(user.getId());
			updateUser.setLocked(new Date());
			updateUser.setState(UserState.LOCKED.getCode());
			this.userMapper.updateByPrimaryKeySelective(updateUser);
			// 清空计时器
			redisUtil.getRedisTemplate().delete(key);
			return true;
		}
		return false;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<LoginResult> loginFromApp(LoginDto loginDto) {
		BaseOutput<LoginResult> result = this.login(loginDto);
		if (!result.isSuccess()) {
			return result;
		}
//		if (StringUtils.isNotBlank(loginDto.getPushId())) {
//			User user = this.userMapper.selectByPrimaryKey(result.getData().getUser().getId());
//			int rows = this.userMapper.updateByPrimaryKeySelective(user);
//			if (rows <= 0) {
//				throw new AppException("更新推送id失败");
//			}
//		}
		return result;

	}

}