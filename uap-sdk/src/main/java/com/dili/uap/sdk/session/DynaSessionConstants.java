package com.dili.uap.sdk.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class DynaSessionConstants {

	// sessionId - SessionData的Redis 过期时间(秒)
	// sessionId - userId和UserIdSessionData的Redis 过期时间(秒)
	// 默认为30分钟
	private Long SESSION_TIMEOUT;

	// sessionId - SessionData的Redis 过期时间(秒)
	// sessionId - userId和UserIdSessionData的Redis 过期时间(秒)
	// 默认为30天
	private Long TOKEN_TIMEOUT;
	
	private String UAP_CONTEXT_PATH;

	public Long getSessionTimeout() {
		return SESSION_TIMEOUT;
	}

	@Value("${uap.sessionTimeout:1800}")
	public void setSessionTimeout(Long sessionTimeout) {
		SESSION_TIMEOUT = sessionTimeout;
	}
	
	public Long getTokenTimeout() {
		return TOKEN_TIMEOUT;
	}

	@Value("${uap.tokenTimeout:2592000}")
	public void setTokenTimeout(Long tokenTimeout) {
		TOKEN_TIMEOUT = tokenTimeout;
	}

	public String getUapContextPath() {
		return UAP_CONTEXT_PATH;
	}

	@Value("${uap.contextPath}")
	public void setUapContextPath(String uapContextPath) {
		UAP_CONTEXT_PATH = uapContextPath;
	}
	
	

}