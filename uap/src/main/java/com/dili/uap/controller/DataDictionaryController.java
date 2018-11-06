package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.DataDictionaryDto;
import com.dili.uap.provider.SystemProvider;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.service.DataDictionaryService;
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

import java.util.Collections;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:39:45.
 */
@Api("/dataDictionary")
@Controller
@RequestMapping("/dataDictionary")
public class DataDictionaryController {
    @Autowired
    DataDictionaryService dataDictionaryService;
    @Autowired
    SystemProvider systemProvider;

    @ApiOperation("跳转到DataDictionary页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "dataDictionary/index";
    }

    @ApiOperation(value = "查询DataDictionary", notes = "查询DataDictionary，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionary", paramType = "form", value = "DataDictionary的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<DataDictionary> list(DataDictionary dataDictionary) {
        return dataDictionaryService.list(dataDictionary);
    }

    @ApiOperation(value = "分页查询DataDictionary", notes = "分页查询DataDictionary，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionary", paramType = "form", value = "DataDictionary的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(DataDictionaryDto dataDictionary) {
        try {
            return dataDictionaryService.listEasyuiPageByExample(dataDictionary, true).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation("新增DataDictionary")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionary", paramType = "form", value = "DataDictionary的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(DataDictionary dataDictionary) {
    	 return  dataDictionaryService.insertAfterCheck(dataDictionary);
    }

    @ApiOperation("修改DataDictionary")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DataDictionary", paramType = "form", value = "DataDictionary的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(DataDictionary dataDictionary) {
       return dataDictionaryService.updateAfterCheck(dataDictionary);
    }

    @ApiOperation("删除DataDictionary")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "DataDictionary的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
    	return dataDictionaryService.deleteAfterCheck(id);
    }
    @RequestMapping(value = "/systemList.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    Object systemList() {
    	 return  systemProvider.getLookupList(null, Collections.emptyMap(), null);
    }
}