package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录控制器
 * @author wangmi
 * @date 2018-5-22
 */
@Api("/login")
@Controller
@RequestMapping("/login")
public class LoginController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	//跳转到登录页面
	public static final String INDEX_PATH = "login/index";
	//跳转到登录页Controller
    public static final String REDIRECT_INDEX_PAGE = "redirect:/login/index.html";

	@ApiOperation("跳转到Login页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return INDEX_PATH;
	}

    @ApiOperation("执行login请求，跳转到Main页面或者返回login页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginDto", paramType = "form", value = "用户信息", required = false, dataType = "string") })
    @RequestMapping(value = "/login.action", method = { RequestMethod.GET, RequestMethod.POST })
    public String loginAction(LoginDto loginDto, ModelMap modelMap, HttpServletRequest request) {
		//设置登录后需要返回的上一页URL,用于记录登录地址到Cookie
		loginDto.setLoginPath(WebUtil.fetchReferer(request));
		//设置ip和hosts,用于记录登录日志
		loginDto.setIp(WebUtil.getRemoteIP(request));
		loginDto.setHost(request.getRemoteHost());
		//如果有登录用户名和密码，并且登录系统是UAP，则跳到平台首页，并加载登录数据
		//如果没有登录用户名和密码，并且登录系统是UAP，则跳到平台首页，不加载登录数据
		//如果没有登录用户名和密码，并且登录系统不是UAP，则跳到系统首页，不加载登录数据
		//如果用户名和密码不为空，则需要加载登录数据
		if (StringUtils.isNotBlank(loginDto.getUserName()) && StringUtils.isNotBlank(loginDto.getPassword())) {
			BaseOutput<Boolean> output = loginService.loginAndTag(loginDto);
			//登录失败后跳到登录页
			if (!output.isSuccess()) {
				//登录失败放入登录结果信息
				modelMap.put("msg", output.getResult());
				return INDEX_PATH;
			}
		}
		//跳转到平台首页，参数带上系统编码
		return IndexController.REDIRECT_INDEX_PAGE;
    }

    @ApiOperation("执行logout请求，跳转login页面或者弹出错误")
    @RequestMapping(value = "/logout.action", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody BaseOutput logoutAction(String systemCode, @RequestParam(required = false) Long userId, HttpServletRequest request) {
        this.userService.logout(WebContent.getPC().getSessionId());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		//没有userId则取userTicket
		userId = userId == null ? userTicket == null ? null : userTicket.getId() : userId;
		//如果有用户id，则记录登出日志
        if(userId != null) {
			LoginLog loginLog = DTOUtils.newDTO(LoginLog.class);
			//设置ip和hosts,用于记录登录日志
			loginLog.setIp(WebUtil.getRemoteIP(request));
			loginLog.setHost(request.getRemoteHost());
			loginLog.setUserId(userId);
			loginLog.setSystemCode(systemCode);
			loginService.logLogout(loginLog);
		}
        try {
            WebContent.setCookie(SessionConstants.COOKIE_SESSION_ID, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseOutput.success();
    }

	@ApiOperation("跳转到登录页面")
	@RequestMapping(value = "/toLogin.html", method = RequestMethod.GET)
	public String toLogin(ModelMap modelMap) {
		return "toLogin";
	}

}
