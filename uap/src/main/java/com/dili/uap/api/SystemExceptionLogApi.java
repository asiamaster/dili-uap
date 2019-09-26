package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.SystemExceptionLog;
import com.dili.uap.service.SystemExceptionLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统异常日志Api
 */
@Api("/systemExceptionLog")
@Controller
@RequestMapping("/systemExceptionLog")
public class SystemExceptionLogApi {
	@Autowired
	private SystemExceptionLogService systemExceptionLogService;

	/**
	 * 新增DataAuth
	 * @param systemExceptionLog
	 * @return
	 */
	@ApiOperation("新增DataAuth")
	@ApiImplicitParams({ @ApiImplicitParam(name = "SystemExceptionLog", paramType = "form", value = "SystemExceptionLog的form信息", required = true, dataType = "SystemExceptionLog") })
	@RequestMapping(value = "/insert.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput insert(SystemExceptionLog systemExceptionLog) {
		systemExceptionLogService.insertSelective(systemExceptionLog);
		return BaseOutput.success("插入成功");
	}

}