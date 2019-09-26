package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginLogDto;
import com.dili.uap.service.LoginLogService;
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
 * This file was generated on 2018-05-22 15:30:02.
 */
@Api("/loginLog")
@Controller
@RequestMapping("/loginLog")
public class LoginLogController {
    @Autowired
    LoginLogService loginLogService;

    /**
     * 跳转到LoginLog页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到LoginLog页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "loginLog/index";
    }

    /**
     * 查询LoginLog
     * @param loginLog
     * @return
     */
    @ApiOperation(value="查询LoginLog", notes = "查询LoginLog，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LoginLog", paramType="form", value = "LoginLog的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<LoginLog> list(LoginLog loginLog) {
        return loginLogService.list(loginLog);
    }

    /**
     * 分页查询LoginLog
     * @param loginLog
     * @return
     * @throws Exception
     */
    @ApiOperation(value="分页查询LoginLog", notes = "分页查询LoginLog，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LoginLog", paramType="form", value = "LoginLog的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(LoginLogDto loginLog) throws Exception {
    	EasyuiPageOutput output=loginLogService.findByLoginLogDto(loginLog, true);
        return output.toString();
    }

    /**
     * 新增LoginLog
     * @param loginLog
     * @return
     */
    @ApiOperation("新增LoginLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LoginLog", paramType="form", value = "LoginLog的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(LoginLog loginLog) {
        loginLogService.insertSelective(loginLog);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改LoginLog
     * @param loginLog
     * @return
     */
    @ApiOperation("修改LoginLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LoginLog", paramType="form", value = "LoginLog的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(LoginLog loginLog) {
        loginLogService.updateSelective(loginLog);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除LoginLog
     * @param id
     * @return
     */
    @ApiOperation("删除LoginLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "LoginLog的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        loginLogService.delete(id);
        return BaseOutput.success("删除成功");
    }

}