package com.dili.uap.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.sdk.redis.DataAuthRedis;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.redis.UserSystemRedis;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dili.uap.sdk.domain.System;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by asiam on 2018/6/7 0007.
 */
@Api("/authentication")
@Controller
@RequestMapping("/authenticationApi")
public class AuthenticationApi {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRedis userRedis;

    @Autowired
    private UserSystemRedis userSystemRedis;

    @Autowired
    private DataAuthRedis dataAuthRedis;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private ResourceMapper resourceMapper;

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

    @ApiOperation("统一授权登出")
    @RequestMapping(value = "/loginout.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput loginout(@RequestBody String json, HttpServletRequest request){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        userService.logout(sessionId);
        return BaseOutput.success("登出成功");
    }

    /**
     * 根据sessionId判断用户是否登录
     * @param json
     * @return
     */
    @ApiOperation("鉴权")
    @RequestMapping(value = "/authentication.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput authentication(@RequestBody String json){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        return userRedis.getSessionUserId(sessionId) == null ? BaseOutput.failure("未登录").setCode(ResultCode.NOT_AUTH_ERROR) : BaseOutput.success("已登录");
    }

    /**
     * 根据sessionId获取系统权限列表，如果未登录将返回空
     * @param json
     * @return
     */
    @ApiOperation("获取系统权限列表")
    @RequestMapping(value = "/listSystems.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<System>> listSystems(@RequestBody String json){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        return BaseOutput.success("调用成功").setData(userSystemRedis.getRedisUserSystems(userId));
    }

    @ApiOperation("获取菜单权限列表")
    @RequestMapping(value = "/listMenus.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Menu>> listMenus(@RequestBody String json){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }

        return BaseOutput.success("调用成功").setData(this.menuMapper.listByUserId(userId));
    }

    @ApiOperation("获取资源权限列表")
    @RequestMapping(value = "/listResources.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Resource>> listResources(@RequestBody String json){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserId(userId));
    }

    @ApiOperation("获取数据权限列表")
    @RequestMapping(value = "/listDataAuthes.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Map>> listDataAuthes(@RequestBody String json){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        return BaseOutput.success("调用成功").setData(dataAuthRedis.dataAuth(userId));
    }

    /**
     * 从json中获取sessionId
     * @param json
     * @return
     */
    private String getSessionIdByJson(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getString("sessionId");
    }
}