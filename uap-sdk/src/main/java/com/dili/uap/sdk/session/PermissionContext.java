package com.dili.uap.sdk.session;


import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.DataAuthRedis;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.redis.UserUrlRedis;
import com.dili.uap.sdk.util.WebContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限上下文
 */
public class PermissionContext {
    private static final Logger log = LoggerFactory.getLogger(PermissionContext.class);
    private HttpServletRequest req;
    private HttpServletResponse resp;

    //处理器
    private Object handler;
    //referer
    private String referer = "";

    //请求地址
    private String uri;

    //请求的全路径
    private String url;

    //查询字符串
    private String queryString;

    private String sessionId;

    private Long userId;

    private ManageConfig config;

    private UserRedis userRedis;

    private UserUrlRedis userResRedis;

    private DataAuthRedis dataAuthRedis;

    public PermissionContext(HttpServletRequest req, HttpServletResponse resp, Object handler, ManageConfig conf) {
        setReq(req);
        setConfig(conf);
        this.resp = resp;
        this.handler = handler;
        userRedis = SpringUtil.getBean(UserRedis.class);
        userResRedis = SpringUtil.getBean(UserUrlRedis.class);
        dataAuthRedis = SpringUtil.getBean(DataAuthRedis.class);
    }

    public String getReferer() {
        return referer;
    }

    public String getUri() {
        return uri;
    }

    public HttpServletRequest getReq() {
        return req;
    }

    public void setReq(HttpServletRequest req) {
        this.req = req;
        referer = req.getHeader("referer");
        uri = req.getRequestURI();
        url = req.getRequestURL().toString();
        queryString = req.getQueryString();
    }

    public HttpServletResponse getResp() {
        return resp;
    }

    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }


    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ManageConfig getConfig() {
        return config;
    }

    public void setConfig(ManageConfig config) {
        this.config = config;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void sendRedirect(String s) throws IOException {
        resp.sendRedirect(makePath(s));
    }

    public String makePath(String s) {
        if (config.getDomain().endsWith("/")) {
            if (s.startsWith("/")) {
                s = s.substring(1);
            }
        } else {
            if (!s.startsWith("/")) {
                s = "/" + s;
            }
        }
        return config.getDomain() + s;
    }


    /**
     * 没有权限
     */
    public void nonPermission() {
        try {
            String requestType = req.getHeader("X-Requested-With");
            if (requestType == null) {
                sendRedirect("/noAccess.do");
                return;
            }
            String path = makePath("/noAccess.do");
            resp.setStatus(403);
            resp.addHeader("nonAccessable", path);
            resp.getWriter().write("nonAccessable");
            resp.flushBuffer();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return;
    }


    public void noAccess() throws IOException {
        String requestType = req.getHeader("X-Requested-With");
        if (requestType == null) {
            sendRedirect("/error/noAccess.do");
            return;
        }
        String path = makePath("/error/noAccess.do");
        resp.addHeader("noAccess", path);
        resp.setStatus(401);
        resp.getWriter().write("noAccess");
        resp.flushBuffer();
    }

    public String getSessionId() {
        if (sessionId == null) {
            //首先读取链接中的session
            sessionId = req.getParameter(SessionConstants.SESSION_ID);
            if(StringUtils.isBlank(sessionId)) {
                sessionId = req.getHeader(SessionConstants.SESSION_ID);
            }
            if (StringUtils.isNotBlank(sessionId)) {
                WebContent.setCookie(SessionConstants.SESSION_ID, sessionId);
            } else {
                sessionId = WebContent.getCookieVal(SessionConstants.SESSION_ID);
            }
        }
        return sessionId;
    }

    public UserTicket getUser(){
        if (getSessionId() == null) {
            return null;
        }
        return getUserRedis().getUser(getSessionId());
    }

    public UserTicket getAuthorizer(){
        String authKey = req.getParameter(SessionConstants.AUTH_KEY);
        if (StringUtils.isBlank(authKey)) {
            authKey = req.getHeader(SessionConstants.AUTH_KEY);
            if(StringUtils.isBlank(authKey)) {
                return null;
            }
        }
        return userRedis.getUser(authKey);
    }

    public Long getUserId(){
        if (getSessionId() == null) {
            return null;
        }
        if (userId ==  null) {
            userId = userRedis.getSessionUserId(getSessionId());
        }
        return userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserRedis getUserRedis() {
        return userRedis;
    }

    public UserUrlRedis getUserResRedis() {
        return userResRedis;
    }

    public DataAuthRedis getDataAuthRedis() {
        return dataAuthRedis;
    }
}
