package com.dili.uap.manager;


import java.util.Set;

/**
 *
 * 用户管理者
 * @createTime 2018-5-22
 * @author wangmi
 */
public interface UserManager {


	void clearSession(String sessionId);

	String clearUserSession(Long userId);

	/**
	 * 获取当前在线的用户ID
	 * @return
	 */
	Set<String> getOnlineUserIds();

}
