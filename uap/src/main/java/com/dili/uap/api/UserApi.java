package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.component.ResumeLockedUserJob;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.ScheduleMessage;
import com.dili.uap.domain.dto.UserDepartmentRole;
import com.dili.uap.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.domain.dto.UserQuery;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.service.UserService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Api("/userApi")
@Controller
@RequestMapping("/userApi")
public class UserApi {
	@Autowired
	UserService userService;

	@Autowired
	DepartmentMapper departmentMapper;

	@Autowired
	ResumeLockedUserJob resumeLockedUserJob;

	/**
	 * 查询User实体接口
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "查询User实体接口", notes = "根据id查询User接口，返回User实体")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "User的id", required = true, dataType = "long") })
	@RequestMapping(value = "/get.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<User> get(@RequestBody Long id) {
		return BaseOutput.success().setData(userService.get(id));
	}

	/**
	 * 查询User列表接口
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "查询User列表接口", notes = "查询User列表接口，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<List<User>> list(User user) {
		return BaseOutput.success().setData(userService.list(user));
	}

	/**
	 * 查询用户
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public PageOutput<List<User>> listByExample(UserQuery user) {
		List<User> users = this.userService.listByExample(user);
		if (users instanceof Page) {
			Page<User> page = (Page) users;
			Long total = page.getTotal();
			return PageOutput.success().setTotal(total.intValue()).setPageNum(page.getPageNum()).setPageSize(page.getPageSize()).setData(users);
		} else {
			return PageOutput.success().setTotal(users.size()).setPageNum(user.getPage()).setPageSize(user.getRows()).setData(users);
		}
	}

	/**
	 * 根据ids查询用户
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listUserByIds.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<User>> listUserByIds(@RequestBody List<String> ids) {
		UserDto user = DTOUtils.newInstance(UserDto.class);
		user.setIds(ids);
		return BaseOutput.success().setData(userService.listByExample(user));
	}

	/**
	 * 根据角色roleId查询用户集合
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "listUserByRoleId.api")
	@ResponseBody
	public BaseOutput<List<User>> listUserByRoleId(@RequestBody Long roleId) {
		return BaseOutput.success().setData(userService.findUserByRole(roleId));
	}

	/**
	 * 根据用户，查询用户对应角色，部门信息
	 * 
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findUserContainDepartmentAndRole.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<UserDepartmentRole>> findUserContainDepartmentAndRole(@RequestBody UserDepartmentRoleQuery query) {
		List<UserDepartmentRole> list = this.userService.findUserContainDepartmentAndRole(query);
		return BaseOutput.success().setData(list);
	}

	/**
	 * 扫描并解锁锁定的用户
	 * 
	 * @param scheduleMessage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resumeLockedUser.api", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput resumeLockedUser(@RequestBody ScheduleMessage scheduleMessage) {
		resumeLockedUserJob.scan(scheduleMessage);
		return BaseOutput.success();
	}

	/**
	 * 查询当前市场下具有特定权限编码的用户
	 * 
	 * @param firmCode 部门id
	 * @param resourceCode 权限编码
	 * @return
	 */
	@RequestMapping("/findCurrentFirmUsersByResourceCode.api")
	@ResponseBody
	public BaseOutput<List<User>> findCurrentFirmUsersByResourceCode(@RequestParam String firmCode, @RequestParam String resourceCode) {
		List<User> list = this.userService.findCurrentFirmUsersByResourceCode(firmCode, resourceCode);
		return BaseOutput.success().setData(list);
	}

}