package com.dili.uap.sdk.rpc;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOField;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.dto.UapDataDictionaryDto;

/**
 * 数据字典(值)接口 Created by asiam on 2018/7/10 0010.
 */
@Restful("${uap.contextPath}")
public interface DataDictionaryRpc {

	@POST("/dataDictionaryApi/listDataDictionaryValue.api")
	BaseOutput<List<DataDictionaryValue>> listDataDictionaryValue(@VOBody DataDictionaryValue dataDictionaryValue);

	@POST("/dataDictionaryApi/listDataDictionary.api")
	BaseOutput<List<DataDictionary>> listDataDictionary(@VOBody DataDictionary dataDictionary);

	@POST("/dataDictionaryApi/listDataDictionaryValueByDdCode.api")
	BaseOutput<List<DataDictionaryValue>> listDataDictionaryValueByDdCode(@ReqParam("ddCode") String ddCode);

	@POST("/dataDictionaryApi/findByCode.api")
	BaseOutput<UapDataDictionaryDto> findByCode(@VOField("code") String code, @VOField("systemCode") String systemCode);

	/**
	 * 添加数据字典值
	 * 
	 * @param dataDictionaryValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertDataDictionaryDto.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Object> insertDataDictionaryDto(@VOBody UapDataDictionaryDto dataDictionaryDto);
}
