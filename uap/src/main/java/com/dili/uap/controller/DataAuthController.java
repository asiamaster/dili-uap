package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.service.DataAuthService;
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
 * This file was generated on 2018-05-23 11:29:55.
 */
@Api("/dataAuth")
@Controller
@RequestMapping("/dataAuth")
public class DataAuthController {
    @Autowired
    DataAuthService dataAuthService;

    @ApiOperation("跳转到DataAuth页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "dataAuth/index";
    }

    @ApiOperation(value="查询DataAuth", notes = "查询DataAuth，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DataAuth", paramType="form", value = "DataAuth的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<DataAuth> list(DataAuth dataAuth) {
        return dataAuthService.list(dataAuth);
    }

    @ApiOperation(value="分页查询DataAuth", notes = "分页查询DataAuth，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DataAuth", paramType="form", value = "DataAuth的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(DataAuth dataAuth) throws Exception {
        return dataAuthService.listEasyuiPageByExample(dataAuth, true).toString();
    }

    @ApiOperation("新增DataAuth")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DataAuth", paramType="form", value = "DataAuth的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(DataAuth dataAuth) {
        dataAuthService.insertSelective(dataAuth);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改DataAuth")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DataAuth", paramType="form", value = "DataAuth的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(DataAuth dataAuth) {
        dataAuthService.updateSelective(dataAuth);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除DataAuth")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "DataAuth的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        dataAuthService.delete(id);
        return BaseOutput.success("删除成功");
    }
}