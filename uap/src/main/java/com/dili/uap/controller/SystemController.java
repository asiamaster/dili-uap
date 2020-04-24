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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 16:24:56.
 */
@Api("/system")
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
	@ApiOperation("跳转到System页面")
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
	@ApiOperation(value = "查询System", notes = "查询System，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Systems", paramType = "form", value = "System的form信息", required = false, dataType = "string") })
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
	@ApiOperation(value = "分页查询System", notes = "分页查询System，返回easyui分页信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Systems", paramType = "form", value = "System的form信息", required = false, dataType = "string") })
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
	@ApiOperation("新增System")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Systems", paramType = "form", value = "System的form信息", required = true, dataType = "string") })
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
	@ApiOperation("修改System")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Systems", paramType = "form", value = "System的form信息", required = true, dataType = "string") })
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
	@ApiOperation("删除System")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "form", value = "System的主键", required = true, dataType = "long") })
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
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}
}