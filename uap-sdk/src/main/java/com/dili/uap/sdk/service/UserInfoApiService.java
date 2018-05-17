package com.dili.uap.sdk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.uap.sdk.domain.UserTicket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoApiService extends AbstractApiService {
    private static final Logger log = LoggerFactory.getLogger(UserInfoApiService.class);

    public UserInfoApiService(String token, String baseUrl) {
        super(token, baseUrl);
    }

    public UserTicket getUserInfoById(Long id) {
        if (id == null) {
            log.error("查询操作员信息的ID为空");
            return null;
        }
        try {
            String respString = httpGet("/api/user/" + id + ".do", null);
            if (!StringUtils.isBlank(respString)) {
                JSONObject respObj = JSON.parseObject(respString);
                if (respObj.getBooleanValue("isSuccess")) {
                    return JSON.parseObject(respObj.getString("user"), UserTicket.class);
                }
            }
        } catch (Exception e) {
            log.error("通过ID查询操作员信息失败");
        }
        return null;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    public UserTicket getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            log.error("查询操作员信息的用户名为空");
            return null;
        }
        try {
            String respString = httpGet("/api/user/name/" + username + ".do", null);
            if (!StringUtils.isBlank(respString)) {
                JSONObject respObj = JSON.parseObject(respString);
                if (respObj.getBooleanValue("isSuccess")) {
                    return JSON.parseObject(respObj.getString("user"), UserTicket.class);
                }
            }
        } catch (Exception e) {
            log.error("通过用户名查询操作员信息失败");
        }
        return null;
    }

    /**
     * 通过手机号获取用户信息
     *
     * @param phoneNumber 用户的手机号
     * @return 用户信息
     */
    public UserTicket getUserByPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            log.error("查询操作员信息的手机号为空");
            return null;
        }
        try {
            String respString = httpGet("/api/user/cellphone/" + phoneNumber + ".do", null);
            if (!StringUtils.isBlank(respString)) {
                JSONObject respObj = JSON.parseObject(respString);
                if (respObj.getBooleanValue("isSuccess")) {
                    return JSON.parseObject(respObj.getString("user"), UserTicket.class);
                }
            }
        } catch (Exception e) {
            log.error("通过手机查询操作员信息失败");
        }
        return null;
    }

    /**
     * 根据用户编号查询用户
     *
     * @param serialNumber 用户编号
     * @return 用户信息
     */
    public UserTicket getUserBySerialNumber(String serialNumber) {
        if (StringUtils.isBlank(serialNumber)) {
            log.error("查询操作员信息的编号为空");
            return null;
        }
        try {
            String respString = httpGet("/api/user/serialNumber/" + serialNumber + ".do", null);
            if (!StringUtils.isBlank(respString)) {
                JSONObject respObj = JSON.parseObject(respString);
                if (respObj.getBooleanValue("isSuccess")) {
                    return JSON.parseObject(respObj.getString("user"), UserTicket.class);
                }
            }
        } catch (Exception e) {
            log.error("通过手机查询操作员信息失败");
        }
        return null;
    }

    /**
     * 根据部门查询用户列表
     * @param depId
     * @return
     */
    public List<UserTicket> getUsersForDep(Long depId){
        Map<String, String> map = new HashMap<>();
        map.put("depId", depId.toString());
        return getUsers(map);
    }

    /**
     * 根据查询条件获取用户分页列表
     *
     * @param request
     * @return
     */
    public List<UserTicket> getUsers(Map<String, String> request) {
        try {
            request.put("currPage", "0");
            request.put("pageSize", "1000");
            String respString = httpGet("/api/user/users/list.do", request);
            if (StringUtils.isNotBlank(respString)) {
                JSONObject respObj = JSON.parseObject(respString);
                if (respObj.getBooleanValue("isSuccess")) {
                    JSONObject page = respObj.getJSONObject("page");
                    if (page == null) {
                        return null;
                    }
                    return JSON.parseArray(page.getString("list"), UserTicket.class);
//                    return JSON.parseObject(respObj.getString("page"), PageTemplate.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查找操作员列表失败");
        }
        return null;
    }

//    public static void main(String[] args) {
////        UserInfoApiService u = new UserInfoApiService("", "http://mg.nong12.com");
//        UserInfoApiService u = SessionContext.getSessionContext().fetchUserApi();
////        List<UserTicket> list = u.getUsersForDep(22l);
//        Map<String, String> map = new HashMap<>();
//        map.put("txt", "a");
//        List<UserTicket> list = u.getUsers(map);
//        for (UserTicket ut : list) {
//            System.out.println(ut);
//        }
//    }


}
