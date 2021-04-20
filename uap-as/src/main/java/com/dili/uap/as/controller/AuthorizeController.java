package com.dili.uap.as.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.as.domain.OAuthClient;
import com.dili.uap.as.domain.OauthClientPrivilege;
import com.dili.uap.as.domain.dto.LoginDto;
import com.dili.uap.as.domain.dto.LoginResult;
import com.dili.uap.as.service.AuthorizeService;
import com.dili.uap.as.service.LoginService;
import com.dili.uap.as.service.OAuthClientService;
import com.dili.uap.as.service.OauthClientPrivilegeService;
import com.dili.uap.sdk.domain.dto.JwtToken;
import com.dili.uap.sdk.domain.dto.UserToken;
import com.dili.uap.sdk.exception.UapLoginException;
import com.dili.uap.sdk.service.AuthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.message.types.TokenType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 遵循rfc6749协议
 * refer: http://www.rfcreader.com/#rfc6749_line163
 * @author: Wang Mi
 * @description: oauth授权控制器
 *
 * 代码的作用:
 * 1、首先通过如 http://localhost:8081/api/oauth-server/authorize?response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A9080%2Foauth-client%2FcallbackCode&client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee
 * 2、该控制器首先检查clientId是否正确；如果错误将返回相应的错误信息
 * 3、然后判断用户是否登录了，如果没有登录首先到登录页面登录
 * 4、登录成功后生成相应的auth code即授权码，然后重定向到客户端地址，如http://localhost:9080/oauth-client/oauth2-login?code=52b1832f5dff68122f4f00ae995da0ed；在重定向到的地址中会带上code参数（授权码），接着客户端可以根据授权码去换取access token。
 */
@Controller
@RequestMapping("/api/oauth-server")
public class AuthorizeController {

    @Autowired
    private OAuthClientService oAuthClientService;
    @Autowired
    private AuthorizeService authorizeService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private AuthService authService;
    @Autowired
    private OauthClientPrivilegeService oauthClientPrivilegeService;

