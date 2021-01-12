package com.dili.uap.sdk.service;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserToken;
import com.dili.uap.sdk.glossary.TokenStep;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.JwtService;
import com.dili.uap.sdk.util.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * JWT鉴权服务
 * @author: WM
 * @time: 2021/1/4 17:22
 */
@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Resource
    private JwtService jwtService;
    @Resource
    UserRedis userRedis;

    /**
     * 根据accessToken和refreshToken获取用户信息
     * 如果accessToken过期，则将redis超时推后dynaSessionConstants.getSessionTimeout()的时长<br/>
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserTicket getUserTicket(String accessToken, String refreshToken) {
        //选读取accessToken
        UserTicket userTicket = jwtService.getUserTicket(accessToken);
        if (null != userTicket) {
            return userTicket;
        }
        //accessToken超时，则根据refreshToken重新生成
        UserToken userToken = userRedis.applyAccessToken(refreshToken);
        //redis超时，无法获取到用户信息
        if(userToken == null){
            return null;
        }
        setCookieResponseAndDefer(userToken);
        return userToken.getUserTicket();
    }

    /**
     * 根据accessToken和refreshToken获取用户及token信息
     * 如果accessToken过期，则将redis超时推后dynaSessionConstants.getSessionTimeout()的时长<br/>
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserToken getUserToken(String accessToken, String refreshToken) {
        UserTicket userTicket = jwtService.getUserTicket(accessToken);
        if (null != userTicket) {
            UserToken userToken = DTOUtils.newInstance(UserToken.class);
            userToken.setRefreshToken(refreshToken);
            userToken.setAccessToken(accessToken);
            userToken.setUserTicket(userTicket);
            userToken.setTokenStep(TokenStep.ACCESS_TOKEN.getCode());
            return userToken;
        }
        //accessToken超时，则根据refreshToken重新生成
        UserToken userToken = userRedis.applyAccessToken(refreshToken);
        //redis超时，无法获取到用户信息
        if(userToken == null){
            return null;
        }
        setCookieResponseAndDefer(userToken);
        return userToken;
    }

    /**
     * 只有重新签发accessToken才设置cookie和response header，并且推后redis超时
     * @param userToken
     */
    private void setCookieResponseAndDefer(UserToken userToken){
        if(TokenStep.REFRESH_TOKEN.getCode().equals(userToken.getTokenStep())) {
            //变更web端cookie中的accessToken
            WebContent.setCookie(SessionConstants.ACCESS_TOKEN_KEY, userToken.getAccessToken());
            //在response header中通知C端和移动端accessToken变更
            WebContent.setResponseHeader(SessionConstants.ACCESS_TOKEN_KEY, userToken.getAccessToken());
            userRedis.defer(userToken.getRefreshToken(), userToken.getUserTicket());
        }
    }

}
