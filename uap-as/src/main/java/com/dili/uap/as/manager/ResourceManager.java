package com.dili.uap.as.manager;

/**
 * 资源管理者
 * 
 * @createTime 2018-5-21
 * @author wangmi
 */
public interface ResourceManager {

	/**
	 * 初始化用户资源权限到redis
	 * 
	 * @param userId
	 */
	void initWebUserResourceCodeInRedis(Long userId);

	/**
	 * 初始化用户APP资源权限到redis
	 * 
	 * @param userId
	 */
	void initAppUserResourceCodeInRedis(Long userId);

	/**
	 * 初始化用户资源权限到redis
	 * @param userId
	 * @param systemType
	 */
	void initUserResourceCodeInRedis(Long userId, Integer systemType);
}
