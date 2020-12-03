package com.dili.uap.manager;

import javax.validation.constraints.NotNull;

import com.dili.uap.sdk.validator.ModifyView;

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
	void initUserResourceCodeInRedis(Long userId);

	/**
	 * 初始化用户APP资源权限到redis
	 * 
	 * @param userId
	 */
	void initUserResourceCodeTokenInRedis(Long userId);

}
