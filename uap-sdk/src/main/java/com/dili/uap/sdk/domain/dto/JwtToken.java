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

    /**
     * 授权码参数的redirectUri，用于获取令牌时进行验证
     * @return
     */
    String getRedirectUri();
    void setRedirectUri(String redirectUri);

    /**
     * access token过期剩余时间(秒)
     * @return
     */
    Long getExpires();
    void setExpires(Long expires);

}
