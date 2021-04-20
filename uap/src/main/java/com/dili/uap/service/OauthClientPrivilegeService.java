package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.domain.OauthClientPrivilege;
import com.dili.uap.domain.dto.OauthClientPrivilegeQuery;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-03-10 15:33:44.
 */
public interface OauthClientPrivilegeService extends BaseService<OauthClientPrivilege, Long> {

    /**
     * 判断是否存在客户端权限
     * @param oauthClientPrivilegeQuery
     * @return
     */
    Boolean existsPrivilege(OauthClientPrivilegeQuery oauthClientPrivilegeQuery);
}