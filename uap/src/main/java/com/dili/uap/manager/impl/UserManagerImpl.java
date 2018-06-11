package com.dili.uap.manager.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.uap.dao.RoleMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.manager.MenuManager;
import com.dili.uap.manager.ResourceManager;
import com.dili.uap.manager.SessionRedisManager;
import com.dili.uap.manager.UserManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class UserManagerImpl implements UserManager {
	private final static Logger LOG = LoggerFactory.getLogger(UserManagerImpl.class);

	@Autowired
	private ManageRedisUtil myRedisUtil;
	@Autowired
	private SessionRedisManager sessionManager;

	@Override
	public void clearSession(String sessionId) {
		LOG.debug("--- Clear User SessionData --- : SessionId - " + sessionId);
		this.myRedisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + sessionId);
		this.sessionManager.clearUserIdSessionDataKey(this.sessionManager.getSessionUserIdKey(sessionId));
		this.sessionManager.clearSessionUserIdKey(sessionId);
	}

	@Override
	public String clearUserSession(Long userId) {
		String oldSessionId = this.sessionManager.getUserIdSessionDataKey(userId.toString());
		if(StringUtils.isBlank(oldSessionId)){
			return oldSessionId;
		}
		LOG.debug("--- Clear User SessionData --- : SessionId - " + oldSessionId);
		this.myRedisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + oldSessionId);
		this.sessionManager.clearUserIdSessionDataKey(userId.toString());
		this.sessionManager.clearSessionUserIdKey(oldSessionId);
		return oldSessionId;
	}

	@Override
	public Set<String> getOnlineUserIds() {
		return myRedisUtil.getRedisTemplate().keys(SessionConstants.USERID_SESSION_KEY + "*");
	}

}