    /**
     * 跳转到Login页面
     * http://as.diligrp.com:8396/api/oauth-server/authorize?response_type=code&client_id=hszx&state=uuid&redirect_uri=https://www.baidu.com
     * # Authorization Request
     * 客户端通过使用“application/x-www-form-urlencoding”格式向授权端点URI的查询组件添加以下参数来构造请求URI
     * - response_type：必须的。值必须是"code"。
     * - client_id：必须的。客户端标识符。
     * - redirect_uri：可选的。
     * - scope：可选的。请求访问的范围。
     * - state：推荐的。一个不透明的值用于维护请求和回调之间的状态。授权服务器在将用户代理重定向会客户端的时候会带上该参数。
     *
     * # Authorization Response
     * 如果资源所有者授权访问请求，授权服务器发出授权代码并通过使用“application/x-www-form- urlencoding”格式向重定向URI的查询组件添加以下参数，将其给客户端。
     * - code：必须的。授权服务器生成的授权码。授权代码必须在发布后不久过期，以减少泄漏的风险。建议最大授权代码生命期为10分钟。客户端不得多次使用授权代码。如果授权代码不止一次使用，授权服务器必须拒绝请求，并在可能的情况下撤销先前基于该授权代码发布的所有令牌。授权代码是绑定到客户端标识符和重定向URI上的。
     * - state：如果之前客户端授权请求中带的有"state"参数，则响应的时候也会带上该参数。
     * @param request
     * @return
     */
    @GetMapping(value = "/authorize")
    public Object index(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            if(StringUtils.isEmpty(oauthRequest.getRedirectURI()) || StringUtils.isEmpty(oauthRequest.getClientId()) || StringUtils.isEmpty(oauthRequest.getResponseType())){
                return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, "参数验证失败");
            }
            //根据传入的clientId 判断 客户端是否存在
            OAuthClient oAuthClient = oAuthClientService.getByCode(oauthRequest.getClientId());
            if (null == oAuthClient) {
                //生成错误信息,告知客户端不存在
               return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_CLIENT, "客户端验证失败，如错误的client_id/client_secret");
            }
            request.setAttribute("clientName", oAuthClient.getName());
            request.setAttribute(OAuth.OAUTH_REDIRECT_URI, oauthRequest.getRedirectURI());
            request.setAttribute(OAuth.OAUTH_CLIENT_ID, oauthRequest.getClientId());
            request.setAttribute(OAuth.OAUTH_RESPONSE_TYPE, oauthRequest.getResponseType());
            request.setAttribute(OAuth.OAUTH_STATE, oauthRequest.getState());
            //转到授权登录页面
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
     * @param model
     * @param request
     * @param response
     * @return
     * @throws OAuthSystemException
     * @throws URISyntaxException
     */
    @PostMapping(value="/login")
    public Object authorize(Model model, HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, URISyntaxException, OAuthProblemException {
        try {
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            //根据传入的clientId 判断 客户端是否存在
            OAuthClient oAuthClient = oAuthClientService.getByCode(oauthRequest.getClientId());
            if (null == oAuthClient) {
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
            LoginResult loginResult = login(request, oauthRequest.getClientId());
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
            //根据用户选择的clientPrivileges，插入不存在的权限
            String[] clientPrivileges = request.getParameterValues("clientPrivilege");
            OauthClientPrivilege oauthClientPrivilege = DTOUtils.newInstance(OauthClientPrivilege.class);
            oauthClientPrivilege.setUserId(loginResult.getUser().getId());
            oauthClientPrivilege.setOauthClientId(oAuthClient.getId());
            //先删除该openid和用户的所有权限
            oauthClientPrivilegeService.deleteByExample(oauthClientPrivilege);
            for (String clientPrivilege : clientPrivileges) {
                oauthClientPrivilege.setPrivilege(Integer.parseInt(clientPrivilege));
                oauthClientPrivilegeService.insertSelective(oauthClientPrivilege);
                oauthClientPrivilege.setId(null);
            }
            //生成授权码
            String authorizationCode = null;
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if(ResponseType.CODE.toString().equals(responseType)) {
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oAuthIssuer.authorizationCode();
                //把授权码和对应的token放到缓存中，用于获取令牌
                authorizeService.saveAuthCode(loginResult.getUser().getUserName(), authorizationCode, loginResult.getAccessToken(), loginResult.getRefreshToken(), loginResult.getAccessTokenTimeout(), oauthRequest.getRedirectURI());
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
                return new ResponseEntity("告诉客户端没有传入redirectUri", HttpStatus.NOT_FOUND);
            }
            // 返回错误消息
            final OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        } catch (UapLoginException e){
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            request.setAttribute(OAuth.OAUTH_REDIRECT_URI, oauthRequest.getRedirectURI());
            request.setAttribute(OAuth.OAUTH_CLIENT_ID, oauthRequest.getClientId());
            request.setAttribute(OAuth.OAUTH_RESPONSE_TYPE, oauthRequest.getResponseType());
            request.setAttribute(OAuth.OAUTH_STATE, oauthRequest.getState());
            request.setAttribute("msg", e.getMessage());
            return "login/index";
        }
    }

    /**
     * 通过授权码获取令牌
     *
     * http://uap.diligrp.com:8081/api/oauth-server/token?grant_type=authorization_code&client_id=hszx&code=code&redirect_uri=https://www.baidu.com
     * # Access Token Request
     * header: Context-Type必须是application/x-www-form-urlencoded
     * grant_type：必须的。值必须是"authorization_code"或"refresh_token"
     * code：必须的。值是从授权服务器那里接收的授权码。
     * redirect_uri：如果在授权请求的时候包含"redirect_uri"参数，那么这里也需要包含"redirect_uri"参数。而且，这两处的"redirect_uri"必须完全相同。
     * client_id：如果客户端需要认证，那么必须带的该参数。
     * client_secret:客户端密钥
     *
     * # Access Token Response
     *  HTTP/1.1 200 OK
     *  Content-Type: application/json;charset=UTF-8
     *  Cache-Control: no-store
     *  Pragma: no-cache
     *  {
     *    "access_token":"2YotnFZFEjr1zCsicMWpAA",
     *    "token_type":"example",
     *    "expires_in":3600,
     *    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
     *    "example_parameter":"example_value"
     *  }
     * @param request
     * @return
     * @throws OAuthSystemException
     * @throws URISyntaxException
     */
    @PostMapping(value="/token")
    @ResponseBody
    public Object token(HttpServletRequest request) throws OAuthSystemException {
        OAuthTokenRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthTokenRequest(request);
            OAuthResponse response = null;
            //验证client code和密钥
            ResponseEntity responseEntity = validateTokenRequest(oauthRequest);
            if(null != responseEntity){
                return responseEntity;
            }
            //grantType为授权码
            if(GrantType.AUTHORIZATION_CODE.toString().equalsIgnoreCase(oauthRequest.getGrantType())){
                String authzCode = oauthRequest.getCode();
                JwtToken jwtToken = authorizeService.getJwtTokenByCode(authzCode);
                if(jwtToken == null){
                    return buildResponseEntity(HttpServletResponse.SC_UNAUTHORIZED, OAuthError.TokenResponse.INVALID_REQUEST, "访问令牌不存在或已过期，请重新验证");
                }
                if(!jwtToken.getRedirectUri().equals(oauthRequest.getRedirectURI())){
                    return buildResponseEntity(HttpServletResponse.SC_UNAUTHORIZED, OAuthError.TokenResponse.INVALID_REQUEST, "重定向URI验证失败");
                }
                // 生成OAuth响应
                response = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setTokenType(TokenType.BEARER.toString())
                        .setAccessToken(jwtToken.getAccessToken())
                        .setExpiresIn(jwtToken.getExpires().toString())
                        .setRefreshToken(jwtToken.getRefreshToken())
                        .buildJSONMessage();
            }else if(GrantType.REFRESH_TOKEN.toString().equalsIgnoreCase(oauthRequest.getGrantType())){//grantType为刷新token
                UserToken userToken = authService.refreshOAuthUserToken(oauthRequest.getRefreshToken());
                if(userToken == null){
                    return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, "refresh_token过期");
                }
                // 生成OAuth响应
                response = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setTokenType(TokenType.BEARER.toString())
                        .setAccessToken(userToken.getAccessToken())
                        .setExpiresIn(userToken.getExpires().toString())
                        .setRefreshToken(userToken.getRefreshToken())
                        .buildJSONMessage();
            }else{
                return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, "无效的grantType参数");
            }
            // some code
//            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
//            String accessToken = oauthIssuerImpl.accessToken();
//            String refreshToken = oauthIssuerImpl.refreshToken();
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch(OAuthProblemException ex) {
            return buildResponseEntity(HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, ex.getMessage());
        }
    }

    /**
     * 根据privilege获取OauthClientPrivilege
     * @param oauthClientPrivileges
     * @param privilege
     * @return
     */
    private OauthClientPrivilege getOauthClientPrivilege(List<OauthClientPrivilege> oauthClientPrivileges, Integer privilege){
        for (OauthClientPrivilege oauthClientPrivilege : oauthClientPrivileges) {
            if (oauthClientPrivilege.getPrivilege().equals(privilege)) {
                return oauthClientPrivilege;
            }
        }
        return null;
    }

    /**
     * 验证token请求参数
     * 包括: clientId、clientSecret、grantType、redirectURI和code不为空
     * 包括：oauthClient的code和密钥校验
     * @param oauthRequest
     * @return
     * @throws OAuthSystemException
     */
    private ResponseEntity validateTokenRequest(OAuthTokenRequest oauthRequest) throws OAuthSystemException {
        // 1.获取OAuth客户端id(这里取code)
        String clientId = oauthRequest.getClientId();
        String clientSecret = oauthRequest.getClientSecret();
        String grantType = oauthRequest.getGrantType();
        String redirectURI = oauthRequest.getRedirectURI();
        if(clientId == null || clientSecret == null || grantType == null){
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                    .setErrorDescription("无效的请求参数")
                    .buildJSONMessage();
            return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }
        if(GrantType.AUTHORIZATION_CODE.toString().equalsIgnoreCase(grantType) && (oauthRequest.getCode() == null || redirectURI == null)){
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                    .setErrorDescription("无效的请求参数:code")
                    .buildJSONMessage();
            return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }else if(GrantType.REFRESH_TOKEN.toString().equalsIgnoreCase(grantType) && oauthRequest.getRefreshToken() == null){
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                    .setErrorDescription("无效的请求参数:refresh_token")
                    .buildJSONMessage();
            return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }

        // 数据库校验客户端id是否正确
        OAuthClient oAuthClient = oAuthClientService.getByCode(clientId);
        if (null == oAuthClient) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                    .setErrorDescription("无效的客户端ID")
                    .buildJSONMessage();
            return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }
        // 检查客户端安全key是否正确
        if (!oAuthClient.getSecret().equals(clientSecret)) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                    .setErrorDescription("客户端安全key认证不通过")
                    .buildJSONMessage();
            return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }
        return null;
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
     * 用户授权登录
     * @param request
     * @return
     */
    private LoginResult login(HttpServletRequest request, String openId) {
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        try {
            LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
            loginDto.setUserName(username);
            loginDto.setPassword(password);
            loginDto.setOpenId(openId);
            BaseOutput<LoginResult> loginResultBaseOutput = loginService.oauthLogin(loginDto);
            if(!loginResultBaseOutput.isSuccess()){
                throw new UapLoginException(loginResultBaseOutput.getMessage());
            }
            return loginResultBaseOutput.getData();
        }catch(Exception e){
            throw new UapLoginException(e.getMessage());
        }
    }


}
