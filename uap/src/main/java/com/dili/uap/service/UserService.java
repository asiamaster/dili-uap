package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.domain.dto.UserDataDto;
import com.dili.uap.domain.dto.UserDepartmentRole;
import com.dili.uap.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserDataAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-18 10:46:46.
 */
public interface UserService extends BaseService<User, Long> {

	/**
	 * 登出
	 * 
	 * @param sessionId
	 */
	void logout(String sessionId);

	/**
	 * 根据角色ID查询用户列表信息
	 * 
	 * @param roleId 角色ID
	 * @return 用户列表
	 */
	List<User> findUserByRole(Long roleId);

	/**
	 * 修改密码
	 * 
	 * @param userId 用户ID
	 * @param user   用户信息
	 * @return 结果
	 */
	BaseOutput<Object> changePwd(Long userId, UserDto user);

	/**
	 * 保存用户信息
	 * 
	 * @param user 用户信息对象
	 * @return
	 */
	BaseOutput save(User user);

	/**
	 * 根据用户ID重置密码
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	BaseOutput resetPass(Long userId);

	/**
	 * 根据用户ID，操作启禁用 用户
	 * 
	 * @param userId 用户ID
	 * @param enable 是否启用(true-启用，false-禁用)
	 * @return
	 */
	BaseOutput upateEnable(Long userId, Boolean enable);

	/**
	 * 根据用户ID获取用户所拥有的角色权限
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<UserDataDto> getUserRolesForTree(Long userId);

	/**
	 * 保存用户的角色信息
	 * 
	 * @param userId  用户ID
	 * @param roleIds 角色ID集
	 * @return
	 */
	BaseOutput saveUserRoles(Long userId, String[] roleIds);

	/**
	 * 查询用户列表
	 * 
	 * @param domain
	 * @param useProvider
	 * @return
	 * @throws Exception
	 */
	public EasyuiPageOutput selectForEasyuiPage(UserDto domain, boolean useProvider) throws Exception;

	/**
	 * 根据id查询用户信息
	 *
	 * @param userId 用户id
	 * @return
	 */
	BaseOutput<Object> fetchLoginUserInfo(Long userId);

	/**
	 * 获取用户的数据权限
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<UserDataDto> getUserDataAuthForTree(Long userId);

	/**
	 * 保存用户的角色信息
	 * 
	 * @param userId    用户ID
	 * @param dataIds   数据ID(包含所属集合)
	 * @param dataRange 数据权限范围
	 * @return 操作结果
	 */
	BaseOutput<List<UserDataAuth>> saveUserDatas(Long userId, String[] dataIds, Long dataRange);

	/**
	 * 根据用户ID解锁用户
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	BaseOutput unlock(Long userId);

	/**
	 * 查询在线用户列表
	 * 
	 * @param user
	 * @return
	 */
	EasyuiPageOutput listOnlinePage(UserDto user) throws Exception;

	/**
	 * 根据用户ID强制下线用户
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	BaseOutput forcedOffline(Long userId, Integer systemType);

	/**
	 * 根据用户，查询用户对应角色，部门信息
	 * 
	 * @param UserDepartmentRoleQuery
	 * @return
	 */
	List<UserDepartmentRole> findUserContainDepartmentAndRole(UserDepartmentRoleQuery query);

	/**
	 * 根据用户ID获取用户所拥有的项目权限
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<UserDataDto> getUserDataProjectAuthForTree(Long userId);

	/**
	 * 查询当前市场下具有特定权限编码的用户
	 * 
	 * @param firmCode     部门id
	 * @param resourceCode 权限编码
	 * @return
	 */
	List<User> findCurrentFirmUsersByResourceCode(String firmCode, String resourceCode);

	/**
	 * 验证用户密码
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	BaseOutput<Object> validatePassword(Long userId, String password);

	/**
	 * 保存用户的角色信息
	 *
	 * @param userId 用户ID
	 * @param roleId 角色ID
	 * @return
	 */
	BaseOutput saveUserRole(Long userId, Long roleId);

	/**
	 * 查询具有特定权限编码的用户
	 * 
	 * @param resourceCode 权限编码
	 * @param marketId     TODO
	 * @return
	 */
	List<User> findUsersByResourceCode(String resourceCode, Long marketId);

	/**
	 * 通过app注册用户
	 *
	 * @param user
	 * 
	 * @return
	 */
	BaseOutput registeryByApp(User user);

	/**
	 * 获取进厅数据权限
	 * 
	 * @param id
	 * @return
	 */
	List<UserDataDto> getUserTradingDataAuth(Long id);

	/**
	 * 根据部门id获取各部门人数
	 *
	 * @param map{departmentIds:部门id集合;date:截止创建日期}
	 * @return
	 */
	List<HashMap<String, Object>> getUserCountByDepartmentIds(Map<String,Object> map);

	/**
	 * 将superiorId置空
	 *
	 * @param superiorId   上级id
	 * @return
	 */
	Integer updateBySuperiorId(Long superiorId);

	/**
	 * 根据部门ids获取几个部门总人数
	 *
	 * @param map{seniorId:上级部门id;ids:下级部门id集合(包含上级部门id);firmCode:市场code;date:截止创建日期}
	 * @return
	 */
	List<HashMap<Long, Long>> getUserCountByDepartments(List<Map<String,Object>> map);

}