package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.Resource;
import com.dili.uap.service.ResourceService;
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

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:46:27.
 */
@Api("/resource")
@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    @ApiOperation("跳转到Resource页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "resource/index";
    }

    @ApiOperation(value = "查询资源列表", notes = "查询Menu，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", paramType = "Long", value = "menuId", required = false, dataType = "Long") })
    @RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody List<Resource> list(@RequestParam String menuId) {
        Resource query = DTOUtils.newDTO(Resource.class);
        if(menuId.startsWith("menu_")){
            query.setMenuId(Long.parseLong(menuId.substring(5)));
        }
        query.setSort("id");
        query.setOrder("asc");
        return this.resourceService.listByExample(query);
    }


    @ApiOperation("新增Resource")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Resource resource) {
        String menuId = resource.aget("menuId").toString();
        if(menuId.startsWith("menu_")){
            //如果菜单树上点的节点是菜单， 需要设置当前节点的父节点
            resource.setMenuId(Long.parseLong(menuId.substring(5)));
        }
        resourceService.insertSelective(resource);
        return BaseOutput.success("新增成功").setData(resource.getId());
    }

    @ApiOperation("修改Resource")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Resource resource) {
        String menuId = resource.aget("menuId").toString();
        if(menuId.startsWith("menu_")){
            //如果菜单树上点的节点是菜单， 需要设置当前节点的父节点
            resource.setMenuId(Long.parseLong(menuId.substring(5)));
        }
        resourceService.updateSelective(resource);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Resource")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Resource的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        resourceService.delete(id);
        return BaseOutput.success("删除成功");
    }
}