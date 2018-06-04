package com.dili.uap.manager;


/**
 * 系统管理者
 * @createTime 2018-6-4
 * @author asiamaster
 */
public interface SystemManager {

	/**
	 * 初始化用户系统列表到redis
	 * @param userId
	 */
	void initUserSystemInRedis(Long userId);

}
