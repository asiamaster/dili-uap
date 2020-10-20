package com.dili.uap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.dto.UserResourceQueryDto;
import com.dili.uap.service.ResourceService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-12-30 16:56:50.
 */
@Controller
@RequestMapping("/resourceApi")
public class ResourceApi {

	@Autowired
	private ResourceService resourceService;

	/**
	 * 查询权限信息名称集合
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/listResourceCodeByUserId.api")
	@ResponseBody
	public BaseOutput<List<String>> listResourceCodeByUserId(@RequestBody Long userId) {
		return BaseOutput.success().setData(resourceService.listResourceCodeByUserId(userId));
	}

	/**
	 * 根据菜单URL和用户id，查询有权限的资源
	 * 
	 * @param url
	 * @return
	 */
	@GetMapping(value = "/listResourceCodeByMenuUrl.api")
	@ResponseBody
	public BaseOutput<List<String>> listResourceCodeByMenuUrl(@RequestParam(name = "url") String url, @RequestParam(name = "userId") Long userId) {
		return BaseOutput.success().setData(resourceService.listResourceCodeByMenuUrl(url, userId));
	}

	/**
	 * 根据权限码和用户id，查询有权限的资源
	 * 
	 * @param url
	 * @return
	 */
	@PostMapping(value = "/listResourceCodesByUserId.api")
	@ResponseBody
	public BaseOutput<List<String>> listResourceCodesByUserId(UserResourceQueryDto dto) {
		return BaseOutput.success().setData(resourceService.listResourceCodesByUserId(dto.getUserId(), dto.getResourceCodes()));
	}
}
