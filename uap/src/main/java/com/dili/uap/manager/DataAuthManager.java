package com.dili.uap.manager;


import com.dili.uap.domain.DataAuth;

import java.util.List;

/**
 * 数据权限管理者
 * @createTime 2018-5-23
 * @author wm
 */
public interface DataAuthManager {

	/**
	 * 初始化用户数据权限到Redis
	 * @param userId
	 */
	void initUserDataAuthsInRedis(Long userId);

	/**
	 * 根据用户id和数据权限类型获取指定数据权限
	 * @param userId
	 * @param dataType
	 * @return
	 */
	DataAuth getUserCurrentDataAuth(Long userId, String dataType);

	/**
	 * 根据用户id获取所有数据权限
	 * @param userId
	 * @return
	 */
	List<DataAuth> getUserDataAuth(Long userId);
}
