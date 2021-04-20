package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.OauthClientPrivilege;
import com.dili.uap.domain.dto.OauthClientPrivilegeQuery;

public interface OauthClientPrivilegeMapper extends MyMapper<OauthClientPrivilege> {

    /**
     * 判断是否存在客户端权限
     * @param oauthClientPrivilegeQuery
     * @return
     */
    Boolean existsPrivilege(OauthClientPrivilegeQuery oauthClientPrivilegeQuery);
}