package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

import javax.annotation.Resource;
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
		loginDto.setLoginPath(WebUtil.fetchReferer(request));
		loginDto.setRemoteIP(WebUtil.getRemoteIP(request));
		BaseOutput<Boolean> output = loginService.loginAndTag(loginDto);
		if (output.isSuccess()) {
			return IndexController.REDIRECT_INDEX_PAGE;
		}
		modelMap.put("msg", output.getResult());
        return INDEX_PATH;
    }

    @ApiOperation("执行logout请求，跳转login页面或者弹出错误")
    @RequestMapping(value = "/logout.action", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody BaseOutput logoutAction(ModelMap modelMap) {
        String sessionId = WebContent.getPC().getSessionId();
        this.userService.logout(sessionId);
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
