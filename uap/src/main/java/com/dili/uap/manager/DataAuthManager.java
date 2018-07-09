package com.dili.uap.manager;


import com.dili.uap.domain.UserDataAuth;

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
	 * 根据用户id和数据权限引用编码获取指定数据权限
	 * @param userId 用户id
	 * @param refCode 数据权限引用编码
	 * @return
	 */
	List<UserDataAuth> listUserDataAuthesByRefCode(Long userId, String refCode);

	/**
	 * 根据用户id获取所有数据权限
	 * @param userId 用户id
	 * @return
	 */
	List<UserDataAuth> listUserDataAuthes(Long userId);

}
