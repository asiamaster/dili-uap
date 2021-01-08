package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.uap.component.ResumeLockedUserJob;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.ScheduleMessage;
import com.dili.uap.domain.dto.UserDepartmentRole;
import com.dili.uap.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.glossary.UserState;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.RoleService;
import com.dili.uap.service.UserService;
import com.github.pagehelper.Page;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

	@Autowired
	LoginService loginService;

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
			return PageOutput.success().setTotal(total).setPageNum(page.getPageNum()).setPageSize(page.getPageSize()).setData(users);
		} else {
			return PageOutput.success().setTotal((long) users.size()).setPageNum(user.getPage()).setPageSize(user.getRows()).setData(users);
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
	 * @param firmCode     市场编码
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
	 * 查询具有特定权限编码的用户
	 * 
	 * @param resourceCode 权限编码
	 * @param marketId     市场id
	 * @return
	 */
	@RequestMapping("/findUsersByResourceCode.api")

	@ResponseBody
	public BaseOutput<List<User>> findUsersByResourceCode(@RequestParam String resourceCode, @RequestParam Long marketId) {
		List<User> list = this.userService.findUsersByResourceCode(resourceCode, marketId);
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
	 *
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
	 *
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

	/**
	 * 通过app注册用户
	 *
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registeryByApp.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput registeryByApp(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		String userName = jo.getString("userName");
		String realName = jo.getString("realName");
		String cellphone = jo.getString("cellphone");
		String email = jo.getString("email");
		String position = jo.getString("position");
		String cardNumber = jo.getString("cardNumber");
		String firmCode = jo.getString("firmCode");
		Long departmentId = jo.getLong("departmentId");
		String description = jo.getString("description");
		User user = this.setUser(userName, realName, cellphone, email, position, cardNumber, firmCode, departmentId, description);
		return userService.registeryByApp(user);
	}

	/**
	 * 设置用户对象
	 *
	 * @param userName     用户名
	 * @param realName     真实姓名
	 * @param cellphone    手机号码
	 * @param email        邮箱
	 * @param position     职位
	 * @param cardNumber   卡号
	 * @param firmCode     归属市场编码
	 * @param departmentId 归属部门
	 * @param description  备注
	 * @return
	 */
	private User setUser(String userName, String realName, String cellphone, String email, String position, String cardNumber, String firmCode, Long departmentId, String description) {
		UserDto user = DTOUtils.newInstance(UserDto.class);
		user.setUserName(userName);
		user.setRealName(realName);
		user.setCellphone(cellphone);
		user.setEmail(email);
		user.setPosition(position);
		user.setCardNumber(cardNumber);
		user.setFirmCode(firmCode);
		user.setDepartmentId(departmentId);
		user.setDescription(description);
		return user;
	}

	/**
	 * 小程序修改密码
	 *
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changePwdForApp.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput changePwdForApp(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Long userId = jo.getLong("userId");
		String oldPassword = jo.getString("oldPassword");
		String newPassword = jo.getString("newPassword");
		String confirmPassword = jo.getString("confirmPassword");
		UserDto user = DTOUtils.newInstance(UserDto.class);
		user.setOldPassword(oldPassword);
		user.setNewPassword(newPassword);
		user.setConfirmPassword(confirmPassword);
		User userInDB = userService.get(userId);
		if (userInDB == null) {
			return BaseOutput.failure("用户不存在");
		}
		if (!UserState.INACTIVE.getCode().equals(userInDB.getState())) {
			return BaseOutput.failure("用户当前状态不能修改密码");
		}
		BaseOutput output = userService.changePwd(userId, user);
		if (!output.isSuccess()) {
			return BaseOutput.failure(output.getMessage());
		}
		return output;
	}

	/**
	 * 根据部门id获取部门人数
	 *
	 * @param ids eg:1,2,3
	 * @param date 截止创建日期,yyyy-MM-dd
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserCountByDepartmentIds.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput getUserCountByDepartmentIds(@RequestParam String ids,@RequestParam String date) {
		List<HashMap<String, Object>> list;
		Map<String,Object> map = new HashMap<>();
		if(StringUtils.isBlank(date)){
			date = DateUtils.format(new Date(),"yyyy-MM-dd");
		}
		map.put("date",date+" 23:59:59");
		List<Long> longList = null;
		if(StringUtils.isNotBlank(ids)){
			String str[] = ids.split(",");
			Long longIds[] = (Long[]) ConvertUtils.convert(str, Long.class);
			longList = Arrays.asList(longIds);
			map.put("departmentIds",longList);
			list = userService.getUserCountByDepartmentIds(map);
		}
		map.put("departmentIds",longList);
		list = userService.getUserCountByDepartmentIds(map);
		return BaseOutput.success().setData(list);
	}
}