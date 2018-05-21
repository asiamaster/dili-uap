package com.dili.uap.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.LoginResult;

/**
 * Created by asiam on 2018/5/18 0018.
 */
public interface LoginService {

    /**
     * 根据用户名和密码登录，返回登录结果DTO
     * @param userName
     * @param password
     * @return
     */
    BaseOutput<LoginResult> login(String userName, String password);

    /**
     * 登录并标记(标记到Cookie)
     * 根据用户名和密码登录，返回是否登录成功
     * @param userName
     * @param password
     * @return
     */
    BaseOutput<Boolean> loginAndTag(String userName, String password);
}
