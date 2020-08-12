package com.dili.uap.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.domain.ExceptionLog;
import com.dili.logger.sdk.domain.input.ExceptionLogQueryInput;
import com.dili.logger.sdk.rpc.ExceptionLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.DateUtils;
import com.dili.uap.domain.dto.SystemExceptionLogDto;
import com.dili.uap.sdk.domain.SystemExceptionLog;
import com.dili.uap.sdk.glossary.ExceptionType;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.service.SystemExceptionLogService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 15:27:04.
 */
@Controller
@RequestMapping("/systemExceptionLog")
public class SystemExceptionLogController {
	@Autowired
	SystemExceptionLogService systemExceptionLogService;
	@Autowired
	private ExceptionLogRpc logRpc;

	/**
	 * 跳转到SystemExceptionLog页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "systemExceptionLog/index";
	}

	/**
	 * 查询SystemExceptionLog
	 * 
	 * @param systemExceptionLog
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<SystemExceptionLog> list(SystemExceptionLog systemExceptionLog) {
		return systemExceptionLogService.list(systemExceptionLog);
	}

	/**
	 * 分页查询SystemExceptionLog
	 * 
	 * @param systemExceptionLog
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(SystemExceptionLogDto systemExceptionLog, HttpServletRequest req) throws Exception {
		if (StringUtils.isNotBlank(req.getHeader(SessionConstants.SESSION_ID))) {
			if (systemExceptionLog.getEndExceptionTime() == null) {
				systemExceptionLog.setEndExceptionTime(new Date());
			}
			if (systemExceptionLog.getStartExceptionTime() == null) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -31);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				systemExceptionLog.setStartExceptionTime(calendar.getTime());
			} else {
				int diff = DateUtils.differentDays(systemExceptionLog.getStartExceptionTime(), systemExceptionLog.getEndExceptionTime());
				if (diff > 31) {
					return JSON.toJSONString(BaseOutput.failure("只能导出31天以内的数据"));
				}
			}
		}
		ExceptionLogQueryInput query = new ExceptionLogQueryInput();
		if (systemExceptionLog.getStartExceptionTime() != null) {
			query.setCreateTimeStart(LocalDateTime.ofInstant(systemExceptionLog.getStartExceptionTime().toInstant(), ZoneId.systemDefault()));
		}
		if (systemExceptionLog.getEndExceptionTime() != null) {
			query.setCreateTimeEnd(LocalDateTime.ofInstant(systemExceptionLog.getEndExceptionTime().toInstant(), ZoneId.systemDefault()));
		}
		if (StringUtils.isNotBlank(systemExceptionLog.getSystemCode())) {
			query.setSystemCode(systemExceptionLog.getSystemCode());
		}
		if (StringUtils.isNotBlank(systemExceptionLog.getType())) {
			query.setExceptionType(systemExceptionLog.getType());
		} else {
			Set<String> types = new HashSet<>(ExceptionType.values().length);
			for (ExceptionType exceptionType : ExceptionType.values()) {
				types.add(exceptionType.getCode());
			}
			query.setExceptionTypeSet(types);
		}
		PageOutput<List<ExceptionLog>> output = this.logRpc.listPage(query);
		return new EasyuiPageOutput(output.getTotal(), output.getData()).toString();
	}

}