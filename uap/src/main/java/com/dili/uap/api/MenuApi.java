package com.dili.uap.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.service.MenuService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@RestController
@RequestMapping("/menuApi")
public class MenuApi {
	@Autowired
	private MenuService menuService;

	/**
	 * 查询menu实体接口
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/get.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput<Menu> get(@RequestBody Long id) {
		return BaseOutput.success().setData(menuService.get(id));
	}

	/**
	 * 查询menu列表接口
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/list.api", method = { RequestMethod.POST })
	public @ResponseBody BaseOutput<List<Menu>> list(@RequestBody(required = false) Menu menu) {
		return BaseOutput.success().setData(menuService.list(menu));
	}

	/**
	 * 根据URL查询菜单明细
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMenuDetailByUrl.api", method = {RequestMethod.POST })
	public BaseOutput<Map<String, Object>> getMenuDetailByUrl(@RequestBody String url) {
		return BaseOutput.success().setData(this.menuService.getMenuDetailByUrl(url));
	}

	/**
	 * 查询菜单
	 * @param menu
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.POST })
	public BaseOutput<Menu> listByExample(@RequestBody(required = false) Menu menu) {
		return BaseOutput.success().setData(this.menuService.listByExample(menu));
	}

	/**
	 * 根据URL获取父菜单
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getParentMenusByUrl.api", method = { RequestMethod.POST })
	public BaseOutput<List<Menu>> getParentMenusByUrl(@RequestBody String url) {
		return BaseOutput.success().setData(this.menuService.getParentMenusByUrl(url));
	}

}