package com.dili.uap.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.UserDataAuth;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.service.UserDataAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据权限Api
 */
@Api("/dataAuthApi")
@Controller
@RequestMapping("/dataAuthApi")
public class DataAuthApi {

    @Autowired
    private UserDataAuthService userDataAuthService;

    @ApiOperation(value = "根据条件查询用户数据权限")
    @ApiImplicitParams({ @ApiImplicitParam(name = "UserDataAuth", value = "UserDataAuth", required = true, dataType = "UserDataAuth") })
    @RequestMapping(value = "/listUserDataAuth.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<UserDataAuth>> listUserDataAuth(UserDataAuth userDataAuth) {
        return BaseOutput.success().setData(userDataAuthService.listByExample(userDataAuth));
    }

}