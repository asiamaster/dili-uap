package com.dili.uap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.component.DataAuthSource;
import com.dili.uap.sdk.domain.DataAuthRef;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.dili.uap.service.DataAuthRefService;
import com.google.common.collect.Lists;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Controller
@RequestMapping("/dataAuthRefApi")
public class DataAuthRefApi {
	@Autowired
	private DataAuthRefService dataAuthRefService;

	@Autowired
	private DataAuthSource dataAuthSource;

	/**
	 * 根据编码查询资源
	 * @param refCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listSourcesByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List> listSourcesByCode(@RequestBody String refCode) {
		DataAuthRef dataAuthRef  = DTOUtils.newInstance(DataAuthRef.class);
		dataAuthRef.setCode(refCode);
		List<DataAuthRef> dataAuthRefs = dataAuthRefService.list(dataAuthRef);
		if(dataAuthRefs == null || dataAuthRefs.isEmpty()){
			return BaseOutput.success().setData(Lists.newArrayList());
		}
		String springId = dataAuthRefs.get(0).getSpringId();
		DataAuthSourceService dataAuthSourceService = dataAuthSource.getDataAuthSourceServiceMap().get(springId);
		//如果不是内部的数据源，只能返回空，调用端只能自己查询
		if(dataAuthSourceService == null){
			return BaseOutput.success().setData(Lists.newArrayList());
		}
		return BaseOutput.success().setData(dataAuthSourceService.listDataAuthes(dataAuthRefs.get(0).getParam()));
	}

}