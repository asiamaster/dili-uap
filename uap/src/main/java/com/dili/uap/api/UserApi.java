package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.service.UserService;
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

	@ApiOperation(value = "查询User实体接口", notes = "根据id查询User接口，返回User实体")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "User的id", required = true, dataType = "long") })
	@RequestMapping(value = "/get.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody
    BaseOutput<User> get(@RequestBody Long id) {
		return BaseOutput.success().setData(userService.get(id));
	}

	@ApiOperation(value = "查询User列表接口", notes = "查询User列表接口，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody
    BaseOutput<List<User>> list(User user) {
		return BaseOutput.success().setData(userService.list(user));
	}

	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<User> listByExample(User user) {
		return BaseOutput.success().setData(this.userService.listByExample(user));
	}

	@ResponseBody
	@RequestMapping(value = "/listUserByIds.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<User>> listUserByIds(@RequestBody List<String> ids){
		UserDto user = DTOUtils.newDTO(UserDto.class);
		user.setIds(ids);
		return BaseOutput.success().setData(userService.listByExample(user));
	}

}