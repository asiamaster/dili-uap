package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.Menu;
import com.dili.uap.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
@Api("/menu")
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    @ApiOperation("跳转到Menu页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "menu/index";
    }

    @RequestMapping(value = "/listSystemMenu.action", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Map> listSystemMenu() {
        return this.menuService.listSystemMenu();
    }

    @ApiOperation(value = "查询Menu", notes = "查询Menu，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", paramType = "Long", value = "menu parent_id", required = false, dataType = "Long") })
    @RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody List<Menu> list(@RequestParam String parentId) {
        Long parentIdLong = null;
        if(parentId.startsWith("menu_")){
            parentIdLong = Long.parseLong(parentId.substring(5));
        }else if(parentId.startsWith("sys_")){
            parentIdLong = Long.parseLong(parentId.substring(4));
        }else{
            parentIdLong = Long.parseLong(parentId);
        }
        Menu query = DTOUtils.newDTO(Menu.class);
        query.setParentId(parentIdLong);
        return this.menuService.list(query);
    }

    @ApiOperation("新增Menu")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Menu", paramType="form", value = "Menu的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Menu menu) {
        menuService.insertSelective(menu);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Menu")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Menu", paramType="form", value = "Menu的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Menu menu) {
        menuService.updateSelective(menu);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Menu")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Menu的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        menuService.delete(id);
        return BaseOutput.success("删除成功");
    }
}