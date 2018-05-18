package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.UserRole;
import com.dili.uap.service.UserRoleService;
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
 * This file was generated on 2018-05-18 11:48:16.
 */
@Api("/userRole")
@Controller
@RequestMapping("/userRole")
public class UserRoleController {
    @Autowired
    UserRoleService userRoleService;

    @ApiOperation("跳转到UserRole页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "userRole/index";
    }

    @ApiOperation(value="查询UserRole", notes = "查询UserRole，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UserRole", paramType="form", value = "UserRole的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<UserRole> list(@ModelAttribute UserRole userRole) {
        return userRoleService.list(userRole);
    }

    @ApiOperation(value="分页查询UserRole", notes = "分页查询UserRole，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UserRole", paramType="form", value = "UserRole的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute UserRole userRole) throws Exception {
        return userRoleService.listEasyuiPageByExample(userRole, true).toString();
    }

    @ApiOperation("新增UserRole")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UserRole", paramType="form", value = "UserRole的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute UserRole userRole) {
        userRoleService.insertSelective(userRole);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改UserRole")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UserRole", paramType="form", value = "UserRole的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute UserRole userRole) {
        userRoleService.updateSelective(userRole);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除UserRole")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "UserRole的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        userRoleService.delete(id);
        return BaseOutput.success("删除成功");
    }
}