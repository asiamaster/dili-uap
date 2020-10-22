package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.domain.dto.UserDataDto;
import com.dili.uap.domain.dto.UserDepartmentRole;
import com.dili.uap.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.domain.dto.UserDto;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserMapper extends MyMapper<User> {

	/**
	 * 根据角色ID查询用户列表信息
	 * 
	 * @param roleId 角色ID
	 * @return 用户列表
	 */
	List<User> findUserByRole(Long roleId);

	/**
	 * 根据条件联合查询数据信息
	 * 
	 * @param user
	 * @return
	 */
	List<UserDto> selectForPage(UserDto user);

	/**
	 * 根据用户ID和登录用户id查询用户所拥有的数据权限 超级管理员不用传入loginUserId参数
	 * 
	 * @param params 查询参数(key:userId(用户ID)、firmCode(市场code))
	 * @return
	 */
	List<UserDataDto> selectUserDatas(Map<String, Object> params);

	/**
	 * 根据用户，查询用户对应角色，部门信息
	 * 
	 * @param UserDepartmentRoleQuery
	 * @return
	 */
	List<UserDepartmentRole> findUserContainDepartmentAndRole(UserDepartmentRoleQuery query);

	/**
	 * 查询当前部门下具有特定权限编码的用户
	 * 
	 * @param firmCode     部门id
	 * @param resourceCode 权限编码
	 * @return
	 */
	List<User> findCurrentFirmUsersByResourceCode(@Param("firmCode") String firmCode, @Param("resourceCode") String resourceCode);

	/**
	 * 查询具有特定权限编码的用户
	 * 
	 * @param resourceCode 权限编码
	 * @param marketId TODO
	 * @return
	 */
	List<User> findUsersByResourceCode(String resourceCode, Long marketId);
}