package com.dili.uap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.dto.SystemDto;
import com.dili.uap.service.SystemService;
import com.google.common.collect.Lists;

/**
 * 系统api
 * 
 * @author jiang
 *
 */
@RestController
@RequestMapping("/systemApi")
public class SystemApi {

	@Autowired
	private SystemService systemService;

	/**
	 * 系统查询api
	 * 
	 * @param systems 查询参数
	 * @return 结果集
	 */
	@RequestMapping("/list.api")
	@Deprecated
	public BaseOutput<List<Systems>> list(SystemDto systems) {
		List<Systems> list = this.systemService.listByExample(systems);
		return BaseOutput.success().setData(list);
	}

	/**
	 * 系统查询api
	 *
	 * @param systems 查询参数
	 * @return 结果集
	 */
	@RequestMapping("/listByExample.api")
	public BaseOutput<List<Systems>> listByExample(SystemDto systems) {
		return BaseOutput.success().setData(this.systemService.listByExample(systems));
	}

	/**
	 * 根据编码查询系统信息
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Systems> getByCode(@RequestBody String code) {
		Systems condition  = DTOUtils.newInstance(Systems.class);
		condition.setCode(code);
		List<Systems> systemsList = systemService.list(condition);
		if(systemsList == null || systemsList.isEmpty()){
			return BaseOutput.success().setData(Lists.newArrayList());
		}
		return BaseOutput.success().setData(systemsList.get(0));
	}
}
