package com.dili.uap.sdk.manager.impl;

import com.dili.uap.sdk.manager.SessionRedisManager;
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
 * 用户session redis管理者
 * Created by asiamaster
 */
@Component
public class SessionRedisManagerImpl implements SessionRedisManager {

	@Autowired
	private ManageRedisUtil myRedisUtil;

	@Override
	public void setUserIdSessionIdKey(String userId, String sessionId) {
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + userId);
		userIdSessionIds.add(sessionId);
		userIdSessionIds.expire(SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
//		myRedisUtil.set(SessionConstants.USERID_SESSIONID_KEY + user.getId(), sessionId,
//				SessionConstants.SESSION_TIMEOUT);
	}

	@Override
	public List<String> getSessionIdsByUserId(String userId) {
//		return myRedisUtil.get(SessionConstants.USERID_SESSIONID_KEY + userId, String.class);
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + userId);
		List<String> sessionIds = Lists.newArrayList();
		while (true){
			String sessionId = userIdSessionIds.pop();
			if(sessionId == null){
				break;
			}
			sessionIds.add(sessionId);
		}
		return sessionIds;
	}

	@Override
	public List<String> getOnlineUserIds(){
		Set<String> keys = myRedisUtil.getRedisTemplate().keys(SessionConstants.USERID_SESSIONID_KEY + "*");
		if(keys == null || keys.isEmpty()){
			return Lists.newArrayList();
		}
		List<String> userIds = Lists.newArrayList();
		for(String key : keys){
			userIds.add(key.substring(SessionConstants.USERID_SESSIONID_KEY.length()));
		}
		return userIds;
	}

	@Override
	public List<String> getOnlineUserSessionIds(){
		Set<String> keys = myRedisUtil.getRedisTemplate().keys(SessionConstants.SESSIONID_USERID_KEY + "*");
		if(keys == null || keys.isEmpty()){
			return Lists.newArrayList();
		}
		List<String> sessionIds = Lists.newArrayList();
		for(String key : keys){
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
	public void clearUserIdSessionDataKeyBySessionId(String sessionId) {
		BoundSetOperations<String, String> userIdSessionIds = myRedisUtil.getRedisTemplate().boundSetOps(SessionConstants.USERID_SESSIONID_KEY + getUserIdBySessionId(sessionId));
		userIdSessionIds.remove(sessionId);
	}

	@Override
	public void addKickSessionKey(String oldSessionId) {
		myRedisUtil.set(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId, "",
				SessionConstants.COOKIE_TIMEOUT.longValue());
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
		myRedisUtil.set(SessionConstants.SESSIONID_USERID_KEY + sessionId, userId,
				SessionConstants.SESSION_TIMEOUT);
	}

	@Override
	public String getUserIdBySessionId(String sessionId) {
		return myRedisUtil.get(SessionConstants.SESSIONID_USERID_KEY + sessionId, String.class);
	}

	@Override
	public void clearSessionUserIdKey(String sessionId) {
		myRedisUtil.remove(SessionConstants.SESSIONID_USERID_KEY + sessionId);
	}

	// sessionId - userId 操作 - END
}
