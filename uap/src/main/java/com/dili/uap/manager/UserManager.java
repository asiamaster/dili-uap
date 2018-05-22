package com.dili.uap.manager;


/**
 *
 * 用户管理者
 * @createTime 2018-5-22
 * @author wangmi
 */
public interface UserManager {


	void clearSession(String sessionId);

	String clearUserSession(Long userId);

}
