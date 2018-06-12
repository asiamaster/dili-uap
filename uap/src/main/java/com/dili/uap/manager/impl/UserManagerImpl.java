package com.dili.uap.manager.impl;

import com.dili.uap.manager.UserManager;
import com.dili.uap.sdk.manager.SessionRedisManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
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
	private ManageRedisUtil myRedisUtil;
	@Autowired
	private SessionRedisManager sessionRedisManager;

	@Override
	public void clearSession(String sessionId) {
		LOG.debug("--- Clear User SessionData --- : SessionId - " + sessionId);
		this.myRedisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + sessionId);
		this.sessionRedisManager.clearUserIdSessionDataKeyBySessionId(sessionId);
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
			this.myRedisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + oldSessionId);
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
