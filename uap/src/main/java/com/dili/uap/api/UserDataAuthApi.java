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
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.service.UserDataAuthService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Api("/userDataAuthApi")
@Controller
@RequestMapping("/userDataAuthApi")
public class UserDataAuthApi {
	@Autowired
	UserDataAuthService userDataAuthService;



	/**
	 * 根据userId查询UserDataAuthValue值集合接口
	 * @param userId
	 * @return
	 */
	@ApiOperation(value = "查询UserDataAuthValue列表接口", notes = "查询UserDataAuthValue列表接口，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", paramType = "form", value = "UserDataAuthValue的form信息", required = false, dataType = "Long") })
	@RequestMapping(value = "/listUserDataAuthValue.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody
    BaseOutput<List<String>> listUserDataAuthValue(@RequestBody Long userId) {
		List<String> listUserDataAuthValue = userDataAuthService.listUserDataAuthValueByUserId(userId,DataAuthType.PROJECT.getCode());
		return BaseOutput.success().setData(listUserDataAuthValue);
	}

	
}