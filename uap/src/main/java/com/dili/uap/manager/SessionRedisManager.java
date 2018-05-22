package com.dili.uap.manager;


import com.dili.uap.domain.User;

/**
 * 用户和sessionId相关的缓存管理者
 * Created by root on 5/20/15.
 */
public interface SessionRedisManager {

    /**
     * 清空key为SessionConstants.SESSION_USERID_KEY + sessionId， 值为用户id的缓存
     * @param sessionId
     */
    void clearSessionUserIdKey(String sessionId);

    /**
     * 清空 key为SessionConstants.USERID_SESSION_KEY + userId, 值为:用户信息的Map, key为sessionId和user 的缓存
     * @param userId
     */
    void clearUserIdSessionDataKey(String userId);

    /**
     * 缓存 SessionConstants.SESSION_USERID_KEY + sessionId: userId
     * @param sessionId
     * @param userId
     */
    void setSessionUserIdKey(String sessionId, String userId);

    /**
     * 根据sessionId取用户id
     * @param sessionId
     * @return
     */
    String getSessionUserIdKey(String sessionId);

    /**
     * 缓存 用户id:sessionId和用户信息的Map, key为sessionId和user
     * @param user
     * @param session
     */
    void setUserIdSessionDataKey(User user, String session);

    /**
     * 取key为SessionConstants.USERID_SESSION_KEY + userId，值为用户信息的Map, key为sessionId和user 的缓存
     * @param userId
     * @return
     */
    String getUserIdSessionDataKey(String userId);

    /**
     * 判断key为SessionConstants.USERID_SESSION_KEY + userId，值为用户信息的Map, key为sessionId和user 的缓存是否存在
     * @param s
     * @return
     */
    Boolean existUserIdSessionDataKey(String s);

    /**
     * 添加key为SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId，值为空串的缓存
     * @param oldSessionId
     */
    void addKickSessionKey(String oldSessionId);

    /**
     * 判断key为SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId，值为空串的缓存是否存在
     * @param oldSessionId
     * @return
     */
    Boolean existKickSessionKey(String oldSessionId);

    /**
     * 清空key为SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId，值为空串的缓存
     * @param oldSessionId
     */
    void clearKickSessionKey(String oldSessionId);
}
