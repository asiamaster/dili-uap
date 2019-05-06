package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.ResourceLink;
import com.dili.uap.service.ResourceLinkService;
import com.dili.uap.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
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
 * This file was generated on 2018-05-21 16:46:27.
 */
@Api("/resource")
@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;
    @Autowired
    ResourceLinkService resourceLinkService;

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

    @RequestMapping(value = "/getName.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String getName(Resource resource) {
        List<Resource> resources = this.resourceService.list(resource);
        if(CollectionUtils.isEmpty(resources)){
            return null;
        }
        return resources.get(0).getName();
    }

    @ApiOperation(value = "查询资源列表", notes = "查询Menu，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", paramType = "Long", value = "menuId", required = false, dataType = "Long") })
    @RequestMapping(value = "/listResourceLink.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody List<Map> listResourceLink(ResourceLink resourceLink) throws Exception {
        List<ResourceLink> resourceLinks = this.resourceLinkService.list(resourceLink);
        return ValueProviderUtils.buildDataByProvider(resourceLink, resourceLinks);
    }

    @ApiOperation("新增ResourceLink")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/addResourceLink.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput addResourceLink(ResourceLink resourceLink) {
        List<ResourceLink> resourceLinks = resourceLinkService.list(resourceLink);
        if(resourceLinks.isEmpty()) {
            resourceLinkService.insertSelective(resourceLink);
            return BaseOutput.success("绑定资源链接成功").setData(resourceLink.getId());
        }else{
            return BaseOutput.success("已经绑定资源链接").setData(resourceLink.getId());
        }
    }

    @ApiOperation("删除ResourceLink")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/deleteResourceLink.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput deleteResourceLink(@RequestParam Long id) {
        resourceLinkService.delete(id);
        return BaseOutput.success("删除资源链接成功");
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
        //级联更新ResourceLink,先获取原始ResourceCode
        Resource resource1 = resourceService.get(resource.getId());
        ResourceLink resourceLinkCondition = DTOUtils.newDTO(ResourceLink.class);
        resourceLinkCondition.setResourceCode(resource1.getCode());
        resourceService.updateSelective(resource);
        ResourceLink resourceLink = DTOUtils.newDTO(ResourceLink.class);
        resourceLink.setResourceCode(resource.getCode());
        resourceLinkService.updateSelectiveByExample(resourceLink, resourceLinkCondition);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Resource")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", paramType="form", value = "Resource的主键", required = true, dataType = "long")
    })
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        //级联更新ResourceLink,先获取原始ResourceCode
        Resource resource = resourceService.get(id);
        resourceService.delete(id);
        ResourceLink resourceLinkCondition = DTOUtils.newDTO(ResourceLink.class);
        resourceLinkCondition.setResourceCode(resource.getCode());
        resourceLinkService.deleteByExample(resourceLinkCondition);
        return BaseOutput.success("删除成功");
    }
}