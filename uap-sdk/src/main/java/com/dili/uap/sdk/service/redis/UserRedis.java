package com.dili.uap.sdk.service.redis;

import com.alibaba.fastjson.JSON;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.config.DynamicConfig;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserToken;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.glossary.TokenStep;
import com.dili.uap.sdk.service.UserJwtService;
import com.dili.uap.sdk.util.KeyBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户redis操作
 */
@Service
public class UserRedis {

	private static final Logger log = LoggerFactory.getLogger(UserRedis.class);
	@Resource(name="manageRedisUtil")
	private ManageRedisUtil redisUtil;
	@Resource
	private UserJwtService userJwtService;
	@Resource
	private DynamicConfig dynamicConfig;
	@Resource
    UapRedisDistributedLock uapRedisDistributedLock;

	/**
	 * 申请新的accessToken
	 * 如果redis中的refreshToken超时，则返回空
	 * @param refreshToken
	 * @return
	 */
	public UserToken applyAccessToken(String refreshToken) {
		String userTicketJSON = redisUtil.get(KeyBuilder.buildRefreshTokenKey(refreshToken), String.class);
		if (StringUtils.isEmpty(userTicketJSON)) {
			return null;
		}
		UserToken userToken = DTOUtils.newInstance(UserToken.class);
		userToken.setRefreshToken(refreshToken);
		UserTicket userTicket = JSON.parseObject(userTicketJSON, UserTicket.class);
		userToken.setUserTicket(userTicket);
		//锁定获取accessToken，10秒
		if (uapRedisDistributedLock.tryGetLockSync(refreshToken, refreshToken, 10L)) {
			try {
				// 先从缓存中取 accessToken，
				// 如果取到，则说明前一个过期的请求已经生成了accessToken(并可能也推后了redis过期时长)
				String cachedAccessToken = redisUtil.get(refreshToken+":cache", String.class);
				if(cachedAccessToken != null){
					userToken.setAccessToken(cachedAccessToken);
					userToken.setTokenStep(TokenStep.REFRESH_CACHE.getCode());
				}else {// 根据redis中的userTicket重新签发
					String accessToken = userJwtService.generateUserTokenByRSA256(userTicket, SystemType.getSystemType(userTicket.getSystemType()));
					redisUtil.set(refreshToken+":cache", accessToken, 10L);
					userToken.setAccessToken(accessToken);
					userToken.setTokenStep(TokenStep.REFRESH_TOKEN.getCode());
				}
			} finally {
				//完成后释放锁
				if (uapRedisDistributedLock.exists(refreshToken)) {
					uapRedisDistributedLock.releaseLock(refreshToken, refreshToken);
				}
			}
		}else{
			log.error("获取分布式锁失败");
			return null;
		}
		return userToken;
	}

	/**
	 * 推迟redis session过期时间
	 * 
	 * @param refreshToken
	 * @param userTicket
	 */
	public void defer(String refreshToken, UserTicket userTicket) {
		Integer systemType = userTicket.getSystemType();
		Long userId = userTicket.getId();
		Long refreshTokenTimeout = dynamicConfig.getRefreshTokenTimeout(systemType);
		//推后当前refreshToken保存的用户信息
		redisUtil.expire(KeyBuilder.buildRefreshTokenKey(refreshToken), refreshTokenTimeout, TimeUnit.SECONDS);
		redisUtil.expire(KeyBuilder.buildUserIdRefreshTokenKey(userTicket.getId().toString(), systemType), refreshTokenTimeout, TimeUnit.SECONDS);
		//推后用户权限信息
		redisUtil.expire(KeyBuilder.buildUserSystemKey(userId.toString(), userTicket.getSystemType()), refreshTokenTimeout, TimeUnit.SECONDS);
		redisUtil.expire(KeyBuilder.buildUserMenuUrlKey(userId.toString(), userTicket.getSystemType()), refreshTokenTimeout, TimeUnit.SECONDS);
		redisUtil.expire(KeyBuilder.buildUserResourceCodeKey(userId.toString(), userTicket.getSystemType()), refreshTokenTimeout, TimeUnit.SECONDS);
		redisUtil.expire(KeyBuilder.buildUserDataAuthKey(userId.toString(), userTicket.getSystemType()), refreshTokenTimeout, TimeUnit.SECONDS);
	}

	/**
	 * 根据refreshToken清空redis中的RefreshToken和UserIdRefreshToken
	 * @param refreshToken
	 */
	public void clearByRefreshToken(String refreshToken) {
		// 参考loginServiceImpl.login(LoginDto loginDto)和loginServiceImpl.makeRedisTag()方法
		// ------------------------------------------------
		String userTicketJSON = redisUtil.get(KeyBuilder.buildRefreshTokenKey(refreshToken), String.class);
		if (!StringUtils.isEmpty(userTicketJSON)) {
			UserTicket userTicket = JSON.parseObject(userTicketJSON, UserTicket.class);
			redisUtil.remove(KeyBuilder.buildUserIdRefreshTokenKey(userTicket.getId().toString(), userTicket.getSystemType()));
		}
		redisUtil.remove(KeyBuilder.buildRefreshTokenKey(refreshToken));

	}

	/**
	 * 根据userId清空用户redis中的refreshToken数据，返回被清空的refreshToken列表
	 * 如果该用户没有refreshToken，则返回null
	 * @param userId
	 * @return
	 */
	public Set<String> clearByUserId(Long userId, Integer systemType) {
		Set<String> refreshTokens = this.findRefreshTokensByUserId(userId.toString(), systemType);
		if (CollectionUtils.isEmpty(refreshTokens)) {
			return null;
		}
		for (String oldRefreshToken : refreshTokens) {
			this.redisUtil.remove(KeyBuilder.buildRefreshTokenKey(oldRefreshToken));
		}
		this.redisUtil.remove(KeyBuilder.buildUserIdRefreshTokenKey(userId.toString(), systemType));
		return refreshTokens;
	}

	/**
	 * 根据用户id获取refreshTokens
	 * @param userId
	 * @return
	 */
	public Set<String> findRefreshTokensByUserId(String userId, Integer systemType) {
		BoundSetOperations<String, String> userIdSessionIds = redisUtil.getRedisTemplate().boundSetOps(KeyBuilder.buildUserIdRefreshTokenKey(userId, systemType));
//		List<String> sessionIds = Lists.newArrayList();
//		//根据类型过滤
//		for(String sessionId : userIdSessionIds.members()) {
//			sessionIds.add(sessionId);
//		}
		return userIdSessionIds.members();
	}

	/**
	 * 获取当前在线的用户ID
	 * @return
	 */
	public List<String> listOnlineUserIds() {
		Set<String> keys = redisUtil.getRedisTemplate().keys(SessionConstants.USERID_REFRESH_TOKEN_KEY + "*");
		if (keys == null || keys.isEmpty()) {
			return Lists.newArrayList();
		}
		List<String> userIds = Lists.newArrayList();
		for (String key : keys) {
			userIds.add(key.substring(SessionConstants.USERID_REFRESH_TOKEN_KEY.length()+2));
		}
		return userIds;
	}

}
