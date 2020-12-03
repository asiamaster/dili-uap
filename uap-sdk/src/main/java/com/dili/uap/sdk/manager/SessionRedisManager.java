package com.dili.uap.sdk.manager;

import java.util.List;

/**
 * 用户和sessionId相关的缓存管理者 Created by root on 5/20/15.
 */
public interface SessionRedisManager {

	/**
	 * 清空key为SessionConstants.SESSIONID_USERID_KEY + sessionId， 值为用户id的缓存
	 * 
	 * @param sessionId
	 */
	void clearSessionUserIdKey(String sessionId);
	
	/**
	 * 清空key为SessionConstants.TOKEN_USERID_KEY + token， 值为用户id的缓存
	 * 
	 * @param sessionId
	 */
	void clearTokenUserIdKey(String token);

	/**
	 * 清空 key为SessionConstants.USERID_SESSIONID_KEY + userId, 值为:用户信息的Map,
	 * key为sessionId和user 的缓存
	 * 
	 * @param userId
	 */
	void clearUserIdSessionDataKey(String userId);
	
	/**
	 * 清空 key为SessionConstants.USERID_TOKEN_KEY + userId, 值为:用户信息的Map,
	 * key为token和user 的缓存
	 * 
	 * @param userId
	 */
	void clearUserIdTokenDataKey(String userId);

	/**
	 * 清空 key为SessionConstants.USERID_SESSIONID_KEY + userId, 值为:用户信息的Map,
	 * key为sessionId和user 的缓存
	 * 
	 * @param sessionId
	 */
	void clearUserIdSessionDataKeyBySessionId(String sessionId);

	/**
	 * 缓存 SessionConstants.SESSIONID_USERID_KEY + sessionId: userId
	 * 
	 * @param sessionId
	 * @param userId
	 */
	void setSessionUserIdKey(String sessionId, String userId);

	/**
	 * 缓存 SessionConstants.SESSIONID_USERID_KEY + sessionId: userName
	 * 
	 * @param sessionId
	 * @param userName
	 */
	void setSessionUserNameKey(String sessionId, String userName);

	/**
	 * 根据sessionId取用户id
	 * 
	 * @param sessionId
	 * @return
	 */
	String getUserIdBySessionId(String sessionId);

	/**
	 * 根据sessionId取用户登录名
	 * 
	 * @param sessionId
	 * @return
	 */
	String getUserNameBySessionId(String sessionId);

	/**
	 * 缓存 用户id:sessionId和用户信息的Map, key为sessionId和user
	 * 
	 * @param userId
	 * @param sessionId
	 */
	void setUserIdSessionIdKey(String userId, String sessionId);

	/**
	 * 获取指定id用户sessionId集合
	 * 
	 * @param userId
	 * @return
	 */
	List<String> getSessionIdsByUserId(String userId);
	
	/**
	 * 获取指定id用户token集合
	 * 
	 * @param userId
	 * @return
	 */
	List<String> getTokensByUserId(String userId);

	/**
	 * 判断key为SessionConstants.USERID_SESSIONID_KEY + userId，值为用户信息的Map,
	 * key为sessionId和user 的缓存是否存在
	 * 
	 * @param s
	 * @return
	 */
	Boolean existUserIdSessionIdKey(String s);
	
	/**
	 * 判断key为SessionConstants.USERID_TOKEN_KEY + userId，值为用户信息的Map,
	 * key为token和user 的缓存是否存在
	 * 
	 * @param s
	 * @return
	 */
	Boolean existUserIdTokenKey(String s);

	/**
	 * 添加key为SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId，值为空串的缓存
	 * 
	 * @param oldSessionId
	 */
	void addKickSessionKey(String oldSessionId);
	
	/**
	 * 添加key为SessionConstants.KICK_TOKEN_KEY + oldToken，值为空串的缓存
	 * 
	 * @param oldToken
	 */
	void addKickTokenKey(String oldToken);

	/**
	 * 判断key为SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId，值为空串的缓存是否存在
	 * 
	 * @param oldSessionId
	 * @return
	 */
	Boolean existKickSessionKey(String oldSessionId);

	/**
	 * 清空key为SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId，值为空串的缓存
	 * 
	 * @param oldSessionId
	 */
	void clearKickSessionKey(String oldSessionId);

	/**
	 * 获取在线用户id集合
	 * 
	 * @return
	 */
	List<String> getOnlineUserIds();

	/**
	 * 获取在线用户sessionId集合
	 * 
	 * @return
	 */
	List<String> getOnlineUserSessionIds();

	/**
	 * 缓存 SessionConstants.TOKEN_USERID_KEY + sessionId: userId
	 * 
	 * @param token
	 * @param userId
	 */
	void setTokenUserIdKey(String token, String userId);

	/**
	 * 缓存 SessionConstants.TOKEN_USERID_KEY + sessionId: userName
	 * 
	 * @param token 
	 * @param userName
	 */
	void setTokenUserNameKey(String token, String userName);

	/**
	 * 缓存 用户id:token和用户信息的Map, key为token和user
	 * 
	 * @param userId
	 * @param token
	 */
	void setUserIdTokenKey(String userId, String token);

}
