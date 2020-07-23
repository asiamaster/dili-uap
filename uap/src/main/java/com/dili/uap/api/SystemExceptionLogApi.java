package com.dili.uap.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.SystemExceptionLog;
import com.dili.uap.service.SystemExceptionLogService;

/**
 * 系统异常日志Api
 */
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
	@RequestMapping(value = "/insert.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput insert(SystemExceptionLog systemExceptionLog) {
		systemExceptionLogService.insertSelective(systemExceptionLog);
		return BaseOutput.success("插入成功");
	}

}