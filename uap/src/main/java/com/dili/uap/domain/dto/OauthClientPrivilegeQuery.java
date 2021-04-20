package com.dili.uap.domain.dto;

import com.dili.uap.domain.OauthClientPrivilege;

/**
 * 由MyBatis Generator工具自动生成
 * 开放授权客户端权限
 * This file was generated on 2021-03-10 15:33:44.
 */
public interface OauthClientPrivilegeQuery extends OauthClientPrivilege {

    /**
     * 客户端编码(openid)
     * @return
     */
    String getCode();
    void setCode(String code);
}