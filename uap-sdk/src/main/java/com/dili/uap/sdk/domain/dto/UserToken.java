package com.dili.uap.sdk.domain.dto;

import com.dili.uap.sdk.domain.UserTicket;

/**
 * 包含用户信息和token
 * 用于鉴权后返回多字段元组
 * @author: WM
 * @time: 2021/1/5 11:10
 */
public interface UserToken extends JwtToken {
    UserTicket getUserTicket();
    void setUserTicket(UserTicket userTicket);

    /**
     * 获取UserToken的步骤， 1: 直接从accessToken获取, 2: 从refreshToken的临时缓存中获取, 3: 根据refreshToken重新颁发accessToken
     * 参见TokenStep
     * @return
     */
    Integer getTokenStep();
    void setTokenStep(Integer tokenStep);
}
