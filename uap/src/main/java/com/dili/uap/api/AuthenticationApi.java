package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.service.LoginService;
import com.dili.uap.utils.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by asiam on 2018/6/7 0007.
 */
@Api("/authentication")
@Controller
@RequestMapping("/authenticationApi")
public class AuthenticationApi {

    @Autowired
    private LoginService loginService;

    @ApiOperation("统一授权登录")
    @RequestMapping(value = "/login.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput login(@RequestBody String json, HttpServletRequest request){
        JSONObject jsonObject = JSONObject.parseObject(json);
        LoginDto loginDto = DTOUtils.newDTO(LoginDto.class);
        loginDto.setUserName(jsonObject.getString("userName"));
        loginDto.setPassword(jsonObject.getString("password"));
        //设置登录后需要返回的上一页URL,用于记录登录地址到Cookie
        loginDto.setLoginPath(WebUtil.fetchReferer(request));
        //设置ip和hosts,用于记录登录日志
        loginDto.setIp(WebUtil.getRemoteIP(request));
        loginDto.setHost(request.getRemoteHost());
        return loginService.login(loginDto);
    }
}