package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.UserRoleMapper;
import com.dili.uap.domain.UserRole;
import com.dili.uap.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 11:48:16.
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, Long> implements UserRoleService {

    public UserRoleMapper getActualDao() {
        return (UserRoleMapper)getDao();
    }
}