package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.OauthClientPrivilegeMapper;
import com.dili.uap.domain.OauthClientPrivilege;
import com.dili.uap.domain.dto.OauthClientPrivilegeQuery;
import com.dili.uap.service.OauthClientPrivilegeService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-03-10 15:33:44.
 */
@Service
public class OauthClientPrivilegeServiceImpl extends BaseServiceImpl<OauthClientPrivilege, Long> implements OauthClientPrivilegeService {

    public OauthClientPrivilegeMapper getActualDao() {
        return (OauthClientPrivilegeMapper)getDao();
    }

    /**
     * 判断是否存在客户端权限
     * @param oauthClientPrivilegeQuery
     * @return
     */
    @Override
    public Boolean existsPrivilege(OauthClientPrivilegeQuery oauthClientPrivilegeQuery){
        return getActualDao().existsPrivilege(oauthClientPrivilegeQuery);
    }
}