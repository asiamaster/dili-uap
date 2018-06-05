package com.dili.uap.controller;

import com.dili.uap.constants.UapConstants;
import com.dili.uap.sdk.domain.System;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserSystemRedis;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api("/index")
@Controller
@RequestMapping("/index")
public class IndexController {

	//跳转到首页
	public static final String INDEX_PATH = "index/index";
	//跳转到平台首页
	public static final String PLATFORM_PATH = "index/platform";
	//跳转到首页Controller
	public static final String REDIRECT_INDEX_PAGE = "redirect:/index/index.html";

	public static final String USERDETAIL_PATH = "index/userDetail";
	public static final String CHANGEPWD_PATH = "index/changePwd";

	@Autowired
	private UserSystemRedis userSystemRedis;

	@ApiOperation("跳转到权限主页面")
	@RequestMapping(value = "/index.html", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap modelMap, HttpServletRequest request) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			modelMap.put("userid", userTicket.getId());
			modelMap.put("username", userTicket.getRealName());
			String systemCode = request.getParameter("systemCode") == null ? UapConstants.SYSTEM_CODE : request.getParameter("systemCode");
			modelMap.put("systemCode", systemCode);
			return INDEX_PATH;
		} else {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
	}


	@ApiOperation("跳转到平台页面")
	@RequestMapping(value = "/platform.html", method = RequestMethod.GET)
	public String platform(ModelMap modelMap) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			List<System> systems = userSystemRedis.getRedisUserSystems(userTicket.getId());
			modelMap.put("systems", systems);
			modelMap.put("userid", userTicket.getId());
			modelMap.put("username", userTicket.getRealName());
			modelMap.put("systemCode", UapConstants.SYSTEM_CODE);
			return PLATFORM_PATH;
		} else {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
	}


	@ApiOperation("跳转到个人信息页面")
	@RequestMapping(value = "/userDetail.html", method = RequestMethod.GET)
	public String userDetail(ModelMap modelMap) {
		return USERDETAIL_PATH;
	}

	@ApiOperation("跳转到修改密码页面")
	@RequestMapping(value = "/changePwd.html", method = RequestMethod.GET)
	public String changePwd(ModelMap modelMap) {
		return CHANGEPWD_PATH;
	}

}
