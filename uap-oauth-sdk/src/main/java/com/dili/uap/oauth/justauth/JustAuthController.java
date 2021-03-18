package com.dili.uap.oauth.justauth;

import com.alibaba.fastjson.JSON;
import com.dili.uap.oauth.config.OauthConfig;
import com.dili.uap.oauth.domain.Response;
import com.dili.uap.oauth.exception.BusinessException;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import me.zhyd.oauth.utils.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * 实战演示如何使用JustAuth实现第三方登录
 * @author wangmi
 */
@Controller
@RequestMapping("/api/oauth")
public class JustAuthController {
    private static Pattern ip = Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
    @Autowired
    OauthConfig oauthConfig;
    /**
     * 获取授权链接并跳转到第三方授权页面
     * source只支持uap
     * http://uap.diligrp.com/api/oauth/render/uap
     * @param response response
     * @throws IOException response可能存在的异常
     */
    @RequestMapping(value = "/render/{source}", method = {RequestMethod.GET, RequestMethod.POST})
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     *
     * @param callback 第三方回调时的入参
     * @return 第三方平台的用户信息
     */
    @RequestMapping(value = "/callback/{source}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView callback(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request, HttpServletResponse servletResponse) {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (response.ok()) {
            if(oauthConfig.getIndexPath().trim().startsWith("redirect:")){
                //设置cookie，一天过期
                setCookie(servletResponse, "authUser", JSON.toJSONString(response.getData()),86400, getCookieDomain(request.getRequestURL().toString()) );
                return new ModelAndView(oauthConfig.getIndexPath());
            }else {
                request.setAttribute("authUser", response.getData());
                //跳转到内部controller继续处理
                return new ModelAndView(request.getContextPath() + oauthConfig.getIndexPath());
            }
        }
        String exceptionPath = oauthConfig.getExceptionPath();
        if (exceptionPath.trim().startsWith("redirect:")) {
            return new ModelAndView(UrlBuilder.fromBaseUrl(exceptionPath).queryParam("exMsg", response.getMsg()).build());
        }
        request.setAttribute("exception", new BusinessException(String.valueOf(response.getCode()), response.getMsg()));
        request.setAttribute("exMsg", response.getMsg());
        return new ModelAndView(exceptionPath);
    }

    @RequestMapping("/refresh/{source}/{uuid}")
    @ResponseBody
    public Object refreshAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) {
        AuthRequest authRequest = getAuthRequest(source.toLowerCase());
        AuthToken authToken = new AuthToken();
        authToken.setRefreshToken(getRefreshTokenByUuid(uuid));
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.refresh(authToken);
            if (response.ok()) {
                updateRefreshToken(uuid, response.getData());
                return Response.success("用户 [" + uuid + "] 的 access token 已刷新", response.getData());
            }
            return Response.error("用户 [" + uuid + "] 的 access token 刷新失败！" + response.getMsg());
        } catch (AuthException e) {
            return Response.error(e.getErrorMsg());
        }
    }

    /**
     * 子类实现，用于刷新token时
     * 根据用户id获取refreshToken，如果用户不存在，需要抛出AuthException异常
     * @param uuid
     * @return
     */
    protected String getRefreshTokenByUuid(String uuid){
        return "f7a6ec11-507a-4869-8f35-a6ddf4695ee8";
    }

    /**
     * 子类实现(可选)
     * (在缓存中)更新用户的refreshToken
     * @param uuid
     * @param authToken
     */
    protected void updateRefreshToken(String uuid, AuthToken authToken){

    }

    /**
     * 设置COOKIE
     *
     * @param key
     * @param val
     */
    private void setCookie(HttpServletResponse resp, String key, String val, Integer maxAge, String domain) {
        Cookie cookie = null;
        try {
            val = val == null ? null : URLEncoder.encode(val, "utf-8");
            cookie = new Cookie(key, val);
            cookie.setDomain(domain);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            resp.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("cookie写入失败");
        }
    }

    private String getCookieDomain(String uri) {
        try {
            return getCookieDomain(new URI(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI转换出错!");
        }
    }

    private String getCookieDomain(URI uri) {
        String host = uri.getHost();
        if (ip.matcher(host).find()) {
            return host;
        }
        switch (host) {
            case "127.0.0.1":
            case "localhost":
                return host;
            default:
                break;
        }
        String[] strs = host.split("\\.");
        return strs[strs.length - 2] + "." + strs[strs.length - 1];
    }

    /**
     * 获取授权Request
     *
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        source = source.toLowerCase();
        switch (source) {
            case "uap":
                authRequest = new AuthUapRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getProjectPath()+"/api/oauth/callback/uap")
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }

}