package com.dili.uap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.SystemDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.SystemService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 16:24:56.
 */
@Controller
@RequestMapping("/system")
public class SystemController {
	@Autowired
	SystemService systemService;

	/**
	 * 跳转到System页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "system/index";
	}

	/**
	 * 查询System
	 * 
	 * @param system
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Systems> list(Systems system) {
		return systemService.list(system);
	}

	/**
	 * 分页查询System
	 * 
	 * @param system
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(SystemDto system) throws Exception {
		return systemService.listEasyuiPageByExample(system, true).toString();
	}

	/**
	 * 新增System
	 * 
	 * @param system
	 * @return
	 */
	@BusinessLogger(businessType = "system_management", content = "新增系统", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(Systems system) {
		BaseOutput output = systemService.insertAfterCheck(system);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, system.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, system.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 修改System
	 * 
	 * @param system
	 * @return
	 */
	@BusinessLogger(businessType = "system_management", content = "修改系统", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(Systems system) {
		BaseOutput output = systemService.updateAfterCheck(system);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, system.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, system.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 删除System
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "system_management", content = "修改系统", operationType = "del", systemCode = "UAP")
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		Systems system = this.systemService.get(id);
		BaseOutput output = systemService.deleteAfterCheck(id);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, system.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, system.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}
}