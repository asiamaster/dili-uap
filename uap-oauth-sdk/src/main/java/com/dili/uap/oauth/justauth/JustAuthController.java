package com.dili.uap.oauth.justauth;

import com.dili.uap.oauth.config.OauthConfig;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实战演示如何使用JustAuth实现第三方登录
 * @author wangmi
 */
@RestController
@RequestMapping("/api/oauth")
public class JustAuthController {

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
    public Object login(@PathVariable("source") String source, AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.login(callback);
    }

    /**
     * 获取授权Request
     *
     * @return AuthRequest
     */
    // ...
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "uap":
                authRequest = new AuthUapRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getUapContextPath()+"/api/oauth/callback/uap")
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