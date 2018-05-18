package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Role;
import com.dili.uap.service.RoleService;
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
 * This file was generated on 2018-05-18 11:45:41.
 */
@Api("/role")
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @ApiOperation("跳转到Role页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "role/index";
    }

    @ApiOperation(value="查询Role", notes = "查询Role，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Role> list(@ModelAttribute Role role) {
        return roleService.list(role);
    }

    @ApiOperation(value="分页查询Role", notes = "分页查询Role，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Role role) throws Exception {
        return roleService.listEasyuiPageByExample(role, true).toString();
    }

    @ApiOperation("新增Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute Role role) {
        roleService.insertSelective(role);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute Role role) {
        roleService.updateSelective(role);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Role的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        roleService.delete(id);
        return BaseOutput.success("删除成功");
    }
}