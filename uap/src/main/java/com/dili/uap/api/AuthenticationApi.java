package com.dili.uap.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.RSAUtil;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.domain.DataAuthRef;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.manager.DataAuthManager;
import com.dili.uap.sdk.component.DataAuthSource;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.domain.System;
import com.dili.uap.sdk.redis.DataAuthRedis;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.redis.UserSystemRedis;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.DataAuthRefService;
import com.dili.uap.service.LoginService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by asiam on 2018/6/7 0007.
 */
@Api("/authenticationApi")
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

    @Autowired
    private DataAuthManager dataAuthManager;

    @Autowired
    private DataAuthSource dataAuthSource;

    @Autowired
    private DataAuthRefService dataAuthRefService;

    @Value("${rsaPrivateKey:}")
    private String rsaPrivateKey;

    /**
     * 统一授权登录，返回登录用户信息LoginResult
     * @param json
     * @param request
     * @return
     */
    @ApiOperation("统一授权登录")
    @RequestMapping(value = "/login.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput login(@RequestBody String json, HttpServletRequest request){
        try {
            json = decryptRSA(json);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
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

    /**
     * 用户登录验证，返回新的sessionId
     * @param json
     * @return
     */
    @ApiOperation("登录验证")
    @RequestMapping(value = "/validate.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput validate(@RequestBody String json) {
        try {
            json = decryptRSA(json);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        LoginDto loginDto = DTOUtils.newDTO(LoginDto.class);
        loginDto.setUserName(jsonObject.getString("userName"));
        loginDto.setPassword(jsonObject.getString("password"));
        return loginService.validateSaveSession(loginDto);
    }

    /**
     * 根据sessionId判断用户是否登录
     * 无返回信息
     * @param json
     * @return
     */
    @ApiOperation("鉴权")
    @RequestMapping(value = "/authentication.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput authentication(@RequestBody String json){
        try {
            json = decryptRSA(json);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        return userRedis.getSessionUserId(sessionId) == null ? BaseOutput.failure("未登录").setCode(ResultCode.NOT_AUTH_ERROR) : BaseOutput.success("已登录");
    }

    @ApiOperation("统一授权登出")
    @RequestMapping(value = "/loginout.api", method = { RequestMethod.POST })
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
     * 根据sessionId取用户名
     * @param json
     * @return
     */
    @RequestMapping(value = "/getUserNameBySessionId.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput getUserNameBySessionId(@RequestBody String json){
        String sessionId = getSessionIdByJson(json);
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        String userName = userRedis.getUserNameBySessionId(sessionId);
        return userName == null ? BaseOutput.failure("未登录").setCode(ResultCode.NOT_AUTH_ERROR) : BaseOutput.success(userName);
    }

    /**
     * 根据sessionId获取系统权限列表，如果未登录将返回空
     * @param json
     * @return
     */
    @ApiOperation("获取系统权限列表")
    @RequestMapping(value = "/listSystems.api", method = { RequestMethod.POST })
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
    @RequestMapping(value = "/listMenus.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Menu>> listMenus(@RequestBody String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        String sessionId = jsonObject.getString("sessionId");
        String systemId = jsonObject.getString("systemId");
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("systemId", systemId);
        return BaseOutput.success("调用成功").setData(this.menuMapper.listByUserAndSystemId(param));
    }

    @ApiOperation("获取资源权限列表")
    @RequestMapping(value = "/listResources.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Resource>> listResources(@RequestBody String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        String sessionId = jsonObject.getString("sessionId");
        Long systemId = jsonObject.getLong("systemId");
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        if(systemId == null){
            return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserId(userId));
        }
        return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserIdAndSystemId(userId, systemId));
    }

    @ApiOperation("获取数据权限列表")
    @RequestMapping(value = "/listDataAuthes.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Map>> listDataAuthes(@RequestBody String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        String sessionId = jsonObject.getString("sessionId");
        String refCode = jsonObject.getString("refCode");
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        //返回UserDataAuth列表
        return BaseOutput.success("调用成功").setData(dataAuthManager.listUserDataAuthesByRefCode(userId, refCode));
    }

    @ApiOperation("获取数据权限详情列表")
    @RequestMapping(value = "/listDataAuthDetails.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<Map<String, Map>> listDataAuthDetails(@RequestBody String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        String sessionId = jsonObject.getString("sessionId");
        String refCode = jsonObject.getString("refCode");
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        if(StringUtils.isBlank(refCode)){
            return BaseOutput.failure("refCode不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        if(userId == null){
            return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
        }
        //查询数据权限引用dataAuthRef
        DataAuthRef dataAuthRef = DTOUtils.newDTO(DataAuthRef.class);
        dataAuthRef.setCode(refCode);
        List<DataAuthRef> dataAuthRefs = this.dataAuthRefService.list(dataAuthRef);
        if(CollectionUtils.isEmpty(dataAuthRefs)){
            return BaseOutput.failure("数据权限引用不存在");
        }
        //从Redis获取用户有权限的UserDataAuth列表
        List<Map> userDataAuthes = this.dataAuthRedis.dataAuth(refCode, userId);
        if(CollectionUtils.isEmpty(userDataAuthes)){
            return BaseOutput.success("调用成功").setData(userDataAuthes);
        }
        List values = userDataAuthes.parallelStream().map(t -> t.get("value")).collect(Collectors.toList());
        Map<String, Map> dataAuthMap = dataAuthSource.getDataAuthSourceServiceMap().get(dataAuthRefs.get(0).getSpringId()).bindDataAuthes(dataAuthRefs.get(0).getParam(), values);
        //返回UserDataAuth列表
        return BaseOutput.success("调用成功").setData(dataAuthMap);
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/changePwd.api", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput changePwd(@RequestBody String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String sessionId = jsonObject.getString("sessionId");
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        String confirmPassword = jsonObject.getString("confirmPassword");
        if(StringUtils.isBlank(sessionId)){
            return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
        }
        Long userId = userRedis.getSessionUserId(sessionId);
        UserDto userDto = DTOUtils.newDTO(UserDto.class);
        userDto.setId(userId);
        userDto.setNewPassword(newPassword);
        userDto.setConfirmPassword(confirmPassword);
        userDto.setOldPassword(oldPassword);
        return userService.changePwd(userId, userDto);
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

    /**
     * RSA解密
     * @param code
     * @return
     */
    private String decryptRSA(String code) throws Exception {
        return new String(RSAUtil.decryptByPrivateKey(Base64.decodeBase64(code), Base64.decodeBase64(rsaPrivateKey)));
    }
}