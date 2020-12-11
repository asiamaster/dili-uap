package com.dili.uap.controller;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.dto.DataDictionaryDto;
import com.dili.uap.provider.SystemProvider;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.DataDictionaryLevel;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-21 10:39:45.
 */
@Controller
@RequestMapping("/dataDictionary")
public class DataDictionaryController {
	@Autowired
	DataDictionaryService dataDictionaryService;
	@Autowired
	SystemProvider systemProvider;

	/**
	 * 跳转到DataDictionary页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@GetMapping(value = "/index.html")
	public String index(ModelMap modelMap) {
		modelMap.addAttribute("firmCode", SessionContext.getSessionContext().getUserTicket().getFirmCode());
		return "dataDictionary/index";
	}

	/**
	 * 查询DataDictionary
	 * 
	 * @param dataDictionary
	 * @return
	 */
	@PostMapping(value = "/list")
	public @ResponseBody List<DataDictionary> list(DataDictionary dataDictionary) {
		return dataDictionaryService.list(dataDictionary);
	}

	/**
	 * 分页查询DataDictionary
	 * 
	 * @param dataDictionary
	 * @return
	 */
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(DataDictionaryDto dataDictionary) {
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (!user.getFirmCode().equals(UapConstants.GROUP_CODE)) {
			dataDictionary.setLevel(DataDictionaryLevel.BUSINESS.getValue());
		}
		try {
			return dataDictionaryService.listEasyuiPageByExample(dataDictionary, true).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新增DataDictionary
	 * 
	 * @param dataDictionary
	 * @return
	 */
	@BusinessLogger(businessType = "data_dictionary", content = "新增数据字典", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(DataDictionary dataDictionary) {
		BaseOutput output = dataDictionaryService.insertAfterCheck(dataDictionary);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, dataDictionary.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, dataDictionary.getId());
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
	 * 修改DataDictionary
	 * 
	 * @param dataDictionary
	 * @return
	 */
	@BusinessLogger(businessType = "data_dictionary", content = "修改数据字典", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(DataDictionary dataDictionary) {
		BaseOutput output = dataDictionaryService.updateAfterCheck(dataDictionary);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, dataDictionary.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, dataDictionary.getId());
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
	 * 删除DataDictionary
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "data_dictionary", content = "删除数据字典", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		DataDictionary dataDictionary = this.dataDictionaryService.get(id);
		BaseOutput output = dataDictionaryService.deleteAfterCheck(id);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, dataDictionary.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, dataDictionary.getId());
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
	 * 系统列表查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/systemList.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object systemList() {
		return systemProvider.getLookupList(null, Collections.emptyMap(), null);
	}
}