package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.service.SystemConfigService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据字典接口
 */
@Api("/systemConfigApi")
@Controller
@RequestMapping("/systemConfigApi")
public class SystemConfigApi {
	@Autowired
	private SystemConfigService systemConfigService;

	@ResponseBody
	@RequestMapping(value = "/list.api", method = { RequestMethod.POST })
	public BaseOutput<List<SystemConfig>> list(SystemConfig systemConfig) {
		return BaseOutput.success().setData(this.systemConfigService.listByExample(systemConfig));
	}

	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate.api", method = { RequestMethod.POST })
	public BaseOutput saveOrUpdate(SystemConfig systemConfig) {
		return BaseOutput.success().setData(this.systemConfigService.saveOrUpdate(systemConfig));
	}

}