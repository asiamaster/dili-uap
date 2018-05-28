package com.dili.uap.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * 用于登录的传输对象
 * Created by asiam on 2018/5/22 0022.
 */
public interface LoginDto extends IDTO {

    /**
     * 登录用户的id，用于记录登录日志
     * @return
     */
    Long getUserId();
    void setUserId(Long userId);

    String getUserName();
    void setUserName(String userName);

    String getPassword();
    void setPassword(String password);

    /**
     * 客户端访问的ip，用于记录登录日志
     * @return
     */
    String getIp();
    void setIp(String ip);

    /**
     * 客户端访问的host，用于记录登录日志
     * @return
     */
    String getHost();
    void setHost(String host);

    /**
     * 登录成功后的返回地址
     * @return
     */
    String getLoginPath();
    void setLoginPath(String loginPath);

    /**
     * 登录系统编码
     * @return
     */
    String getSystemCode();
    void setSystemCode(String systemCode);

    /**
     * 登录市场编码
     * @return
     */
    String getFirmCode();
    void setFirmCode(String firmCode);

}
