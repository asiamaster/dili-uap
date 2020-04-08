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


    public Long getSessionTimeout() {
        return SESSION_TIMEOUT;
    }

    @Value("${uap.sessionTimeout:1800}")
    public void setSessionTimeout(Long sessionTimeout) {
        SESSION_TIMEOUT = sessionTimeout;
    }

}