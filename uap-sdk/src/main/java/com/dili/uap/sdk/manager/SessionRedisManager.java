package com.dili.uap.sdk.manager;



import java.util.List;

/**
 * 用户和sessionId相关的缓存管理者
 * Created by root on 5/20/15.
 */
public interface SessionRedisManager {

    /**
     * 清空key为SessionConstants.SESSIONID_USERID_KEY + sessionId， 值为用户id的缓存
     * @param sessionId
     */
    void clearSessionUserIdKey(String sessionId);

    /**
     * 清空 key为SessionConstants.USERID_SESSIONID_KEY + userId, 值为:用户信息的Map, key为sessionId和user 的缓存
     * @param userId
     */
    void clearUserIdSessionDataKey(String userId);

    /**
     * 清空 key为SessionConstants.USERID_SESSIONID_KEY + userId, 值为:用户信息的Map, key为sessionId和user 的缓存
     * @param sessionId
     */
    void clearUserIdSessionDataKeyBySessionId(String sessionId);

    /**
     * 缓存 SessionConstants.SESSIONID_USERID_KEY + sessionId: userId
     * @param sessionId
     * @param userId
     */
    void setSessionUserIdKey(String sessionId, String userId);

    /**
     * 根据sessionId取用户id
     * @param sessionId
     * @return
     */
    String getUserIdBySessionId(String sessionId);

    /**
     * 缓存 用户id:sessionId和用户信息的Map, key为sessionId和user
     * @param userId
     * @param sessionId
     */
    void setUserIdSessionIdKey(String userId, String sessionId);

    /**
     * 获取指定id用户sessionId集合
     * @param userId
     * @return
     */
    List<String> getSessionIdsByUserId(String userId);

    /**
     * 判断key为SessionConstants.USERID_SESSIONID_KEY + userId，值为用户信息的Map, key为sessionId和user 的缓存是否存在
     * @param s
     * @return
     */
    Boolean existUserIdSessionIdKey(String s);

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

    /**
     * 获取在线用户id集合
     * @return
     */
    List<String> getOnlineUserIds();

    /**
     * 获取在线用户sessionId集合
     * @return
     */
    List<String> getOnlineUserSessionIds();

}
