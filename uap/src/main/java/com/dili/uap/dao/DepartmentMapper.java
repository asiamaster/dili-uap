package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper extends MyMapper<Department> {

	/**
	 * 查询部门列表(id以及parent带有前辍)
	 * 
	 * @param department
	 * @return
	 */
	List<Map> listDepartments(Department department);

	List<Department> findByUserId(Long userId);

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
}