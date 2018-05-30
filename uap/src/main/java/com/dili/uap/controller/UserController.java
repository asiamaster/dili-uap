package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.PinYinUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

    private static final String defaultPass = "123456";

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
        modelMap.put("defaultPass",defaultPass);
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
        if (StringUtils.isNotBlank(user.getKeywords())) {
            user.mset(IDTO.AND_CONDITION_EXPR, " (user_name =" + user.getKeywords().trim() + " or cellphone=" + user.getKeywords().trim() + ")");
        }
        return userService.listEasyuiPageByExample(user, true).toString();
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

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/getEmailByName.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput getEmailByName(String name) {
        return BaseOutput.success().setData(PinYinUtil.getFullPinYin(name.replace(" ","")) + UapConstants.EMAIL_POSTFIX);
    }
}