package com.dili.uap.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * 用于登录的传输对象
 * Created by asiam on 2018/5/22 0022.
 */
public interface LoginDto extends IDTO {

    String getUserName();
    void setUserName(String userName);

    String getPassword();
    void setPassword(String password);

    /**
     * 客户端访问的ip
     * @return
     */
    String getRemoteIP();
    void setRemoteIP(String remoteIP);

    /**
     * 登录成功后的返回地址
     * @return
     */
    String getLoginPath();
    void setLoginPath(String loginPath);
}
