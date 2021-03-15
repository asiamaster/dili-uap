package com.dili.uap.as.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.as.domain.OAuthClient;
import com.dili.uap.as.service.OAuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-03-08 12:45:30.
 */
@Controller
@RequestMapping("/oauthClient")
public class OauthClientController {
    @Autowired
    OAuthClientService oauthClientService;

    /**
     * 跳转到OauthClient页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "oauthClient/index";
    }

    /**
     * 分页查询OauthClient，返回easyui分页信息
     * @param oauthClient
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(OAuthClient oauthClient) throws Exception {
        return oauthClientService.listEasyuiPageByExample(oauthClient, true).toString();
    }

    /**
     * 新增OauthClient
     * @param oauthClient
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(OAuthClient oauthClient) {
        oauthClientService.insertSelective(oauthClient);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改OauthClient
     * @param oauthClient
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(OAuthClient oauthClient) {
        oauthClientService.updateSelective(oauthClient);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除OauthClient
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        oauthClientService.delete(id);
        return BaseOutput.success("删除成功");
    }
}