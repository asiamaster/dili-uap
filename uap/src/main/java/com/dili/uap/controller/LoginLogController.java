package com.dili.uap.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginLogDto;
import com.dili.uap.glossary.LoginType;
import com.dili.uap.service.LoginLogService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 15:30:02.
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController {
	@Autowired
	LoginLogService loginLogService;
	@Autowired
	private BusinessLogRpc logRpc;

	/**
	 * 跳转到LoginLog页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "loginLog/index";
	}

	/**
	 * 查询LoginLog
	 * 
	 * @param loginLog
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<LoginLog> list(LoginLog loginLog) {
		return loginLogService.list(loginLog);
	}

	/**
	 * 分页查询LoginLog
	 * 
	 * @param loginLog
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(LoginLogDto loginLog) throws Exception {
		BusinessLogQueryInput query = new BusinessLogQueryInput();
		if (loginLog.getStartLoginTime() != null) {
			query.setCreateTimeStart(LocalDateTime.ofInstant(loginLog.getStartLoginTime().toInstant(), ZoneId.systemDefault()));
		}
		if (loginLog.getEndLoginTime() != null) {
			query.setCreateTimeEnd(LocalDateTime.ofInstant(loginLog.getEndLoginTime().toInstant(), ZoneId.systemDefault()));
		}
		if (StringUtils.isNotBlank(loginLog.getSystemCode())) {
			query.setSystemCode(loginLog.getSystemCode());
		}
		if (StringUtils.isNotBlank(loginLog.getType())) {
			query.setOperationType(loginLog.getType());
		} else {
			query.setOperationTypeSet(new HashSet<String>() {
				{
					add(LoginType.LOGIN.getCode());
					add(LoginType.LOGOUT.getCode());
				}
			});
		}
		PageOutput<List<BusinessLog>> output = this.logRpc.listPage(query);
		return new EasyuiPageOutput(output.getTotal(), output.getData()).toString();
	}

	/**
	 * 新增LoginLog
	 * 
	 * @param loginLog
	 * @return
	 */
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(LoginLog loginLog) {
		loginLogService.insertSelective(loginLog);
		return BaseOutput.success("新增成功");
	}

	/**
	 * 修改LoginLog
	 * 
	 * @param loginLog
	 * @return
	 */
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(LoginLog loginLog) {
		loginLogService.updateSelective(loginLog);
		return BaseOutput.success("修改成功");
	}

	/**
	 * 删除LoginLog
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		loginLogService.delete(id);
		return BaseOutput.success("删除成功");
	}

}