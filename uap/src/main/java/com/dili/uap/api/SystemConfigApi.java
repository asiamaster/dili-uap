package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.service.SystemConfigService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
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
@Api("/systemConfigApi")
@Controller
@RequestMapping("/systemConfigApi")
public class SystemConfigApi {
	@Autowired
	private SystemConfigService systemConfigService;

	/**
	 * 查询系统配置
	 * @param systemConfig
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.api", method = { RequestMethod.POST })
	public BaseOutput<List<SystemConfig>> list(SystemConfig systemConfig) {
		return BaseOutput.success().setData(this.systemConfigService.listByExample(systemConfig));
	}

	/**
	 * 保存系统配置
	 * @param systemConfig
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate.api", method = { RequestMethod.POST })
	public BaseOutput saveOrUpdate(SystemConfig systemConfig) {
		return BaseOutput.success().setData(this.systemConfigService.saveOrUpdate(systemConfig));
	}

	/**
	 * 根据编译获取系统配置
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getByCode.api", method = { RequestMethod.POST })
	public BaseOutput<SystemConfig> getByCode(@RequestBody String code) {
		if (StringUtils.isBlank(code)){
			return BaseOutput.success();
		}
		SystemConfig query = DTOUtils.newInstance(SystemConfig.class);
		query.setCode(code);
		return BaseOutput.success().setData(this.systemConfigService.list(query).stream().findFirst().orElseGet(() -> {
			return null;
		}));
	}

}