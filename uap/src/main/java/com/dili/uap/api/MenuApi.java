package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Menu;
import com.dili.uap.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Api("/menuApi")
@RestController
@RequestMapping("/menuApi")
public class MenuApi {
	@Autowired
	private MenuService menuService;

	@ApiOperation(value = "查询menu实体接口", notes = "根据id查询menu接口，返回menu实体")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "menu的id", required = true, dataType = "long") })
	@RequestMapping(value = "/get.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Menu> get(@RequestBody Long id) {
		return BaseOutput.success().setData(menuService.get(id));
	}

	@ApiOperation(value = "查询menu列表接口", notes = "查询menu列表接口，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "menu", paramType = "form", value = "menu的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<List<Menu>> list(@RequestBody(required = false) Menu menu) {
		return BaseOutput.success().setData(menuService.list(menu));
	}

	@ResponseBody
	@RequestMapping(value = "/getMenuDetailByUrl.api", method = {RequestMethod.POST })
	public BaseOutput<Map<String, Object>> getMenuDetailByUrl(@RequestBody String url) {
		return BaseOutput.success().setData(this.menuService.getMenuDetailByUrl(url));
	}

	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Menu> listByExample(@RequestBody(required = false) Menu menu) {
		return BaseOutput.success().setData(this.menuService.listByExample(menu));
	}

	@ResponseBody
	@RequestMapping(value = "/getParentMenusByUrl.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Menu>> getParentMenusByUrl(@RequestBody String url) {
		return BaseOutput.success().setData(this.menuService.getParentMenusByUrl(url));
	}

}