package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 部门接口
 */
@Api("/departmentApi")
@Controller
@RequestMapping("/departmentApi")
public class DepartmentApi {
	@Autowired
    DepartmentService departmentService;

	/**
	 * 根据id查询部门
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "查询Department实体接口", notes = "根据id查询Department接口，返回Department实体")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "Department的id", required = true, dataType = "long") })
	@RequestMapping(value = "/get.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput<Department> get(@RequestBody Long id) {
		return BaseOutput.success().setData(departmentService.get(id));
	}

	/**
	 * 查询部门
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Department> listByExample(@RequestBody(required = false) Department department) {
		return BaseOutput.success().setData(this.departmentService.listByExample(department));
	}
	
	
	/**
	 * 查询单个部门
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByOne.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Department> listByOne(@RequestBody(required = false) Department department) {
		return BaseOutput.success().setData(this.departmentService.getDepartment(department));
	}
	
	
	/**
	 * 查询部门列表
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByDepartment.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Department>> listByDepartment(@RequestBody(required = false) Department department) {
		return BaseOutput.success().setData(this.departmentService.listByExample(department));
	}
}