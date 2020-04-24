package com.dili.uap.controller;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.SystemConfigDto;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 14:34:21.
 */
@Api("/systemConfig")
@Controller
@RequestMapping("/systemConfig")
public class SystemConfigController {
	@Autowired
	SystemConfigService systemConfigService;

	/**
	 * 跳转到SystemConfig页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到SystemConfig页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "systemConfig/index";
	}

	/**
	 * 查询SystemConfig
	 * 
	 * @param systemConfig
	 * @return
	 */
	@ApiOperation(value = "查询SystemConfig", notes = "查询SystemConfig，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "SystemConfig", paramType = "form", value = "SystemConfig的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<SystemConfig> list(SystemConfig systemConfig) {
		return systemConfigService.list(systemConfig);
	}

	/**
	 * 分页查询SystemConfig
	 * 
	 * @param systemConfig
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询SystemConfig", notes = "分页查询SystemConfig，返回easyui分页信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "SystemConfig", paramType = "form", value = "SystemConfig的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(SystemConfigDto systemConfig) throws Exception {
		return systemConfigService.listEasyuiPageByExample(systemConfig, true).toString();
	}

	/**
	 * 新增SystemConfig
	 * 
	 * @param systemConfig
	 * @return
	 */
	@ApiOperation("新增SystemConfig")
	@ApiImplicitParams({ @ApiImplicitParam(name = "SystemConfig", paramType = "form", value = "SystemConfig的form信息", required = true, dataType = "string") })
	@BusinessLogger(businessType = "system_config_management", content = "新增系统配置,系统编码：${systemCode}", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(SystemConfig systemConfig) {
		systemConfigService.insertSelective(systemConfig);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, systemConfig.getCode());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, systemConfig.getId());
		LoggerContext.put("systemCode", systemConfig.getSystemCode());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("新增成功").setData(systemConfig);

	}

	/**
	 * 修改SystemConfig
	 * 
	 * @param systemConfig
	 * @return
	 */
	@ApiOperation("修改SystemConfig")
	@ApiImplicitParams({ @ApiImplicitParam(name = "SystemConfig", paramType = "form", value = "SystemConfig的form信息", required = true, dataType = "string") })
	@BusinessLogger(businessType = "system_config_management", content = "新增系统配置,系统编码：${systemCode}", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(SystemConfig systemConfig) {
		systemConfigService.updateSelective(systemConfig);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, systemConfig.getCode());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, systemConfig.getId());
		LoggerContext.put("systemCode", systemConfig.getSystemCode());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("修改成功").setData(systemConfig);
	}

	/**
	 * 删除SystemConfig
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation("删除SystemConfig")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "form", value = "SystemConfig的主键", required = true, dataType = "long") })
	@BusinessLogger(businessType = "system_config_management", content = "删除系统配置,系统编码：${systemCode}", operationType = "del", systemCode = "UAP")
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		SystemConfig systemConfig = this.systemConfigService.get(id);
		systemConfigService.delete(id);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, systemConfig.getCode());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, systemConfig.getId());
		LoggerContext.put("systemCode", systemConfig.getSystemCode());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("删除成功");
	}
}