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
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.service.FirmService;
import com.google.common.collect.Lists;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Controller
@RequestMapping("/firmApi")
public class FirmApi {
	@Autowired
	FirmService firmService;

	/**
	 * 查询公司
	 * @param firm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Firm>> listByExample(FirmDto firm) {
		return BaseOutput.success().setData(firmService.listByExample(firm));
	}

	/**
	 * 根据编码查询公司
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Firm> getByCode(@RequestBody String code) {
		Firm firm  = DTOUtils.newInstance(Firm.class);
		firm.setCode(code);
		List<Firm> firms = firmService.list(firm);
		if(firms == null || firms.isEmpty()){
			return BaseOutput.success().setData(Lists.newArrayList());
		}
		return BaseOutput.success().setData(firms.get(0));
	}

	/**
	 * 根据id查询公司
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getById.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Firm> getById(@RequestBody Long id) {
		return BaseOutput.success().setData(firmService.get(id));
	}

}