package com.dili.uap.domain.dto;

import com.dili.ss.dto.IBaseDomain;
import com.dili.uap.domain.User;

/**
 * 登录结果
 * Created by asiam on 2018/5/18 0018.
 */
public interface LoginResult extends IBaseDomain {

    //登录会话id
    String getSessionId();
    void setSessionId(String sessionId);

    //登录成功后的返回地址
    String getLoginPath();
    void setLoginPath(String loginPath);

    //登录用户
    User getUser();
    void setUser(User user);
}
