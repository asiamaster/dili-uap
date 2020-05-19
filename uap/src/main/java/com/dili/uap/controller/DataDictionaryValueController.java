package com.dili.uap.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.DataDictionaryValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-21 10:40:13.
 */
@Api("/dataDictionaryValue")
@Controller
@RequestMapping("/dataDictionaryValue")
public class DataDictionaryValueController {
	@Autowired
	DataDictionaryValueService dataDictionaryValueService;

	/**
	 * 跳转到DataDictionaryValue页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到DataDictionaryValue页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "dataDictionaryValue/index";
	}

	/**
	 * 查询DataDictionaryValue
	 * 
	 * @param dataDictionaryValue
	 * @param map
	 * @return
	 */
	@ApiOperation(value = "查询DataDictionaryValue", notes = "查询DataDictionaryValue，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.html", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(DataDictionaryValue dataDictionaryValue, ModelMap map) {
		map.addAttribute("ddCode", dataDictionaryValue.getDdCode());
		return "dataDictionaryValue/list";
	}

	/**
	 * 分页查询DataDictionaryValue
	 * 
	 * @param dataDictionaryValue
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询DataDictionaryValue", notes = "分页查询DataDictionaryValue，返回easyui分页信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(DataDictionaryValue dataDictionaryValue) throws Exception {
		@SuppressWarnings("unchecked")
		Map<Object, Object> metadata = null == dataDictionaryValue.getMetadata() ? new HashMap<>() : dataDictionaryValue.getMetadata();

		metadata.put("firmCode", "firmCodeProvider");
		dataDictionaryValue.setMetadata(metadata);

		return dataDictionaryValueService.listEasyuiPageByExample(dataDictionaryValue, true).toString();
	}

	/**
	 * 新增DataDictionaryValue
	 * 
	 * @param dataDictionaryValue
	 * @return
	 */
	@ApiOperation("新增DataDictionaryValue")
	@ApiImplicitParams({ @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = true, dataType = "string") })
	@BusinessLogger(businessType = "data_dictionary_value", content = "新增数据字典值：dd_code:${ddCode}", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(DataDictionaryValue dataDictionaryValue) {
		BaseOutput output = dataDictionaryValueService.insertAfterCheck(dataDictionaryValue);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, dataDictionaryValue.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, dataDictionaryValue.getId());
			LoggerContext.put("ddCode", dataDictionaryValue.getDdCode());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 修改DataDictionaryValue
	 * 
	 * @param dataDictionaryValue
	 * @return
	 */
	@ApiOperation("修改DataDictionaryValue")
	@ApiImplicitParams({ @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = true, dataType = "string") })
	@BusinessLogger(businessType = "data_dictionary_value", content = "修改数据字典值：dd_code:${ddCode}", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(DataDictionaryValue dataDictionaryValue) {
		BaseOutput output = dataDictionaryValueService.updateAfterCheck(dataDictionaryValue);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, dataDictionaryValue.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, dataDictionaryValue.getId());
			LoggerContext.put("ddCode", dataDictionaryValue.getDdCode());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 删除DataDictionaryValue
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation("删除DataDictionaryValue")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "form", value = "DataDictionaryValue的主键", required = true, dataType = "long") })
	@BusinessLogger(businessType = "data_dictionary_value", content = "删除数据字典值：dd_code:${ddCode}", operationType = "del", systemCode = "UAP")
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		DataDictionaryValue dataDictionaryValue = this.dataDictionaryValueService.get(id);
		dataDictionaryValueService.delete(id);
		BaseOutput output = BaseOutput.success("删除成功");
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, dataDictionaryValue.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, dataDictionaryValue.getId());
			LoggerContext.put("ddCode", dataDictionaryValue.getDdCode());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}
}