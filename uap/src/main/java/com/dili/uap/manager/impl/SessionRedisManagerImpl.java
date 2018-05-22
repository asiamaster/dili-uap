package com.dili.uap.manager.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.uap.domain.User;
import com.dili.uap.manager.SessionRedisManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户session redis管理者
 * Created by asiamaster
 */
@Component
public class SessionRedisManagerImpl implements SessionRedisManager {

	@Autowired
	private ManageRedisUtil myRedisUtil;

	@Override
	public void setUserIdSessionDataKey(User user, String session) {
		JSONObject map = new JSONObject();
		map.put("sessionId", session);
		map.put("user", JSON.toJSONString(user));
		myRedisUtil.set(SessionConstants.USERID_SESSION_KEY + user.getId(), map.toString(),
				SessionConstants.SESSIONID_USERID_TIMEOUT);
	}

	@Override
	public String getUserIdSessionDataKey(String userId) {
		return myRedisUtil.get(SessionConstants.USERID_SESSION_KEY + userId, String.class);
	}

	@Override
	public Boolean existUserIdSessionDataKey(String s) {
		return myRedisUtil.exists(SessionConstants.USERID_SESSION_KEY + s);
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
		myRedisUtil.set(SessionConstants.SESSION_USERID_KEY + sessionId, userId,
				SessionConstants.SESSIONID_USERID_TIMEOUT);
	}

	@Override
	public String getSessionUserIdKey(String sessionId) {
		return myRedisUtil.get(SessionConstants.SESSION_USERID_KEY + sessionId, String.class);
	}

	@Override
	public void clearSessionUserIdKey(String sessionId) {
		myRedisUtil.remove(SessionConstants.SESSION_USERID_KEY + sessionId);
	}

	@Override
	public void clearUserIdSessionDataKey(String userId) {
		myRedisUtil.remove(SessionConstants.USERID_SESSION_KEY + userId);
	}
	// sessionId - userId 操作 - END
}
