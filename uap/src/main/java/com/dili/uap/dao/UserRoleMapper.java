package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.UserRole;

import java.util.Set;

public interface UserRoleMapper extends MyMapper<UserRole> {

    /**
     * 根据用户ID查询该用户拥有的角色ID
     * @param userId 用户ID
     * @return
     */
    Set<Long> getRoleIdsByUserId(Long userId);
}