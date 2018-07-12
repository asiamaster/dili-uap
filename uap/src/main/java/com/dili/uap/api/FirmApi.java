package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.service.FirmService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Api("/firmApi")
@Controller
@RequestMapping("/firmApi")
public class FirmApi {
	@Autowired
	FirmService firmService;

	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Firm>> listByExample(Firm firm) {
		return BaseOutput.success().setData(firmService.listByExample(firm));
	}

	@ResponseBody
	@RequestMapping(value = "/getByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Firm> getByCode(@RequestBody String code) {
		Firm firm  = DTOUtils.newDTO(Firm.class);
		firm.setCode(code);
		List<Firm> firms = firmService.list(firm);
		if(firms == null || firms.isEmpty()){
			return BaseOutput.success().setData(Lists.newArrayList());
		}
		return BaseOutput.success().setData(firms.get(0));
	}

}