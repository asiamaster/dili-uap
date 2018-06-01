package com.dili.uap.controller;

import com.alibaba.fastjson.JSONArray;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.domain.dto.UserRoleDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.PinYinUtil;
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

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    FirmService firmService;

    @ApiOperation("跳转到User页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        String firmCode = SessionContext.getSessionContext().getUserTicket().getFirmCode();
        //用户是否属于集团
        Boolean isGroup = false;
        Firm query = DTOUtils.newDTO(Firm.class);
        if (UapConstants.GROUP_CODE.equals(firmCode)) {
            isGroup = true;
        } else {
            query.setCode(firmCode);
        }
        modelMap.put("firms", firmService.list(query));
        modelMap.put("isGroup", isGroup);
        modelMap.put("firmCode",firmCode);
        modelMap.put("defaultPass",UapConstants.DEFAULT_PASS);
        return "user/index";
    }

    @ApiOperation(value = "查询User", notes = "查询User，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<User> list(User user) {
        return userService.list(user);
    }

    @ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listPage(UserDto user) throws Exception {
        return userService.listEasyuiPage(user, true).toString();
    }

    @ApiOperation("新增User")
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput insert(User user) {
        return userService.save(user);
    }

    @ApiOperation("修改User")
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput update(User user) {
        return userService.save(user);
    }

    @ApiOperation("删除User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput delete(Long id) {
        userService.delete(id);
        return BaseOutput.success("删除成功");
    }

    @ApiOperation(value = "根据角色id查询User", notes = "根据角色id查询User，返回列表信息")
    @RequestMapping(value = "/findUserByRole.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String findUserByRole(Long roleId) {
        List<User> retList = userService.findUserByRole(roleId);
        return new EasyuiPageOutput(retList.size(), retList).toString();
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/changePwd.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput changePwd(UserDto user) {
    	Long userId = SessionContext.getSessionContext().getUserTicket().getId();
       return userService.changePwd(userId,user);
    }

    @ApiOperation(value = "根据姓名转换邮箱信息", notes = "根据姓名转换邮箱信息")
    @RequestMapping(value = "/getEmailByName.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput getEmailByName(String name) {
        return BaseOutput.success().setData(PinYinUtil.getFullPinYin(name.replace(" ","")) + UapConstants.EMAIL_POSTFIX);
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPass.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput resetPass(Long id) {
        return userService.resetPass(id);
    }

    @ApiOperation(value = "用户的禁启用", notes = "用户的禁启用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "用户ID",dataType = "long"),
            @ApiImplicitParam(name = "enable", paramType = "path", value = "是否启用(true-启用)",dataType = "boolean")
    })
    @RequestMapping(value = "/doEnable.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doEnable(Long id,Boolean enable){
         return userService.upateEnable(id,enable);
    }

    @ApiOperation(value = "获取用户的角色信息", notes = "获取用户角色信息(tree结构)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "用户ID",dataType = "long"),
    })
    @RequestMapping(value = "/getUserRolesForTree.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public  String getUserRolesForTree(Long id){
        List list = userService.getUserRolesForTree(id);
        return JSONArray.toJSONString(list);
    }


    @ApiOperation(value = "获取用户的角色信息", notes = "获取用户角色信息(tree结构)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "path", value = "用户ID",dataType = "long"),
            @ApiImplicitParam(name = "roleIds", paramType = "path", value = "角色ID(包括市场ID)",dataType = "String"),
    })
    @RequestMapping(value = "/saveUserRoles.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput saveUserRoles(Long userId,String roleIds[]){
        return userService.saveUserRoles(userId,roleIds);
    }
    

    @ApiOperation(value = "获取当前登录用户的信息", notes = "获取用户信息")
    @ApiImplicitParams({})
    @RequestMapping(value = "/fetchLoginUserInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Object> fetchLoginUserInfo(){
    	Long userId = SessionContext.getSessionContext().getUserTicket().getId();
         return userService.fetchLoginUserInfo(userId);
    }
    
}