package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.domain.UserDataAuth;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-25 14:28:01.
 */
public interface UserDataAuthService extends BaseService<UserDataAuth, Long> {

    /**
     * 按条件删除用户数据权限
     * @param userDataAuth
     * @return
     */
    int delete(UserDataAuth userDataAuth);
}