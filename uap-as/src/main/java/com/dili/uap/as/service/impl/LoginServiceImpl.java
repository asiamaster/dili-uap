package com.dili.uap.as.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.uap.as.constants.UapConstants;
import com.dili.uap.as.domain.dto.LoginDto;
import com.dili.uap.as.domain.dto.LoginResult;
import com.dili.uap.as.glossary.UserState;
import com.dili.uap.as.manager.DataAuthManager;
import com.dili.uap.as.manager.MenuManager;
import com.dili.uap.as.manager.ResourceManager;
import com.dili.uap.as.manager.SystemManager;
import com.dili.uap.as.mapper.FirmMapper;
import com.dili.uap.as.mapper.SystemConfigMapper;
import com.dili.uap.as.mapper.UserMapper;
import com.dili.uap.as.mapper.UserPushInfoMapper;
import com.dili.uap.as.service.LoginService;
import com.dili.uap.as.util.MD5Util;
import com.dili.uap.as.util.WebUtil;
import com.dili.uap.sdk.config.DynamicConfig;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.domain.*;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.service.AuthService;
import com.dili.uap.sdk.service.UserJwtService;
import com.dili.uap.sdk.service.redis.ManageRedisUtil;
import com.dili.uap.sdk.util.KeyBuilder;
import com.dili.uap.sdk.util.WebContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务 Created by asiam on 2018/5/18 0018.
 */
@Component
public class LoginServiceImpl implements LoginService {

	private final static Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
	@SuppressWarnings("all")
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

	@Resource(name="manageRedisUtil")
	private ManageRedisUtil redisUtil;

	@Autowired
	private MenuManager menuManager;

	@Autowired
	private ResourceManager resourceManager;

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private DataAuthManager dataAuthManager;
	@SuppressWarnings("all")
	@Autowired
	private SystemConfigMapper systemConfigMapper;

	@Autowired
	private UserJwtService userJwtService;

	@Autowired
	private AuthService authService;

	@Resource
	private DynamicConfig dynamicConfig;
	@SuppressWarnings("all")
	@Autowired
	private FirmMapper firmMapper;
	@SuppressWarnings("all")
	@Autowired
	private UserPushInfoMapper userPushInfoMapper;

