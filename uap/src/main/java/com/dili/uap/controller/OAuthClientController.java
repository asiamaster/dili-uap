package com.dili.uap.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ss.exception.AppException;
import com.dili.uap.oauth.justauth.JustAuthController;
import com.dili.uap.sdk.util.WebContent;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * oauth跳转页控制器
 */
@Controller
@RequestMapping("/api/oauthclient")
public class OAuthClientController extends JustAuthController {
	// 跳转到首页
	public static final String INDEX_PATH = "oauth/index";
	//用户id和refreshToken的缓存
	public static final Map<String, String> USERID_REFRESHTOKEN_CACHE = new HashMap<>();

	/**
	 * OAuth登录成功后，由AS服务跳转到权限主页面
	 *
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/index.html")
	public String index(ModelMap modelMap, HttpServletRequest request) throws UnsupportedEncodingException {
		AuthUser authUser = (AuthUser) request.getAttribute("authUser");
		//如果是redirect，只能从cookie取AuthUser
		if(authUser == null) {
			String authUserStr = URLDecoder.decode(WebContent.getCookieVal("authUser"), "utf-8");
			if(authUserStr == null) {
				throw new AppException("无法从cookie获取到authUser");
			}
			AuthUser jsonAuthUser = JSON.parseObject(authUserStr, AuthUser.class);
			request.setAttribute("authUser", jsonAuthUser);
			USERID_REFRESHTOKEN_CACHE.put(jsonAuthUser.getUuid(), jsonAuthUser.getToken().getRefreshToken());
		}else{
			USERID_REFRESHTOKEN_CACHE.put(authUser.getUuid(), authUser.getToken().getRefreshToken());
		}
		return INDEX_PATH;
	}

	/**
	 * 子类实现，用于刷新token时
	 * 根据用户id获取refreshToken，如果用户不存在，需要抛出AuthException异常
	 * @param uuid
	 * @return
	 */
	protected String getRefreshTokenByUuid(String uuid){
		return USERID_REFRESHTOKEN_CACHE.get(uuid);
	}

	/**
	 * 子类实现(可选)
	 * 在调用refreshAuth方法后，(在数据库或缓存中)更新用户的refreshToken
	 * @param uuid
	 * @param authToken
	 */
	protected void updateRefreshToken(String uuid, AuthToken authToken){
		USERID_REFRESHTOKEN_CACHE.put(uuid, authToken.getRefreshToken());
		System.out.println("获取到accessToken:" + authToken.getAccessToken());
	}

}
