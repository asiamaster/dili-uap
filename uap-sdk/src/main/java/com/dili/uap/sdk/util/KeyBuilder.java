package com.dili.uap.sdk.util;

import com.dili.uap.sdk.constant.SessionConstants;

/**
 * 构建用户登录保存到Redis的Key
 * @author: WM
 * @time: 2021/1/4 17:45
 */
public class KeyBuilder {
    /**
     * 构建 RefreshTokenKey->用户信息 KEY
     * @param refreshToken
     * @return
     */
    public static String buildRefreshTokenKey(String refreshToken){
        return new StringBuilder().append(SessionConstants.REFRESH_TOKEN_KEY_PREFIX).append(refreshToken).toString();
    }

    /**
     * 构建 用户id->refreshTokens KEY
     * @param userId
     * @param systemType
     * @return
     */
    public static String buildUserIdRefreshTokenKey(String userId, Integer systemType){
        return new StringBuilder()
                .append(SessionConstants.USERID_REFRESH_TOKEN_KEY)
                .append(systemType).append(":")
                .append(userId).toString();
    }

    /**
     * 构建 用户系统 KEY
     * @param userId
     * @param systemType
     * @return
     */
    public static String buildUserSystemKey(String userId, Integer systemType){
        return buildKey(userId, systemType, SessionConstants.USER_SYSTEM_KEY);
    }

    /**
     * 构建 用户菜单 KEY
     * @param userId
     * @param systemType
     * @return
     */
    public static String buildUserMenuUrlKey(String userId, Integer systemType){
        return buildKey(userId, systemType, SessionConstants.USER_MENU_URL_KEY);
    }

    /**
     * 构建 用户资源 KEY
     * @param userId
     * @param systemType
     * @return
     */
    public static String buildUserResourceCodeKey(String userId, Integer systemType){
        return buildKey(userId, systemType, SessionConstants.USER_RESOURCE_CODE_KEY);
    }

    /**
     * 构建 用户数据权限 KEY
     * @param userId
     * @param systemType
     * @return
     */
    public static String buildUserDataAuthKey(String userId, Integer systemType){
        return buildKey(userId, systemType, SessionConstants.USER_DATA_AUTH_KEY);
    }

    /**
     * 通用构建Key方法
     * @param userId
     * @param systemType
     * @param prefix
     * @return
     */
    private static String buildKey(String userId, Integer systemType, String prefix){
        return new StringBuilder().append(prefix).append(systemType).append(":").append(userId).toString();
    }

}
