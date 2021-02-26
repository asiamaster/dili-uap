package com.dili.uap.as.manager;

/**
 * @createTime 2018-5-21
 * @author asiamaster
 */
public interface MenuManager {

	/**
	 * 初始化用户菜单列表到redis
	 * 
	 * @param userId
	 */
	void initWebUserMenuUrlsInRedis(Long userId);

	/**
	 * 初始化移动端用户菜单列表到redis
	 * 
	 * @param userId
	 */
	void initAppUserMenuUrlsInRedis(Long userId);

	/**
	 * 初始化用户菜单URL到redis
	 * @param userId
	 * @param systemType
	 */
	void initUserMenuUrlsInRedis(Long userId, Integer systemType);
}
