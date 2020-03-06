package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.dto.DepartmentDto;

import java.util.List;

@Restful("${uap.contextPath}")
public interface DepartmentRpc {
	/**
	 * 根据条件查询部门
	 * 
	 * @param department
	 * @return
	 */
	@POST("/departmentApi/listByExample.api")
	BaseOutput<List<Department>> listByExample(@VOBody DepartmentDto department);

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	@POST("/departmentApi/get.api")
	BaseOutput<Department> get(@VOBody Long id);

	/**
	 * 根据条件查询，返回唯一结果
	 * 
	 * @param department
	 * @return
	 */
	@POST("/departmentApi/getOne.api")
	BaseOutput<Department> getOne(@VOBody(required = false) Department department);

	/**
	 * 根据条件查询
	 * 
	 * @param department
	 * @return
	 */
	@POST("/api/departmentApi/listByExample.api")
	BaseOutput<Department> listByExample(@VOBody(required = false) Department department);

	/**
	 * 根据条件查询
	 * 
	 * @param department
	 * @return
	 */
	@POST("/departmentApi/listByDepartment.api")
	BaseOutput<List<Department>> listByDepartment(@VOBody(required = false) Department department);

	/**
	 * 根据用户id查询
	 * 
	 * @param userId
	 * @return
	 */
	@POST("/departmentApi/findByUserId.api")
	BaseOutput<List<Department>> findByUserId(@VOBody Long userId);

	/**
	 * 根据id查询一级部门
	 * 
	 * @param id
	 * @return
	 */
	@POST("/departmentApi/getFirstDepartment.api")
	BaseOutput<Department> getFirstDepartment(@VOBody Long id);

	/**
	 * 根据父级部门id查询子部门，包含子部门的子部门
	 * 
	 * @param parentId
	 * @return
	 */
	@POST("/departmentApi/getChildDepartments.api")
	BaseOutput<List<Department>> getChildDepartments(@ReqParam(required = true, value = "parentId") Long parentId);
}
