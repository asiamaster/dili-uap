package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.oplog.annotation.OpLog;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.service.FirmService;
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
 * This file was generated on 2019-04-09 14:35:13.
 */
@Api("/firm")
@Controller
@RequestMapping("/firm")
public class FirmController {
    @Autowired
    FirmService firmService;

    /**
     * 跳转到Firm页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到Firm页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "firm/index";
    }

    /**
     * 查询Firm
     * @param firm
     * @return
     */
    @ApiOperation(value="查询Firm", notes = "查询Firm，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Firm", paramType="form", value = "Firm的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Firm list(Firm firm) {
        return firm;
    }

    /**
     * 分页查询Firm
     * @param firm
     * @return
     * @throws Exception
     */
    @ApiOperation(value="分页查询Firm", notes = "分页查询Firm，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Firm", paramType="form", value = "Firm的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Firm firm) throws Exception {
        return firmService.listEasyuiPageByExample(firm, true).toString();
    }

    /**
     * 新增Firm
     * @param firm
     * @return
     */
    @ApiOperation("新增Firm")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Firm", paramType="form", value = "Firm的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Firm firm) {
        firmService.insertSelective(firm);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改Firm
     * @param firm
     * @return
     */
    @ApiOperation("修改Firm")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Firm", paramType="form", value = "Firm的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @OpLog(contentProvider = "updateLogContentProvider")
    public @ResponseBody BaseOutput update(Firm firm) {
        firmService.updateSelective(firm);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除Firm
     * @param id
     * @return
     */
    @ApiOperation("删除Firm")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Firm的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        firmService.delete(id);
        return BaseOutput.success("删除成功");
    }
}