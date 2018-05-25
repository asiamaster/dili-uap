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
	 * 初始化用户所有数据权限到Redis
	 * @param userId 用户id
	 */
	void initUserDataAuthesInRedis(Long userId);

	/**
	 * 根据用户id和数据权限类型获取指定数据权限，并缓存到redis
	 * @param userId 用户id
	 * @param dataType 数据权限类型
	 * @return
	 */
	List<DataAuth> listUserDataAuthesByType(Long userId, String dataType);

	/**
	 * 根据用户id获取所有数据权限
	 * @param userId 用户id
	 * @return
	 */
	List<DataAuth> listUserDataAuthes(Long userId);

	/**
	 * 根据用户id和市场编码获取所有数据权限
	 * @param userId	用户id	必填
	 * @param firmCode 市场编码	必填
	 * @return
	 */
	List<DataAuth> listUserDepartmentDataAuthes(Long userId, String firmCode);
}
