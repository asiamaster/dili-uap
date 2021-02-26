package com.dili.uap.as.manager;

/**
 * 系统管理者
 * 
 * @createTime 2018-6-4
 * @author asiamaster
 */
public interface SystemManager {

	/**
	 * 初始化用户系统列表到redis
	 * 
	 * @param userId
	 */
	void initWebUserSystemInRedis(Long userId);

	/**
	 * 初始化用户移动端系统列表到redis
	 * 
	 * @param userId
	 */
	void initAppUserSystemInRedis(Long userId);

	/**
	 * 根据系统类型初始化用户系统列表到redis
	 *
	 * @param userId
	 */
	void initUserSystemInRedis(Long userId, Integer systemType);
}
