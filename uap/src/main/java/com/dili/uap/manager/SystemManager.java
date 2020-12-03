package com.dili.uap.manager;

import javax.validation.constraints.NotNull;

import com.dili.uap.sdk.validator.ModifyView;

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
	void initUserSystemInRedis(Long userId);

	/**
	 * 初始化用户移动端系统列表到redis
	 * 
	 * @param userId
	 */
	void initUserSystemTokenInRedis(Long id);

}
