package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.service.DepartmentService;
import com.dili.uap.service.FirmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 部门接口
 */
@Controller
@RequestMapping("/departmentApi")
public class DepartmentApi {
	@Autowired
    private DepartmentService departmentService;
	@Autowired
	private FirmService firmService;

	/**
	 * 根据id查询部门
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/get.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput<Department> get(@RequestBody Long id) {
		return BaseOutput.success().setData(departmentService.get(id));
	}

	/**
	 * 查询部门
	 * 
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Department>> listByExample(DepartmentDto department) {
		//如果传入的市场code为空，市场id不为空，因为部门表里没有存市场id，则需要转换市场id为市场code
		if (Objects.nonNull(department.getFirmId()) && StringUtils.isBlank(department.getFirmCode())) {
			Firm firm = firmService.get(department.getFirmId());
			if (Objects.nonNull(firm)) {
				department.setFirmCode(firm.getCode());
			}
		}
		return BaseOutput.success().setData(this.departmentService.listByExample(department));
	}

	/**
	 * 查询单个部门
	 * 
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOne.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Department> getOne(Department department) {
		return BaseOutput.success().setData(this.departmentService.getDepartment(department));
	}

	/**
	 * 查询部门列表
	 * 
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByDepartment.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Department>> listByDepartment(Department department) {
		return BaseOutput.success().setData(this.departmentService.listByExample(department));
	}

	/**
	 * 根据iD查询所在一级部门
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFirstDepartment.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Department> getFirstDepartment(@RequestBody Long id) {
		return BaseOutput.success().setData(this.departmentService.getFirstDepartment(id));
	}

	/**
	 * 根据父级id查询所有子部门，包含子部门的子部门
	 * 
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getChildDepartments.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Department> getChildDepartments(@RequestParam Long parentId) {
		return BaseOutput.success().setData(this.departmentService.getChildDepartments(parentId));
	}

	/**
	 * 根据用户ID及市场ID，查询用户在某市场中有权限的部门
	 * @param userId 用户ID
	 * @param firmId 市场ID
	 * @return
	 */
	@RequestMapping("/listUserAuthDepartmentByFirmId.api")
	@ResponseBody
	public BaseOutput<List<Department>> listUserAuthDepartmentByFirmId(@RequestParam("userId") Long userId, @RequestParam("firmId") Long firmId) {
		return BaseOutput.success().setData(this.departmentService.listUserAuthDepartmentByFirmId(userId, firmId));
	}

}