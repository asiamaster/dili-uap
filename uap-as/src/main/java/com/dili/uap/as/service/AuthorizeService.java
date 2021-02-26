package com.dili.uap.as.service;

import com.alibaba.fastjson.JSON;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.dto.JwtToken;
import com.dili.uap.sdk.service.redis.ManageRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * oauth授权服务
 * @author: WM
 * @time: 2021/2/26 14:53
 */
@Service
public class AuthorizeService {

    @Autowired
    ManageRedisUtil manageRedisUtil;

    private static final String AUTH_CODE_KEY_PREFIX = "authCode:";
    private static final String AUTH_CODE_TOKENS_KEY_PREFIX = "authCode:token:";

    /**
     * 保存授权码
     * @param authCode
     */
    public void saveAuthCode(String username, String authCode, String accessToken, String refreshToken){
        manageRedisUtil.set(AUTH_CODE_KEY_PREFIX+username, authCode, 10L, TimeUnit.MINUTES);
        JwtToken jwtToken = DTOUtils.newInstance(JwtToken.class);
        jwtToken.setAccessToken(accessToken);
        jwtToken.setRefreshToken(refreshToken);
        manageRedisUtil.set(AUTH_CODE_TOKENS_KEY_PREFIX+authCode, JSON.toJSONString(jwtToken), 10L, TimeUnit.MINUTES);
    }

    /**
     * 获取授权码
     * @param username
     * @return
     */
    public String getAuthCode(String username){
        return manageRedisUtil.get(AUTH_CODE_KEY_PREFIX + username, String.class);
    }

    /**
     * 根据授权码获取令牌
     * @param authCode
     * @return
     */
    public JwtToken getJwtTokenByCode(String authCode){
        String jwtTokenStr = (String)manageRedisUtil.get(AUTH_CODE_TOKENS_KEY_PREFIX + authCode);
        return JSON.parseObject(jwtTokenStr, JwtToken.class);
    }
}
