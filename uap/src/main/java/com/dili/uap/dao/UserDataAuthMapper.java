package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.UserDataAuth;

import java.util.Map;

public interface UserDataAuthMapper extends MyMapper<UserDataAuth> {

    /**
     * 根据登录用户id和选择用户id删除，选择用户的数据权限
     * @param param
     */
    void deleteUserDataAuth(Map param);
}