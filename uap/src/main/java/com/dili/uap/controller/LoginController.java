package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <B>Description</B>地利后台管理系统登录 <B>Copyright</B> Copyright (c) 2014 www.dili7
 * All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-4 上午11:09:00
 * @author Nick
 */
@Api("/login")
@Controller
@RequestMapping("/login")
public class LoginController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private LoginService loginService;

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
            @ApiImplicitParam(name = "action", paramType = "form", value = "用户信息", required = false, dataType = "string") })
    @RequestMapping(value = "/loginAction", method = { RequestMethod.GET, RequestMethod.POST })
    public String loginAction(@RequestParam("userName") String userName, @RequestParam("password") String password, ModelMap modelMap) {
//        String requestPath = WebUtil.fetchReferer(request);
		BaseOutput<Boolean> output = loginService.loginAndTag(userName, password);
		if (output.isSuccess()) {
			return IndexController.REDIRECT_INDEX_PAGE;
		}
		modelMap.put("msg", output.getResult());
        return INDEX_PATH;
    }

    @ApiOperation("执行logout请求，跳转login页面或者弹出错误")
    @RequestMapping(value = "/logoutAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody BaseOutput logoutAction(ModelMap modelMap) {
        String sessionId = WebContent.getPC().getSessionId();
//        this.userService.logout(sessionId);
//        try {
//            WebContent.setCookie(COOKIE_SESSION_ID, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return BaseOutput.success();
    }



}
