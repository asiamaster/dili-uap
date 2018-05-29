package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.UserService;
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
 * This file was generated on 2018-05-23 16:17:38.
 */
@Api("/user")
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("跳转到User页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "user/index";
    }

    @ApiOperation(value = "查询User", notes = "查询User，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<User> list(User user) {
        return userService.list(user);
    }

    @ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(User user) throws Exception {
        return userService.listEasyuiPageByExample(user, true).toString();
    }

    @ApiOperation("新增User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(User user) {
        userService.insertSelective(user);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(User user) {
        userService.updateSelective(user);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        userService.delete(id);
        return BaseOutput.success("删除成功");
    }

    @ApiOperation(value = "根据角色id查询User", notes = "根据角色id查询User，返回列表信息")
    @RequestMapping(value = "/findUserByRole.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String findUserByRole(Long roleId) {
        List<User> retList = userService.findUserByRole(roleId);
        return new EasyuiPageOutput(retList.size(), retList).toString();
    }
    
    @ApiOperation(value = "根据角色id查询User", notes = "根据角色id查询User，返回列表信息")
    @RequestMapping(value = "/changePwd.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput changePwd(UserDto user) {
    	Long userId = SessionContext.getSessionContext().getUserTicket().getId();
       return userService.changePwd(userId,user);
    }
}