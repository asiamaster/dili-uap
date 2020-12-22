package com.dili.uap.sdk.manager.impl;

import com.dili.uap.sdk.manager.SessionRedisManager;
import com.dili.uap.sdk.session.DynaSessionConstants;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户session redis管理者 Created by asiamaster
 */
@Component
public class SessionRedisManagerImpl implements SessionRedisManager {

	@Autowired
	private ManageRedisUtil myRedisUtil;
	@Autowired
	private DynaSessionConstants dynaSessionConstants;

	@Override
	public void setUserIdSessionIdKey(String userId, String sessionId) {
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + userId);
		userIdSessionIds.add(sessionId);
		userIdSessionIds.expire(dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
//		myRedisUtil.set(SessionConstants.USERID_SESSIONID_KEY + user.getId(), sessionId,
//				SessionConstants.SESSION_TIMEOUT);
	}

	@Override
	public List<String> getSessionIdsByUserId(String userId) {
//		return myRedisUtil.get(SessionConstants.USERID_SESSIONID_KEY + userId, String.class);
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + userId);
		List<String> sessionIds = Lists.newArrayList();
		while (true) {
			String sessionId = userIdSessionIds.pop();
			if (sessionId == null) {
				break;
			}
			sessionIds.add(sessionId);
		}
		return sessionIds;
	}

	@Override
	public List<String> getTokensByUserId(String userId) {
		BoundSetOperations<String, String> userIdTokens = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_TOKEN_KEY + userId);
		List<String> tokens = Lists.newArrayList();
		while (true) {
			String token = userIdTokens.pop();
			if (token == null) {
				break;
			}
			tokens.add(token);
		}
		return tokens;
	}

	@Override
	public List<String> getOnlineUserIds() {
		Set<String> keys = myRedisUtil.getRedisTemplate().keys(SessionConstants.USERID_SESSIONID_KEY + "*");
		if (keys == null || keys.isEmpty()) {
			return Lists.newArrayList();
		}
		List<String> userIds = Lists.newArrayList();
		for (String key : keys) {
			userIds.add(key.substring(SessionConstants.USERID_SESSIONID_KEY.length()));
		}
		return userIds;
	}

	@Override
	public List<String> getOnlineUserSessionIds() {
		Set<String> keys = myRedisUtil.getRedisTemplate().keys(SessionConstants.SESSIONID_USERID_KEY + "*");
		if (keys == null || keys.isEmpty()) {
			return Lists.newArrayList();
		}
		List<String> sessionIds = Lists.newArrayList();
		for (String key : keys) {
			sessionIds.add(key.substring(SessionConstants.SESSIONID_USERID_KEY.length()));
		}
		return sessionIds;
	}

	@Override
	public Boolean existUserIdSessionIdKey(String userId) {
//		return myRedisUtil.exists(SessionConstants.USERID_SESSIONID_KEY + s);
		return myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + userId).isMember(userId);
	}

	@Override
	public void clearUserIdSessionDataKey(String userId) {
		myRedisUtil.remove(SessionConstants.USERID_SESSIONID_KEY + userId);
	}

	@Override
	public void clearUserIdTokenDataKey(String userId) {
		myRedisUtil.remove(SessionConstants.USERID_TOKEN_KEY + userId);
	}

	@Override
	public void clearUserIdSessionDataKeyBySessionId(String sessionId) {
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + getUserIdBySessionId(sessionId));
		userIdSessionIds.remove(sessionId);
	}

	@Override
	public void addKickSessionKey(String oldSessionId) {
		myRedisUtil.set(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId, "", SessionConstants.COOKIE_TIMEOUT.longValue());
	}

	@Override
	public void addKickTokenKey(String oldToken) {
		myRedisUtil.set(SessionConstants.KICK_OLDTOKEN_KEY + oldToken, "", dynaSessionConstants.getTokenTimeout());
	}

	@Override
	public Boolean existKickSessionKey(String oldSessionId) {
		return myRedisUtil.exists(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId);
	}

	@Override
	public void clearKickSessionKey(String oldSessionId) {
		myRedisUtil.remove(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId);
	}

	// sessionId - userId 操作 - START
	@Override
	public void setSessionUserIdKey(String sessionId, String userId) {
		myRedisUtil.set(SessionConstants.SESSIONID_USERID_KEY + sessionId, userId, dynaSessionConstants.getSessionTimeout());
	}

	// sessionId - userName 操作 - START
	@Override
	public void setSessionUserNameKey(String sessionId, String userName) {
		myRedisUtil.set(SessionConstants.SESSIONID_USERNAME_KEY + sessionId, userName, dynaSessionConstants.getSessionTimeout());
	}

	@Override
	public String getUserIdBySessionId(String sessionId) {
		return myRedisUtil.get(SessionConstants.SESSIONID_USERID_KEY + sessionId, String.class);
	}

	@Override
	public String getUserNameBySessionId(String sessionId) {
		return myRedisUtil.get(SessionConstants.SESSIONID_USERNAME_KEY + sessionId, String.class);
	}

	@Override
	public void clearSessionUserIdKey(String sessionId) {
		myRedisUtil.remove(SessionConstants.SESSIONID_USERID_KEY + sessionId);
	}

	@Override
	public void clearTokenUserIdKey(String token) {
		myRedisUtil.remove(SessionConstants.TOKEN_USERID_KEY + token);
	}

	@Override
	public void setTokenUserIdKey(String token, String userId) {
		myRedisUtil.set(SessionConstants.TOKEN_USERID_KEY + token, userId, dynaSessionConstants.getTokenTimeout());
	}

	@Override
	public void setTokenUserNameKey(String token, String userName) {
		myRedisUtil.set(SessionConstants.TOKEN_USERNAME_KEY + token, userName, dynaSessionConstants.getTokenTimeout());
	}

	@Override
	public void setUserIdTokenKey(String userId, String token) {
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_TOKEN_KEY + userId);
		userIdSessionIds.add(token);
		userIdSessionIds.expire(dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
	}

	@Override
	public Boolean existUserIdTokenKey(String userId) {
		return myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_TOKEN_KEY + userId).isMember(userId);
	}

	@Override
	public String getUserIdByToken(String token) {
		return myRedisUtil.get(SessionConstants.TOKEN_USERID_KEY + token, String.class);
	}

	// sessionId - userId 操作 - END
}
