package com.dili.uap.sdk.session;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.config.ManageConfig;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.service.AuthService;
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

	// 处理器
	private Object handler;
	// referer
	private String referer = "";

	// 请求地址
	private String uri;

	// 请求的全路径
	private String url;

	// 查询字符串
	private String queryString;

	private String accessToken;

	private String refreshToken;

	private ManageConfig config;

	private AuthService authService;

	private String domain;

	public PermissionContext(HttpServletRequest req, HttpServletResponse resp, Object handler, ManageConfig conf, String domain) {
		setReq(req);
		setConfig(conf);
		this.resp = resp;
		this.handler = handler;
		authService = SpringUtil.getBean(AuthService.class);
		this.domain = domain;
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
		String serverPath = SpringUtil.getProperty("project.serverPath");
		url = StringUtils.isBlank(serverPath) ? req.getRequestURL().toString() : serverPath + uri;
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

	public String getDomain() {
		return domain;
	}

	public String makePath(String s) {
		if (this.domain.endsWith("/")) {
			if (s.startsWith("/")) {
				s = s.substring(1);
			}
		} else {
			if (!s.startsWith("/")) {
				s = "/" + s;
			}
		}
		return this.domain + s;
	}

	/**
	 * 登录没有权限的处理
	 */
	public void nonPermission() {
		try {
			String requestType = req.getHeader("X-Requested-With");
			if (requestType == null) {
				sendRedirect("/error/noAccess.do");
				return;
			}
			String path = makePath("/error/noAccess.do");
			resp.setStatus(403);
			resp.addHeader("nonAccessable", path);
			resp.getWriter().write("nonAccessable");
			resp.flushBuffer();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return;
	}

	/**
	 * loginCheck的URL没有权限的处理
	 * @throws IOException
	 */
	public void noAccess() throws IOException {
		String requestType = req.getHeader("X-Requested-With");
		if (requestType == null) {
			sendRedirect("/error/noAccess.do");
			return;
		}
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().write(JSON.toJSONString(BaseOutput.failure("登录超时").setCode("401")));
		resp.flushBuffer();
	}

	/**
	 * 未登录(登录超时)的处理
	 * @throws IOException
	 */
	public void noLogin() throws IOException {
		String requestType = req.getHeader("X-Requested-With");
		if (requestType == null) {
			sendRedirect("/error/noLogin.do");
			return;
		}
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().write(JSON.toJSONString(BaseOutput.failure("用户未登录").setCode("401")));
		resp.flushBuffer();
	}

	/**
	 * 从URL参数、header和Cookie中获取Token
	 * 没有取到，返回null
	 * @return
	 */
	public String getAccessToken() {
		if (accessToken == null) {
			// 首先读取链接中的token
			accessToken = req.getParameter(SessionConstants.ACCESS_TOKEN_KEY);
			if (StringUtils.isBlank(accessToken)) {
				accessToken = req.getHeader(SessionConstants.ACCESS_TOKEN_KEY);
			}
			if (StringUtils.isNotBlank(accessToken)) {
				WebContent.setCookie(SessionConstants.ACCESS_TOKEN_KEY, accessToken);
			} else {
				accessToken = WebContent.getCookieVal(SessionConstants.ACCESS_TOKEN_KEY);
			}
		}
		return accessToken;
	}

	/**
	 * 从URL参数、header和Cookie中获取Token
	 * 没有取到，返回null
	 * @return
	 */
	public String getRefreshToken() {
		if (refreshToken == null) {
			// 首先读取链接中的token
			refreshToken = req.getParameter(SessionConstants.REFRESH_TOKEN_KEY);
			if (StringUtils.isBlank(refreshToken)) {
				refreshToken = req.getHeader(SessionConstants.REFRESH_TOKEN_KEY);
			}
			if (StringUtils.isNotBlank(refreshToken)) {
				WebContent.setCookie(SessionConstants.REFRESH_TOKEN_KEY, refreshToken);
			} else {
				refreshToken = WebContent.getCookieVal(SessionConstants.REFRESH_TOKEN_KEY);
			}
		}
		return refreshToken;
	}

	/**
	 * 从URL参数、header和Cookie中获取Token，获取用户信息
	 * @return
	 */
	public UserTicket getUserTicket() {
		String accessToken = getAccessToken();
		if (accessToken == null) {
			return null;
		}
		String refreshToken = getRefreshToken();
		if (refreshToken == null) {
			return null;
		}
		return authService.getUserTicket(accessToken, refreshToken);
	}

}
