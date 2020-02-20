package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.dto.DepartmentDto;

import java.util.List;


@Restful("${uap.contextPath}")
public interface DepartmentRpc {
	/**
     * 根据条件查询部门
	 * @param department
     * @return
     */
	@POST("/departmentApi/listByExample.api")
	BaseOutput<List<Department>> listByExample(@VOBody DepartmentDto department);

}
