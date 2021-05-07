package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.domain.dto.UserDepartmentRole;
import com.dili.uap.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.domain.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	List<Map> selectUserDatas(Map<String, Object> params);

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

	/**
	 * 将相关置空
	 *
	 * @param user
	 * @return
	 */
	Integer updateByUser(User user);

	/**
	 * 根据部门id获取各部门人数
	 *
	 * @param map{departmentIds:部门id集合;date:截止创建日期}
	 * @return
	 */
	List<HashMap<String, Object>> getUserCountByDepartmentIds(Map<String,Object> map);

	/**
	 * 根据部门ids获取几个部门总人数
	 *
	 * @param map{seniorId:上级部门id;ids:下级部门id集合(包含上级部门id);firmCode:市场code;date:截止创建日期}
	 * @return
	 */
	List<HashMap<Long, Long>> getUserCountByDepartments(List<Map<String,Object>> map);

	/**
	 * 根据userName和realName获取userIds
	 *
	 * @param keywords
	 * @return
	 */
    Set<Long> getIdsByName(String keywords);
}