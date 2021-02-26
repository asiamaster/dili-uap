package com.dili.uap.as.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.as.domain.dto.LoginDto;
import com.dili.uap.as.domain.dto.LoginResult;
import com.dili.uap.as.service.AuthorizeService;
import com.dili.uap.as.service.ClientService;
import com.dili.uap.as.service.LoginService;
import com.dili.uap.sdk.exception.UapLoginException;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author: wangsaichao
 * @date: 2018/5/27
 * @description: 授权控制器
 *
 * 代码的作用:
 * 1、首先通过如 http://localhost:9090/oauth-server/authorize?response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A9080%2Foauth-client%2FcallbackCode&client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee
 * 2、该控制器首先检查clientId是否正确；如果错误将返回相应的错误信息
 * 3、然后判断用户是否登录了，如果没有登录首先到登录页面登录
 * 4、登录成功后生成相应的auth code即授权码，然后重定向到客户端地址，如http://localhost:9080/oauth-client/oauth2-login?code=52b1832f5dff68122f4f00ae995da0ed；在重定向到的地址中会带上code参数（授权码），接着客户端可以根据授权码去换取access token。
 */
@Controller
@RequestMapping("/api/oauth-server")
public class AuthorizeController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthorizeService authorizeService;
    @Autowired
    private LoginService loginService;

    /**
     * 跳转到Login页面
     * http://uap.diligrp.com:8081/api/oauth-server/login.html?response_type=code&client_id=hszx&redirect_uri=https://www.baidu.com
     * @param request
     * @return
     */
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public Object index(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            if(StringUtils.isEmpty(oauthRequest.getRedirectURI()) || StringUtils.isEmpty(oauthRequest.getClientId()) || StringUtils.isEmpty(oauthRequest.getResponseType())){
                return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, "参数验证失败");
            }
            //根据传入的clientId 判断 客户端是否存在
            if (null == clientService.getByCode(oauthRequest.getClientId())) {
                //生成错误信息,告知客户端不存在
               return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_CLIENT, "客户端验证失败，如错误的client_id/client_secret");
            }
            request.setAttribute("redirectUri", oauthRequest.getRedirectURI());
            request.setAttribute("clientId", oauthRequest.getClientId());
            request.setAttribute("responseType", oauthRequest.getResponseType());
            return "login/index";
        } catch ( OAuthProblemException e) {
            //出错处理
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                //告诉客户端没有传入redirectUri直接报错
                return new ResponseEntity(
                        "OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
            }
            //返回错误消息（如?error=）
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    /**
     * 授权登录
     * http://uap.diligrp.com:8081/api/oauth-server/authorize?response_type=code&client_id=hszx&redirect_uri=https://www.baidu.com
     * @param model
     * @param request
     * @param response
     * @return
     * @throws OAuthSystemException
     * @throws URISyntaxException
     */
    @RequestMapping(value="/authorize", method = { RequestMethod.GET, RequestMethod.POST })
    public Object authorize(Model model, HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, URISyntaxException, OAuthProblemException {
        try {
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            //根据传入的clientId 判断 客户端是否存在
            if (null == clientService.getByCode(oauthRequest.getClientId())) {
                //生成错误信息,告知客户端不存在
                OAuthResponse oauthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription("客户端验证失败，如错误的client_id/client_secret")
                        .buildJSONMessage();
                return new ResponseEntity(
                        oauthResponse.getBody(), HttpStatus.valueOf(oauthResponse.getResponseStatus()));
            }
            //登录失败会抛出UapLoginException，在下面catch块处理
            LoginResult loginResult = login(request);
            if(loginResult == null){
                //生成错误信息,告知客户端不存在
                OAuthResponse oauthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.INVALID_GRANT)
                        .setErrorDescription("登录失败,用户名或密码错误")
                        .buildJSONMessage();
                return new ResponseEntity(
                        oauthResponse.getBody(), HttpStatus.valueOf(oauthResponse.getResponseStatus()));
            }
            //生成授权码
            String authorizationCode = null;
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if(responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oAuthIssuer.authorizationCode();
                //把授权码放到缓存中
                authorizeService.saveAuthCode(loginResult.getUser().getUserName(), authorizationCode, loginResult.getAccessToken(), loginResult.getRefreshToken());
            }
            // 进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            // 设置授权码
            builder.setCode(authorizationCode);
            // 根据客户端重定向地址
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            // 构建响应
            final OAuthResponse oAuthResponse = builder.location(redirectURI).buildQueryMessage();
            // 根据OAuthResponse 返回 ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        } catch (OAuthProblemException e) {
            // 出错处理
            String redirectUri = e.getRedirectUri();
            if(OAuthUtils.isEmpty(redirectUri)) {
                // 告诉客户端没有传入redirectUri直接报错
                return new ResponseEntity("告诉客户端没有传入redirectUri直接报错！", HttpStatus.NOT_FOUND);
            }
            // 返回错误消息
            final OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        } catch (UapLoginException e){
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            request.setAttribute("redirectUri", oauthRequest.getRedirectURI());
            request.setAttribute("clientId", oauthRequest.getClientId());
            request.setAttribute("responseType", oauthRequest.getResponseType());
            return "login/index";
        }
    }

    /**
     * 构建错误响应体
     * @param code
     * @param error
     * @param desc
     * @return
     * @throws OAuthSystemException
     */
    private ResponseEntity buildResponseEntity(int code, String error, String desc) throws OAuthSystemException {
        //生成错误信息,告知客户端不存在
        OAuthResponse oauthResponse = OAuthASResponse
                .errorResponse(code)
                .setError(error)
                .setErrorDescription(desc)
                .buildJSONMessage();
        return new ResponseEntity(
                oauthResponse.getBody(), HttpStatus.valueOf(oauthResponse.getResponseStatus()));
    }

    /**
     * 用户登录
     * @param request
     * @return
     */
    private LoginResult login(HttpServletRequest request) {
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        try {
            LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
            loginDto.setUserName(username);
            loginDto.setPassword(password);
            BaseOutput<LoginResult> loginResultBaseOutput = loginService.loginWeb(loginDto);
            if(!loginResultBaseOutput.isSuccess()){
                throw new UapLoginException(loginResultBaseOutput.getMessage());
            }
            return loginResultBaseOutput.getData();
        }catch(Exception e){
            throw new UapLoginException(e.getMessage());
        }
    }


}
