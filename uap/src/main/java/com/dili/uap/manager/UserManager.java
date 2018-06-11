package com.dili.uap.manager;


import java.util.List;

/**
 *
 * 用户管理者
 * @createTime 2018-5-22
 * @author wangmi
 */
public interface UserManager {


	void clearSession(String sessionId);

	/**
	 * 清空用户redis数据，返回sessionId列表
	 * @param userId
	 * @return
	 */
	List<String> clearUserSession(Long userId);

	/**
	 * 获取当前在线的用户ID
	 * @return
	 */
	List<String> getOnlineUserIds();

}
