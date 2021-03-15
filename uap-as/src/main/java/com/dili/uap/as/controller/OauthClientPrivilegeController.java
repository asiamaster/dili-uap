package com.dili.uap.as.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.as.domain.OauthClientPrivilege;
import com.dili.uap.as.service.OauthClientPrivilegeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-03-10 15:33:44.
 */
@Controller
@RequestMapping("/oauthClientPrivilege")
public class OauthClientPrivilegeController {
    @Autowired
    OauthClientPrivilegeService oauthClientPrivilegeService;

    /**
     * 跳转到OauthClientPrivilege页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "oauthClientPrivilege/index";
    }

    /**
     * 分页查询OauthClientPrivilege，返回easyui分页信息
     * @param oauthClientPrivilege
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(OauthClientPrivilege oauthClientPrivilege) throws Exception {
        return oauthClientPrivilegeService.listEasyuiPageByExample(oauthClientPrivilege, true).toString();
    }

    /**
     * 新增OauthClientPrivilege
     * @param oauthClientPrivilege
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(OauthClientPrivilege oauthClientPrivilege) {
        oauthClientPrivilegeService.insertSelective(oauthClientPrivilege);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改OauthClientPrivilege
     * @param oauthClientPrivilege
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(OauthClientPrivilege oauthClientPrivilege) {
        oauthClientPrivilegeService.updateSelective(oauthClientPrivilege);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除OauthClientPrivilege
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        oauthClientPrivilegeService.delete(id);
        return BaseOutput.success("删除成功");
    }
}