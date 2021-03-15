package com.dili.uap.oauth.justauth;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.support.HttpHeader;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.HttpUtils;
import me.zhyd.oauth.utils.UrlBuilder;

/**
 * @author: WM
 * @time: 2021/3/2 15:18
 */
public class AuthUapRequest extends AuthDefaultRequest{

    public AuthUapRequest(AuthConfig config) {
        super(config, AuthUapSource.UAP);
    }

    public AuthUapRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthUapSource.UAP, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        //由于doPostAuthorizationCode没有contentType=application/x-www-form-urlencoded，这里自行构建
//        String responseBody = doPostAuthorizationCode(authCallback.getCode());
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = new HttpUtils(config.getHttpConfig()).post(accessTokenUrl(authCallback.getCode()), null, httpHeader);
        JSONObject object = JSONObject.parseObject(responseBody);
        this.checkResponse(object);
        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .refreshToken(object.getString("refresh_token"))
                .idToken(object.getString("id_token"))
                .tokenType(object.getString("token_type"))
                .scope(object.getString("scope"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String responseBody = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(responseBody);
        if(!"200".equals(object.get("code"))){
            throw new AuthException(object.getString("message"));
        }
        JSONObject jsonObject = object.getJSONObject("data");
//        this.checkResponse(object);
        Integer gender = jsonObject.getInteger("gender");
        return AuthUser.builder()
                .uuid(jsonObject.getString("id"))
                .username(jsonObject.getString("userName"))
                .nickname(jsonObject.getString("realName"))
//                .avatar(object.getString("avatar_url"))
//                .blog(object.getString("web_url"))
                .company(jsonObject.getString("firmCode"))
                .location(jsonObject.getString("departmentId"))
                .email(jsonObject.getString("email"))
                .remark(jsonObject.getString("description"))
                .gender(gender == 0 ? AuthUserGender.MALE : gender.equals(1) ? AuthUserGender.FEMALE : AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     * @since 1.11.0
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
//                .queryParam("scope", "read_user+openid")
                .build();
    }

    @Override
    public AuthResponse refresh(AuthToken authToken) {
//        AuthResponse.builder()
//                .code(AuthResponseStatus.SUCCESS.getCode())
//                .data(getAuthToken(refreshTokenUrl(authToken.getRefreshToken())))
//                .build();
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", "application/x-www-form-urlencoded");
        String response = new HttpUtils(config.getHttpConfig()).post(refreshTokenUrl(authToken.getRefreshToken()), null, httpHeader);
        return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(getAuthToken(response)).build();
    }

    private AuthToken getAuthToken(String response) {
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        if (!accessTokenObject.containsKey("access_token") || accessTokenObject.containsKey("code")) {
            throw new AuthException(accessTokenObject.getString("error")+":"+accessTokenObject.getString("error_description"));
        }
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .expireIn(accessTokenObject.getInteger("expires_in"))
                .refreshToken(accessTokenObject.getString("refresh_token"))
                .build();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param refreshToken oauth返回的refreshtoken
     * @return 返回获取accessToken的url
     */
    @Override
    protected String refreshTokenUrl(String refreshToken) {
        return UrlBuilder.fromBaseUrl(source.refresh())
                .queryParam("client_id", config.getClientId())
                .queryParam("client_secret", config.getClientSecret())
                .queryParam("refresh_token", refreshToken)
                .queryParam("grant_type", "refresh_token")
                .build();
    }

    private void checkResponse(JSONObject object) {
        // oauth/token 验证异常
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
        // user 验证异常
        if (object.containsKey("message")) {
            throw new AuthException(object.getString("message"));
        }
    }
}
