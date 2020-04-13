package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.domain.Role;
import com.dili.uap.sdk.domain.dto.RoleUserDto;
import com.dili.uap.domain.RoleMenu;
import com.dili.uap.domain.RoleResource;
import com.dili.uap.domain.dto.SystemResourceDto;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface RoleMapper extends MyMapper<Role> {

	/**
	 * 根据角色ID查询该角色下是否存在有用户
	 * 
	 * @param roleId 角色ID
	 * @return 用户数
	 */
	Long countUserByRoleId(Long roleId);

	/**
	 * 根据角色ID删除角色-菜单信息
	 * 
	 * @param param 角色ID和登录用户ID
	 * @return TODO
	 */
	int deleteRoleMenuByRoleId(Map param);

	/**
	 * 根据角色ID删除角色-资源信息
	 * 
	 * @param param 角色ID和登录用户ID
	 * @return TODO
	 * @return 受影响行数
	 */
	int deleteRoleResourceByRoleId(Map param);

	/**
	 * 加载所有的菜单及资源信息
	 * 
	 * @return
	 */
	List<SystemResourceDto> getRoleMenuAndResource();

	/**
	 * 加载用户有权限的菜单及资源信息
	 * 
	 * @return
	 */
	List<SystemResourceDto> getRoleMenuAndResourceByUserId(Long userId);

	/**
	 * 根据角色ID查询对应的菜单及资源信息
	 * 
	 * @param roleId 角色ID
	 * @return
	 */
	List<SystemResourceDto> getRoleMenuAndResourceByRoleId(Long roleId);

	/**
	 * 根据用户id获取用户所属角色信息
	 * 
	 * @param userId 用户ID
	 * @return 角色信息
	 */
	List<Role> listByUserId(Long userId);

	/**
	 * 根据用户名获取用户所属角色信息
	 * 
	 * @param userName 用户名
	 * @return 角色信息
	 */
	List<Role> listByUserName(String userName);

	/**
	 * 根据角色id批量查询角色，并关联查询出角色下的用户
	 * 
	 * @param roleIds
	 * @return
	 */
	List<RoleUserDto> listRoleUserByRoleIds(List<Long> roleIds);

	List<RoleMenu> selectInsertRoleMenuByLoggedUserId(@Param("roleMenus") List<RoleMenu> roleMenus, Long roleId, @Param("loggedUserId") Long loggedUserId);

	List<RoleResource> selectInsertRoleResourceByLoggedUserId(@Param("roleResources") List<RoleResource> roleResources, Long roleId, @Param("loggedUserId") Long loggedUserId);

	List<SystemResourceDto> selectLimittedUpdateMenuList(@Param("roleMenus") List<RoleMenu> roleMenus, @Param("roleResources") List<RoleResource> roleResources, @Param("roleId") Long roleId,
			@Param("loggedUserId") Long loggedUserId);

}