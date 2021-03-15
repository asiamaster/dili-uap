package com.dili.uap.as.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.as.domain.OauthClientPrivilege;
import com.dili.uap.as.mapper.OauthClientPrivilegeMapper;
import com.dili.uap.as.service.OauthClientPrivilegeService;
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
}