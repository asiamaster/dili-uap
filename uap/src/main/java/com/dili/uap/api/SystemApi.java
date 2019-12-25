package com.dili.uap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.SystemDto;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.service.SystemService;

import io.swagger.annotations.Api;

/**
 * 系统api
 * 
 * @author jiang
 *
 */
@Api("/systemApi")
@RestController
@RequestMapping("/systemApi")
public class SystemApi {

	@Autowired
	private SystemService systemService;

	@RequestMapping("/list.api")
	public BaseOutput<List<Systems>> list(SystemDto systems) {
		List<Systems> list = this.systemService.listByExample(systems);
		return BaseOutput.success().setData(list);
	}
}
