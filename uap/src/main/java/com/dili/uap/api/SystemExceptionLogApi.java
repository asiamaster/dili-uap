package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.domain.SystemExceptionLog;
import com.dili.uap.domain.UserDataAuth;
import com.dili.uap.manager.DataAuthManager;
import com.dili.uap.service.DataAuthService;
import com.dili.uap.service.SystemExceptionLogService;
import com.dili.uap.service.UserDataAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 系统异常日志Api
 */
@Api("/systemExceptionLog")
@Controller
@RequestMapping("/systemExceptionLog")
public class SystemExceptionLogApi {
	@Autowired
	private SystemExceptionLogService systemExceptionLogService;

	@ApiOperation("新增DataAuth")
	@ApiImplicitParams({ @ApiImplicitParam(name = "SystemExceptionLog", paramType = "form", value = "SystemExceptionLog的form信息", required = true, dataType = "SystemExceptionLog") })
	@RequestMapping(value = "/insert.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(SystemExceptionLog systemExceptionLog) {
		systemExceptionLogService.insertSelective(systemExceptionLog);
		return BaseOutput.success("插入成功");
	}

}