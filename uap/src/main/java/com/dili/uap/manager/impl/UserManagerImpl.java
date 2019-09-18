package com.dili.uap.manager.impl;

import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.manager.UserManager;
import com.dili.uap.sdk.manager.SessionRedisManager;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.session.SessionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Component
public class UserManagerImpl implements UserManager {
	private final static Logger LOG = LoggerFactory.getLogger(UserManagerImpl.class);

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserRedis userRedis;
	@Autowired
	private SessionRedisManager sessionRedisManager;

	@Override
	public void clearSession(String sessionId) {
		LOG.debug("--- Clear User SessionData --- : SessionId - " + sessionId);
		//参考loginServiceImpl.login(LoginDto loginDto)和loginServiceImpl.makeRedisTag()方法
		// ------------------------------------------------

		// SessionConstants.SESSION_KEY_PREFIX + sessionId: JSON.toJSONString(sessionData) : SessionConstants.SESSION_TIMEOUT
		this.redisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + sessionId);

		//清空 key为SessionConstants.USERID_SESSIONID_KEY + userId, 值为:用户信息的Map, key为sessionId和user 的缓存
		//先根据sessionId找到用户id
		//SessionConstants.SESSIONID_USERID_KEY + sessionId : userId : SessionConstants.SESSION_TIMEOUT
		//再根据userId，清除sessionId
		//SessionConstants.USERID_SESSIONID_KEY + userId : sessionId : SessionConstants.SESSION_TIMEOUT
		this.sessionRedisManager.clearUserIdSessionDataKeyBySessionId(sessionId);

		//清空key为SessionConstants.SESSIONID_USERID_KEY + sessionId， 值为用户id的缓存
		//SessionConstants.SESSIONID_USERID_KEY + sessionId : userId : SessionConstants.SESSION_TIMEOUT
		this.sessionRedisManager.clearSessionUserIdKey(sessionId);
	}

	@Override
	public List<String> clearUserSession(Long userId) {
		List<String> oldSessionIds = this.sessionRedisManager.getSessionIdsByUserId(userId.toString());
		if(CollectionUtils.isEmpty(oldSessionIds)){
			return null;
		}
		for(String oldSessionId : oldSessionIds) {
			LOG.debug("--- Clear User SessionData --- : SessionId - " + oldSessionId);
			this.redisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + oldSessionId);
			this.sessionRedisManager.clearUserIdSessionDataKey(userId.toString());
			this.sessionRedisManager.clearSessionUserIdKey(oldSessionId);
		}
		return oldSessionIds;
	}

	@Override
	public List<String> getOnlineUserIds() {
		return sessionRedisManager.getOnlineUserIds();
	}

}
