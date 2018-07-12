package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

	@ApiOperation(value = "查询Department实体接口", notes = "根据id查询Department接口，返回Department实体")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "Department的id", required = true, dataType = "long") })
	@RequestMapping(value = "/get.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput<Department> get(@RequestBody Long id) {
		return BaseOutput.success().setData(departmentService.get(id));
	}

	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Department> listByExample(@RequestBody(required = false) Department department) {
		return BaseOutput.success().setData(this.departmentService.listByExample(department));
	}
}