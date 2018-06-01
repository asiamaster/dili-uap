package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.System;
import com.dili.uap.domain.dto.SystemDto;
import com.dili.uap.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:24:56.
 */
@Api("/system")
@Controller
@RequestMapping("/system")
public class SystemController {
    @Autowired
    SystemService systemService;

    @ApiOperation("跳转到System页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "system/index";
    }

    @ApiOperation(value="查询System", notes = "查询System，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="System", paramType="form", value = "System的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<System> list(System system) {
        return systemService.list(system);
    }

    @ApiOperation(value="分页查询System", notes = "分页查询System，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="System", paramType="form", value = "System的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(SystemDto system) throws Exception {
        return systemService.listEasyuiPageByExample(system, true).toString();
    }

    @ApiOperation("新增System")
    @ApiImplicitParams({
		@ApiImplicitParam(name="System", paramType="form", value = "System的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(System system) {
       return systemService.insertAfterCheck(system);
    }

    @ApiOperation("修改System")
    @ApiImplicitParams({
		@ApiImplicitParam(name="System", paramType="form", value = "System的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(System system) {
    	return systemService.updateAfterCheck(system);
    }

    @ApiOperation("删除System")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "System的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
       return systemService.deleteAfterCheck(id);
        
    }
}