package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.component.ResumeLockedUserJob;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.ScheduleMessage;
import com.dili.uap.domain.dto.UserDepartmentRole;
import com.dili.uap.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.service.RoleService;
import com.dili.uap.service.UserService;
import com.github.pagehelper.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Controller
@RequestMapping("/userApi")
public class UserApi {
	@Autowired
	UserService userService;

	@Autowired
	DepartmentMapper departmentMapper;

	@Autowired
	ResumeLockedUserJob resumeLockedUserJob;
	
	@Autowired
	RoleService roleService;

	/**
	 * 查询User实体接口
	 * 
	 * @param id
	 * @return
	 */
//	@Idempotent(Idempotent.HEADER)
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
	public BaseOutput<List<User>> listByExample(UserQuery user) {
		if (StringUtils.isNotBlank(user.getKeyword())) {
			String keyword = StringEscapeUtils.escapeSql(user.getKeyword());
			user.setMetadata(IDTO.AND_CONDITION_EXPR, "(user_name like '%" + keyword + "%' or real_name like '%" + keyword + "%' or serial_number like '%" + keyword + "%')");
		}
		List<User> users = this.userService.listByExample(user);
		return BaseOutput.success().setData(users);
	}

	/**
	 * 查询用户
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listPageByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public PageOutput<List<User>> listPageByExample(UserQuery user) {
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
	 * @param firmCode     部门id
	 * @param resourceCode 权限编码
	 * @return
	 */
	@RequestMapping("/findCurrentFirmUsersByResourceCode.api")
	@ResponseBody
	public BaseOutput<List<User>> findCurrentFirmUsersByResourceCode(@RequestParam String firmCode, @RequestParam String resourceCode) {
		List<User> list = this.userService.findCurrentFirmUsersByResourceCode(firmCode, resourceCode);
		return BaseOutput.success().setData(list);
	}

	/**
	 * 验证用户密码
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validatePassword.api")
	public BaseOutput<Object> validatePassword(@RequestParam Long userId, @RequestParam String password) {
		return this.userService.validatePassword(userId, password);
	}
	
	/**
	 * 添加用户角色关联
	 * @param json 包含userId，roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveUserRoles.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput<Object> saveUserRoles(@RequestBody String json) {
		// @RequestParam Long userId, @RequestParam Long roleId
		JSONObject jo = JSON.parseObject(json);
		Long userId = jo.getLong("userId");
		Long roleId = jo.getLong("roleId");
		return userService.saveUserRole(userId, roleId);
	}
	/**
	 * 删除用户角色关联
	 * @param json 包含userId，roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/unbindUserRole.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput<Object> unbindUserRole(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Long userId = jo.getLong("userId");
		Long roleId = jo.getLong("roleId");
		return roleService.unbindRoleUser(roleId, userId);
	}
}