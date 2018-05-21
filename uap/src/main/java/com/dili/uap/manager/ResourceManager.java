package com.dili.uap.manager;


/**
 * 资源管理者
 * @createTime 2018-5-21
 * @author wangmi
 */
public interface ResourceManager {

	/**
	 * 初始化用户资源权限到redis
	 * @param userId
	 */
	void initUserResourceCodeInRedis(Long userId);

}