	@Override
	public BaseOutput<String> validate(LoginDto loginDto) {
		BaseOutput output = loginCheck(loginDto);
		return output.isSuccess() ? BaseOutput.success("验证通过"): output;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<LoginResult> loginWeb(LoginDto loginDto) {
		return login(loginDto, SystemType.WEB.getCode());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<LoginResult> loginApp(LoginDto loginDto) {
		BaseOutput<LoginResult> result = login(loginDto, SystemType.APP.getCode());
		if (!result.isSuccess()) {
			return result;
		}
		if (StringUtils.isNotBlank(loginDto.getDeviceType()) && StringUtils.isNotBlank(loginDto.getPushId())) {
			this.userPushInfoMapper.deleteByUserIdOrPushId(result.getData().getUser().getId(), loginDto.getPushId());
			UserPushInfo condition = DTOUtils.newInstance(UserPushInfo.class);
			condition.setUserId(result.getData().getUser().getId());
			condition.setPushId(loginDto.getPushId());
			condition.setPlatform(loginDto.getDeviceType());
			int rows = this.userPushInfoMapper.insert(condition);
			if (rows <= 0) {
				throw new AppException("更新移动端推送信息失败");
			}
		}
		return result;
	}

	@Override
	public BaseOutput<Boolean> loginAndTag(LoginDto loginDto) {
		BaseOutput<LoginResult> output = this.loginWeb(loginDto);
		if (!output.isSuccess()) {
			return BaseOutput.failure(output.getMessage()).setCode(output.getCode()).setData(false);
		}
		this.updateLoginTime(output.getData().getUser());
		makeCookieTag(output.getData().getAccessToken(), output.getData().getRefreshToken());
		return BaseOutput.success("登录成功");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Boolean> loginByTokens(String accessToken, String refreshToken) {
		UserTicket userTicket = this.authService.getUserTicket(accessToken, refreshToken);
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录！");
		}
		User record = DTOUtils.newInstance(User.class);
		record.setId(userTicket.getId());
		User user = this.userMapper.selectOne(record);
		this.updateLoginTime(user);
		makeCookieTag(accessToken, refreshToken);
		return BaseOutput.success("登录成功");
	}

	// ================================= 私有方法分割线  ====================================

	/**
	 * 根据系统类型登录
	 * @param loginDto
	 * @param systemType
	 * @return
	 */
	private BaseOutput<LoginResult> login(LoginDto loginDto, Integer systemType) {
		try {
			BaseOutput<User> output = loginCheck(loginDto);
			if(!output.isSuccess()){
				return (BaseOutput)output;
			}
			User user = output.getData();
			//更新用户登录时间
			this.updateLoginTime(user);
			// 登录成功后清除锁定计时
			clearUserLock(user.getId());
			// 加载用户系统
			this.systemManager.initUserSystemInRedis(user.getId(), systemType);
			// 加载用户url
			this.menuManager.initUserMenuUrlsInRedis(user.getId(), systemType);
			// 加载用户resource
			this.resourceManager.initUserResourceCodeInRedis(user.getId(), systemType);
			// 加载用户数据权限
			this.dataAuthManager.initUserDataAuthesInRedis(user.getId(), systemType);

//			LOG.info(String.format("用户登录成功，用户名[%s] | 用户IP[%s]", loginDto.getUserName(), loginDto.getIp()));
			Firm condition = DTOUtils.newInstance(Firm.class);
			condition.setCode(user.getFirmCode());
			Firm firm = firmMapper.selectOne(condition);
			UserTicket userTicket = DTOUtils.asInstance(user, UserTicket.class);
			//设置登录的系统，用于区别不同的超时时间
			userTicket.setSystemType(systemType);
			if (firm != null) {
				// 用于makeCookieTag中获取firmId
//				WebContent.put("firmId", firm.getId());
				userTicket.setFirmId(firm.getId());
				userTicket.setFirmName(firm.getName());
			}
			// 构建返回的登录信息
			LoginResult loginResult = DTOUtils.newInstance(LoginResult.class);
			// 返回用户信息需要屏蔽用户的密码
			user.setPassword(null);
			loginResult.setUser(userTicket);
			String accessToken = userJwtService.generateUserTokenByRSA256(userTicket, SystemType.getSystemType(systemType));
			Long accessTokenTimeout = dynamicConfig.getAccessTokenTimeout(SystemType.getSystemType(systemType).getCode());
			Long refreshTokenTimeout = dynamicConfig.getRefreshTokenTimeout(SystemType.getSystemType(systemType).getCode());
			//刷新token
			String refreshToken = UUID.randomUUID().toString();
			loginResult.setAccessToken(accessToken);
			loginResult.setRefreshToken(refreshToken);
			loginResult.setAccessTokenTimeout(accessTokenTimeout);
			loginResult.setRefreshTokenTimeout(refreshTokenTimeout);
			loginResult.setLoginPath(loginDto.getLoginPath());
			saveTokenInRedis(userTicket, refreshToken, systemType);
			logLogin(user, loginDto, true, "登录成功");
			return BaseOutput.success("登录成功").setData(loginResult);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			if (loginDto.getUserId() != null) {
				logLogin(null, loginDto, false, e.getMessage());
			}
			return BaseOutput.failure("登录失败").setMessage("登录失败:"+e.getMessage());
		}
	}

	/**
	 * 登录校验
	 * 返回根据用户名查询出的用户信息
	 * @param loginDto
	 * @return
	 */
	private BaseOutput<User> loginCheck(LoginDto loginDto){
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
		return BaseOutput.successData(user);
	}

	/**
	 * 保存相关登录信息到redis
	 * @param user
	 * @param refreshToken
	 * @param systemType 不同系统类型，超时时间不同
	 */
	private void saveTokenInRedis(UserTicket user, String refreshToken, Integer systemType) {
		Long refreshTokenTimeOut = dynamicConfig.getRefreshTokenTimeout(systemType);
		// 用于在SDK中根据sessionId获取用户信息，如果sessionId不存在，过期或者被挤，都会被权限系统拦截
		this.redisUtil.set(KeyBuilder.buildRefreshTokenKey(refreshToken), JSON.toJSONString(user), refreshTokenTimeOut);
		// redis: userID - token
		setUserIdRefreshTokens(user.getId().toString(), systemType, refreshToken, refreshTokenTimeOut);
	}

	/**
	 * 用户id和refreshToken的1对多关系保存到redis
	 * @param userId
	 * @param refreshToken
	 */
	public void setUserIdRefreshTokens(String userId, Integer systemType, String refreshToken, Long refreshTokenTimeOut) {
		BoundSetOperations<String, String> userIdRefreshTokens = redisUtil.getRedisTemplate().boundSetOps(
				KeyBuilder.buildUserIdRefreshTokenKey(userId, systemType));
		userIdRefreshTokens.add(refreshToken);
		userIdRefreshTokens.expire(refreshTokenTimeOut, TimeUnit.SECONDS);
	}

	/**
	 * 更新用户登录时间
	 * @param user
	 */
	private void updateLoginTime(User user) {
		user.setLastLoginTime(new Date());
		int rows = this.userMapper.updateByPrimaryKeySelective(user);
		if (rows <= 0) {
			throw new AppException("更新用户登陆时间失败");
		}
	}

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
	
//	/**
//	 * 用户登陆 挤掉 旧token登陆用户
//	 *
//	 * @param user
//	 */
//	private void jamTokenUser(User user) {
//		if (this.manageConfig.getUserLimitOne() && this.sessionRedisManager.existUserIdTokenKey(user.getId().toString())) {
//			List<String> oldTokens = this.userManager.clearUserToken(user.getId());
//			if (oldTokens == null) {
//				return;
//			}
//			for (String oldToken : oldTokens) {
//				// 为了提示
//				this.sessionRedisManager.addKickTokenKey(oldToken);
//			}
//		}
//	}

	/**
	 * 记录token和登录地址到Cookie
	 * @param accessToken
	 * @param refreshToken
	 */
	private void makeCookieTag(String accessToken, String refreshToken) {
		String referer = WebUtil.fetchReferer(WebContent.getRequest());
		WebContent.setCookie(SessionConstants.ACCESS_TOKEN_KEY, accessToken);
		WebContent.setCookie(SessionConstants.REFRESH_TOKEN_KEY, refreshToken);
//		WebContent.setCookie(SessionConstants.COOKIE_USER_ID_KEY, user.getId().toString());
//		WebContent.setCookie(SessionConstants.COOKIE_USER_NAME_KEY, user.getUserName());
//		WebContent.setCookie(SessionConstants.COOKIE_FIRM_ID_KEY, String.valueOf(WebContent.get("firmId")));
		WebContent.setCookie(SessionConstants.COOKIE_LOGIN_PATH_KEY, referer);
	}

	/**
	 * 缓存用户相关信息到Redis
	 * 
	 * @param user
	 * @param sessionId
	 */
//	private void makeRedisTag(User user, String sessionId) {
//		Map<String, Object> sessionData = new HashMap<>(1);
//		// 根据firmCode查询firmId，放入UserTicket
//		Firm condition = DTOUtils.newInstance(Firm.class);
//		condition.setCode(user.getFirmCode());
//		Firm firm = firmMapper.selectOne(condition);
//		UserTicket userTicket = DTOUtils.asInstance(user, UserTicket.class);
//		if (firm != null) {
//			// 用于makeCookieTag中获取firmId
//			WebContent.put("firmId", firm.getId());
//			userTicket.setFirmId(firm.getId());
//			userTicket.setFirmName(firm.getName());
//		}
//		sessionData.put(SessionConstants.LOGGED_USER, JSON.toJSONString(userTicket));
//
//		LOG.debug("--- Save Session Data To Redis ---");
//		// redis: ressionId - user
//		// 用于在SDK中根据sessionId获取用户信息，如果sessionId不存在，过期或者被挤，都会被权限系统拦截
//		this.redisUtil.set(SessionConstants.SESSION_KEY_PREFIX + sessionId, JSON.toJSONString(sessionData), dynaSessionConstants.getSessionTimeout());
//		// redis: sessionId - userID
//		this.sessionRedisManager.setSessionUserIdKey(sessionId, user.getId().toString());
//		// redis: sessionId - userName
//		this.sessionRedisManager.setSessionUserNameKey(sessionId, user.getUserName());
//		// redis: userID - sessionId
//		this.sessionRedisManager.setUserIdSessionIdKey(user.getId().toString(), sessionId);
//		LOG.debug("UserName: " + user.getUserName() + " | SessionId:" + sessionId + " | SessionData:" + sessionData);
//	}
	
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
		redisUtil.remove(SessionConstants.USER_PWD_ERROR_KEY + userId);
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
			Long nt = System.currentTimeMillis() - t;
			if (nt < pwdErrorRange) {
				break;
			}
			ops.rightPop();
		}
		ops.leftPush(String.valueOf(System.currentTimeMillis()));
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

}