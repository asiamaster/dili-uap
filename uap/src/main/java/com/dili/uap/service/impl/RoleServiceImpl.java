package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.RoleMapper;
import com.dili.uap.domain.Role;
import com.dili.uap.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 11:45:41.
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    public RoleMapper getActualDao() {
        return (RoleMapper)getDao();
    }
}