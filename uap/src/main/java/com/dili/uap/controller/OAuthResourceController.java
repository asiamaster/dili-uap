package com.dili.uap.controller;

import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.dto.OauthClientPrivilegeQuery;
import com.dili.uap.glossary.ClientPrivilege;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.OauthClientPrivilegeService;
import com.dili.uap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: WM
 * @time: 2021/4/20 10:10
 */
@RestController
@RequestMapping("/api/oauth-resource")
public class OAuthResourceController {

    @Autowired
    UserService userService;

    @Autowired
    OauthClientPrivilegeService oauthClientPrivilegeService;
    /**
     * oauth授权，从header获取用户信息
     * @return
     */
    @RequestMapping(value = "/user", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<UserTicket> user() {
        UserTicket userTicket = SessionContext.getSessionContext().getOAuthTicket();
        if(userTicket == null){
            return BaseOutput.failure(ResultCode.UNAUTHORIZED, "用户未登录");
        }
        OauthClientPrivilegeQuery oauthClientPrivilege = DTOUtils.newInstance(OauthClientPrivilegeQuery.class);
        oauthClientPrivilege.setCode(userTicket.getOpenId());
        oauthClientPrivilege.setUserId(userTicket.getId());
        oauthClientPrivilege.setPrivilege(ClientPrivilege.BASIC_INFO.getCode());
        Boolean existsPrivilege = oauthClientPrivilegeService.existsPrivilege(oauthClientPrivilege);
        if(!existsPrivilege){
            return BaseOutput.failure(ResultCode.NOT_AUTH_ERROR, "没有获取用户基本信息的权限");
        }
        return BaseOutput.success().setData(userTicket);
    }

    /**
     * oauth授权，从header获取用户组织信息
     * @return
     */
    @RequestMapping(value = "/org", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<UserTicket> org() {
        UserTicket userTicket = SessionContext.getSessionContext().getOAuthTicket();
        if(userTicket == null){
            return BaseOutput.failure(ResultCode.UNAUTHORIZED, "用户未登录");
        }
        OauthClientPrivilegeQuery oauthClientPrivilege = DTOUtils.newInstance(OauthClientPrivilegeQuery.class);
        oauthClientPrivilege.setCode(userTicket.getOpenId());
        oauthClientPrivilege.setUserId(userTicket.getId());
        oauthClientPrivilege.setPrivilege(ClientPrivilege.ORG_INFO.getCode());
        Boolean existsPrivilege = oauthClientPrivilegeService.existsPrivilege(oauthClientPrivilege);
        if(!existsPrivilege){
            return BaseOutput.failure(ResultCode.NOT_AUTH_ERROR, "没有获取用户组织信息的权限");
        }
        try {
            return BaseOutput.success().setData(userService.buildOAuthOrg(userTicket));
        }catch (Exception e){
            return BaseOutput.failure(e.getMessage());
        }
    }
}
