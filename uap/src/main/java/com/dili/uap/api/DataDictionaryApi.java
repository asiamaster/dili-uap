package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.VOField;
import com.dili.uap.domain.dto.DataDictionaryDto;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.service.DataDictionaryService;
import com.dili.uap.service.DataDictionaryValueService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据字典接口
 */
@Api("/dataDictionaryApi")
@Controller
@RequestMapping("/dataDictionaryApi")
public class DataDictionaryApi {
	@Autowired
	DataDictionaryValueService dataDictionaryValueService;
	@Autowired
	DataDictionaryService dataDictionaryService;
	/**
	 * 查询数据字典
	 * @param dataDictionaryValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> list(DataDictionaryValue dataDictionaryValue) {
		return BaseOutput.success().setData(this.dataDictionaryValueService.listByExample(dataDictionaryValue));
	}
	/**
	 * 查询数据字典
	 * @param dataDictionaryValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listDataDictionaryValue.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> listDataDictionaryValue(DataDictionaryValue dataDictionaryValue) {
		return BaseOutput.success().setData(this.dataDictionaryValueService.listByExample(dataDictionaryValue));
	}
	
	/**
	 * 查询数据字典与值的集合
	 * @param dataDictionaryValue
	 * @return DataDictionaryDto
	 */
	@ResponseBody
	@RequestMapping(value = "/findByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<DataDictionaryDto> findByCode(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		return BaseOutput.success().setData(this.dataDictionaryValueService.findByCode(jo.getString("code"),jo.getString("systemCode")));
	}
	/**
	 * 查询数据字典集合
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
	 * @param dataDictionaryDto
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/insertDataDictionaryDto.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Object> insertDataDictionaryDto(@RequestBody DataDictionaryDto dataDictionaryDto) {
		return BaseOutput.success().setData(this.dataDictionaryValueService.insertDataDictionaryDto(dataDictionaryDto));
	}
}