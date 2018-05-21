package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Resource;
import com.dili.uap.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ApiOperation(value="查询Resource", notes = "查询Resource，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Resource> list(@ModelAttribute Resource resource) {
        return resourceService.list(resource);
    }

    @ApiOperation(value="分页查询Resource", notes = "分页查询Resource，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Resource resource) throws Exception {
        return resourceService.listEasyuiPageByExample(resource, true).toString();
    }

    @ApiOperation("新增Resource")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute Resource resource) {
        resourceService.insertSelective(resource);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Resource")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Resource", paramType="form", value = "Resource的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute Resource resource) {
        resourceService.updateSelective(resource);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Resource")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Resource的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        resourceService.delete(id);
        return BaseOutput.success("删除成功");
    }
}