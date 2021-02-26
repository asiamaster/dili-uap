package com.dili.uap.sdk.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * 包含用户信息和token
 * 用于鉴权后返回多字段元组
 * @author: WM
 * @time: 2021/1/5 11:10
 */
public interface JwtToken extends IDTO {

    String getAccessToken();
    void setAccessToken(String accessToken);

    String getRefreshToken();
    void setRefreshToken(String refreshToken);

}
