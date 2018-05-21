package com.dili.uap.manager;


/**
 * @createTime 2018-5-21
 * @author asiamaster
 */
public interface MenuManager {

	/**
	 * 初始化用户菜单列表到redis
	 * @param userId
	 */
	void initUserMenuUrlsInRedis(Long userId);

}
