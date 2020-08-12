package com.dili.uap.api;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.dto.DataDictionaryDto;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.service.DataDictionaryService;
import com.dili.uap.service.DataDictionaryValueService;
import com.dili.uap.service.FirmService;

/**
 * 数据字典接口
 */
@Controller
@RequestMapping("/dataDictionaryApi")
public class DataDictionaryApi {
	@Autowired
	DataDictionaryValueService dataDictionaryValueService;
	@Autowired
	DataDictionaryService dataDictionaryService;
	@Autowired
	private FirmService firmService;

	/**
	 * 查询数据字典值
	 * 
	 * @param dataDictionaryValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> list(DataDictionaryValue dataDictionaryValue) {
		// 默认按orderNumber排序
		if (dataDictionaryValue.getOrderNumber() != null) {
			dataDictionaryValue.setOrder("orderNumber");
		}
		return BaseOutput.success().setData(this.dataDictionaryValueService.listByExample(dataDictionaryValue));
	}

	/**
	 * 查询数据字典
	 * 
	 * @param dataDictionaryValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listDataDictionaryValue.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> listDataDictionaryValue(DataDictionaryValue dataDictionaryValue) {
		return this.list(dataDictionaryValue);
	}

	/**
	 * 查询数据字典
	 * 
	 * @param dataDictionaryValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listDataDictionaryValueWithFirm.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> listDataDictionaryValueWithFirm(DataDictionaryValue dataDictionaryValue) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(dataDictionaryValue.getFirmCode())) {
			sb.append("(firm_code in ('").append(dataDictionaryValue.getFirmCode()).append("','").append(UapConstants.GROUP_CODE).append("') or firm_code is null)");
			dataDictionaryValue.setFirmCode(null);
		}
		if (dataDictionaryValue.getFirmId() != null) {
			if (StringUtils.isNotBlank(sb)) {
				sb.append(" and ");
			}
			Firm firm = this.firmService.getIdByCode(UapConstants.GROUP_CODE);
			sb.append("(firm_id in (").append(firm.getId()).append(",").append(firm.getId()).append(") or firm_id is null)");
			dataDictionaryValue.setFirmId(null);
		}
		if (StringUtils.isNotBlank(sb)) {
			dataDictionaryValue.setMetadata(IDTO.AND_CONDITION_EXPR, sb.toString());
		}
		return BaseOutput.success().setData(this.dataDictionaryValueService.listByExample(dataDictionaryValue));
	}

	/**
	 * 根据数据字典编码查询数据字典值
	 * 
	 * @param ddCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listDataDictionaryValueByDdCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> listDataDictionaryValueByDdCode(@RequestParam("ddCode") String ddCode) {
		DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
		dataDictionaryValue.setDdCode(ddCode);
		// 默认按orderNumber排序
		if (dataDictionaryValue.getOrderNumber() != null) {
			dataDictionaryValue.setOrder("orderNumber");
		}
		return BaseOutput.success().setData(this.dataDictionaryValueService.listByExample(dataDictionaryValue));
	}

	/**
	 * 查询数据字典与值的集合
	 * 
	 * @param json
	 * @return DataDictionaryDto
	 */
	@ResponseBody
	@RequestMapping(value = "/findByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<DataDictionaryDto> findByCode(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		return BaseOutput.success().setData(this.dataDictionaryValueService.findByCode(jo.getString("code"), jo.getString("systemCode")));
	}

	/**
	 * 查询数据字典集合
	 * 
	 * @param dataDictionary
	 * @return List<DataDictionary>
	 */
	@ResponseBody
	@RequestMapping(value = "/listDataDictionary.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionary>> listDataDictionary(DataDictionary dataDictionary) {
		return BaseOutput.success().setData(this.dataDictionaryService.listByExample(dataDictionary));
	}

	/**
	 * 添加数据字典与值的集合
	 * 
	 * @param dataDictionaryDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertDataDictionaryDto.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Object> insertDataDictionaryDto(@RequestBody DataDictionaryDto dataDictionaryDto) {
		return BaseOutput.success().setData(this.dataDictionaryValueService.insertDataDictionaryDto(dataDictionaryDto));
	}
}