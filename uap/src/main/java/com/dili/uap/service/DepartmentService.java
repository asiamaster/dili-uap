package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 16:10:05.
 */
public interface DepartmentService extends BaseService<Department, Long> {

	/**
	 * 新增部门检查
	 * 
	 * @param department
	 * @return
	 */
	BaseOutput<Department> insertAfterCheck(Department department);

	/**
	 * 修改部门检查
	 * 
	 * @param department
	 * @return
	 */
	BaseOutput<Department> updateAfterCheck(Department department);

	/**
	 * 查询部门列表
	 * 
	 * @param department
	 * @return
	 */
	List<Map> listDepartments(Department department);

	/**
	 * 查询单个部门
	 * 
	 * @param department
	 * @return
	 */
	Department getDepartment(Department department);

	/**
	 * 根据部门Id查询一级部门
	 * 
	 * @param id
	 * @return
	 */
	Department getFirstDepartment(Long id);

	/**
	 * 根据父级id查询所有子部门，包含子部门的子部门
	 * 
	 * @param parentId
	 * @return
	 */
	List<Department> getChildDepartments(Long parentId);

	/**
	 * 根据用户ID及市场ID，查询用户在某市场中有权限的部门
	 * @param userId 用户ID
	 * @param firmId 市场ID
	 * @return
	 */
	List<Department> listUserAuthDepartmentByFirmId(Long userId, Long firmId);

	/**
	 * 查询用户所在市场部门列表
	 * @return
	 */
	List<Map> listUserDepartment(String firmCode);

	/**
	 * 获取所有有子部门的部门
	 *
	 * @return
	 */
	List<Long> getSeniorDepartmentIds();

	/**
	 * 获取查询部门及其上级部门
	 * set id集合
	 * @return
	 */
	List<Map> getDepartmentTree(Department department);

	/**
	 * 删除部门及用户相关部门
	 * id 部门id
	 * @return
	 */
	void deleteDepartment(Long id);

}