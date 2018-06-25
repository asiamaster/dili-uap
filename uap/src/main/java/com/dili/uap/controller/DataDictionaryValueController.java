package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.service.DataDictionaryValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:40:13.
 */
@Api("/dataDictionaryValue")
@Controller
@RequestMapping("/dataDictionaryValue")
public class DataDictionaryValueController {
    @Autowired
    DataDictionaryValueService dataDictionaryValueService;

    @ApiOperation("跳转到DataDictionaryValue页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "dataDictionaryValue/index";
    }

    @ApiOperation(value = "查询DataDictionaryValue", notes = "查询DataDictionaryValue，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(DataDictionaryValue dataDictionaryValue, ModelMap map) {
        map.addAttribute("ddCode", dataDictionaryValue.getDdCode());
        return "dataDictionaryValue/list";
    }

    @ApiOperation(value = "分页查询DataDictionaryValue", notes = "分页查询DataDictionaryValue，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(DataDictionaryValue dataDictionaryValue) throws Exception {
        return dataDictionaryValueService.listEasyuiPageByExample(dataDictionaryValue, true).toString();
    }

    @ApiOperation("新增DataDictionaryValue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(DataDictionaryValue dataDictionaryValue) {
    	return dataDictionaryValueService.insertAfterCheck(dataDictionaryValue);
    }

    @ApiOperation("修改DataDictionaryValue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionaryValue", paramType = "form", value = "DataDictionaryValue的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(DataDictionaryValue dataDictionaryValue) {
        return dataDictionaryValueService.updateAfterCheck(dataDictionaryValue);
    }

    @ApiOperation("删除DataDictionaryValue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "DataDictionaryValue的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        dataDictionaryValueService.delete(id);
        return BaseOutput.success("删除成功");
    }
}