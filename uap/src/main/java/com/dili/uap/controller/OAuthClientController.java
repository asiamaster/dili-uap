package com.dili.uap.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ss.exception.AppException;
import com.dili.uap.sdk.util.WebContent;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * oauth跳转页控制器
 */
@Controller
@RequestMapping("/api/oauthclient")
public class OAuthClientController {
	// 跳转到首页
	public static final String INDEX_PATH = "oauth/index";


	/**
	 * 跳转到权限主页面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap modelMap, HttpServletRequest request) throws UnsupportedEncodingException {
		AuthUser authUser = (AuthUser) request.getAttribute("authUser");
		//如果是redirect，只能从cookie取AuthUser
		if(authUser == null) {
			String authUserStr = URLDecoder.decode(WebContent.getCookieVal("authUser"), "utf-8");
			if(authUserStr == null) {
				throw new AppException("无法从cookie获取到authUser");
			}
			request.setAttribute("authUser", JSON.parseObject(authUserStr, AuthUser.class));
		}
		return INDEX_PATH;
	}


}
