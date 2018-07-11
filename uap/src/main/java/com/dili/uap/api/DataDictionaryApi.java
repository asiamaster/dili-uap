package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.Department;
import com.dili.uap.sdk.domain.DataDictionaryValue;
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

	@ResponseBody
	@RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<DataDictionaryValue>> list(DataDictionaryValue dataDictionaryValue) {
		return BaseOutput.success().setData(this.dataDictionaryValueService.listByExample(dataDictionaryValue));
	}
}