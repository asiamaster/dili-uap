package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Department;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DepartmentMapper extends MyMapper<Department> {

	/**
	 * 查询部门列表(id以及parent带有前辍)
	 * 
	 * @param department
	 * @return
	 */
	List<Map> listDepartments(Department department);

	/**
	 * 根据父级id查询所有子部门，包含子部门的子部门
	 * 
	 * @param parentId
	 * @return
	 */
	List<Department> getChildDepartments(Long parentId);

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
	 * 获取id集合的所有父级部门
	 * set id集合
	 * @return
	 */
	List<String> getDepartmentParentList(Set<String> set);

	/**
	 * 查询部门列表及其所属市场
	 *
	 * @param queryMap
	 * @return
	 */
	List<Map> getDepartmentTree(Map<String,Set<String>> queryMap);

	/**
	 * 查询部门
	 *
	 * @param department
	 * @return
	 */
	List<Department> queryDepartments(Department department);
}