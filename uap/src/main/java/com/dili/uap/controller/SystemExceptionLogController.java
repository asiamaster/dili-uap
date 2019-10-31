package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.SystemExceptionLogDto;
import com.dili.uap.sdk.domain.SystemExceptionLog;
import com.dili.uap.service.SystemExceptionLogService;
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

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 15:27:04.
 */
@Api("/systemExceptionLog")
@Controller
@RequestMapping("/systemExceptionLog")
public class SystemExceptionLogController{
    @Autowired
    SystemExceptionLogService systemExceptionLogService;

    /**
     * 跳转到SystemExceptionLog页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到SystemExceptionLog页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "systemExceptionLog/index";
    }

    /**
     * 查询SystemExceptionLog
     * @param systemExceptionLog
     * @return
     */
    @ApiOperation(value="查询SystemExceptionLog", notes = "查询SystemExceptionLog，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemExceptionLog", paramType="form", value = "SystemExceptionLog的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<SystemExceptionLog> list(SystemExceptionLog systemExceptionLog) {
        return systemExceptionLogService.list(systemExceptionLog);
    }

    /**
     * 分页查询SystemExceptionLog
     * @param systemExceptionLog
     * @return
     * @throws Exception
     */
    @ApiOperation(value="分页查询SystemExceptionLog", notes = "分页查询SystemExceptionLog，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemExceptionLog", paramType="form", value = "SystemExceptionLog的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(SystemExceptionLogDto systemExceptionLog) throws Exception {
        return systemExceptionLogService.listEasyuiPageByExample(systemExceptionLog, true).toString();
    }

    /**
     * 新增SystemExceptionLog
     * @param systemExceptionLog
     * @return
     */
    @ApiOperation("新增SystemExceptionLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemExceptionLog", paramType="form", value = "SystemExceptionLog的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(SystemExceptionLog systemExceptionLog) {
        systemExceptionLogService.insertSelective(systemExceptionLog);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改SystemExceptionLog
     * @param systemExceptionLog
     * @return
     */
    @ApiOperation("修改SystemExceptionLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemExceptionLog", paramType="form", value = "SystemExceptionLog的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(SystemExceptionLog systemExceptionLog) {
        systemExceptionLogService.updateSelective(systemExceptionLog);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除SystemExceptionLog
     * @param id
     * @return
     */
    @ApiOperation("删除SystemExceptionLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "SystemExceptionLog的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        systemExceptionLogService.delete(id);
        return BaseOutput.success("删除成功");
    }
}