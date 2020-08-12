package com.dili.uap.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.WebUtil;

/**
 * 登录控制器
 * 
 * @author wangmi
 * @date 2018-5-22
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	// 跳转到登录页面
	public static final String INDEX_PATH = "login/index";
	// 跳转到登录页Controller
	public static final String REDIRECT_INDEX_PAGE = "redirect:/login/toLogin.html";

	/**
	 * 跳转到Login页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return INDEX_PATH;
	}

	/**
	 * 执行login请求，跳转到Main页面或者返回login页面
	 * 
	 * @param userName
	 * @param password
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLogin.action", method = { RequestMethod.GET, RequestMethod.POST })
	public String getLoginAction(@RequestParam("userName") String userName, @RequestParam("password") String password, ModelMap modelMap, HttpServletRequest request) {
		LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
		loginDto.setUserName(userName);
		loginDto.setPassword(password);
		return loginAction(loginDto, modelMap, request);
	}

	/**
	 * 执行login请求，跳转到Main页面或者返回login页面
	 * 
	 * @param loginDto
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@BusinessLogger(businessType = "login_management", content = "系统登录:${systemCode}", operationType = "login", systemCode = "UAP")
	@RequestMapping(value = "/login.action", method = { RequestMethod.GET, RequestMethod.POST })
	public String loginAction(LoginDto loginDto, ModelMap modelMap, HttpServletRequest request) {
		// 设置登录后需要返回的上一页URL,用于记录登录地址到Cookie
		loginDto.setLoginPath(WebUtil.fetchReferer(request));
		// 设置ip和hosts,用于记录登录日志
		loginDto.setIp(WebUtil.getRemoteIP(request));
		loginDto.setHost(request.getRemoteHost());
		// 如果有登录用户名和密码，并且登录系统是UAP，则跳到平台首页，并加载登录数据
		// 如果没有登录用户名和密码，并且登录系统是UAP，则跳到平台首页，不加载登录数据
		// 如果没有登录用户名和密码，并且登录系统不是UAP，则跳到系统首页，不加载登录数据
		// 如果用户名和密码不为空，则需要加载登录数据
		if (StringUtils.isNotBlank(loginDto.getUserName()) && StringUtils.isNotBlank(loginDto.getPassword())) {
			BaseOutput<Boolean> output = loginService.loginAndTag(loginDto);
			// 登录失败后跳到登录页
			if (!output.isSuccess()) {
				// 登录失败放入登录结果信息
				modelMap.put("msg", output.getMessage());
				return INDEX_PATH;
			}
		}
		// 跳转到平台首页，参数带上系统编码
		return IndexController.REDIRECT_INDEX_PAGE;
	}

	/**
	 * 根据sessionId登录
	 * 
	 * @param sessionId
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@BusinessLogger(businessType = "login_management", content = "系统登录:${system}，sessionId:${sessionId}", operationType = "login", systemCode = "UAP")
	@RequestMapping(value = "/loginBySession.action", method = { RequestMethod.GET, RequestMethod.POST })
	public String loginBySessionAction(String sessionId, ModelMap modelMap, HttpServletRequest request) {
		// 如果有登录用户名和密码，并且登录系统是UAP，则跳到平台首页，并加载登录数据
		// 如果没有登录用户名和密码，并且登录系统是UAP，则跳到平台首页，不加载登录数据
		// 如果没有登录用户名和密码，并且登录系统不是UAP，则跳到系统首页，不加载登录数据
		// 如果用户名和密码不为空，则需要加载登录数据
		if (StringUtils.isNotBlank(sessionId)) {
			BaseOutput<Boolean> output = loginService.loginBySession(sessionId);
			// 登录失败后跳到登录页
			if (!output.isSuccess()) {
				// 登录失败放入登录结果信息
				modelMap.put("msg", output.getMessage());
				return INDEX_PATH;
			} else {
				LoggerContext.put("sessionId", sessionId);
			}
		}
		// 跳转到平台首页，参数带上系统编码
		return IndexController.REDIRECT_INDEX_PAGE;
	}

	/**
	 * 执行logout请求，跳转login页面或者弹出错误
	 * 
	 * @param systemCode
	 * @param userId
	 * @param request
	 * @return
	 */
	@BusinessLogger(businessType = "login_management", content = "${msg}", operationType = "logout")
	@RequestMapping(value = "/logout.action", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseOutput logoutAction(String systemCode, @RequestParam(required = false) Long userId, HttpServletRequest request) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			this.userService.logout(WebContent.getPC().getSessionId());
			// 没有userId则取userTicket
			userId = userId == null ? userTicket == null ? null : userTicket.getId() : userId;
			// 如果有用户id，则记录登出日志
			if (userId != null) {
				LoggerContext.put(LoggerConstant.LOG_SYSTEM_CODE_KEY, systemCode);
				loginService.logLogout(userTicket);
			}
		}
		try {
			WebContent.setCookie(SessionConstants.COOKIE_SESSION_ID, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseOutput.success();
	}

	/**
	 * 跳转到登录页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/toLogin.html", method = RequestMethod.GET)
	public String toLogin(ModelMap modelMap) {
		return "toLogin";
	}

}
