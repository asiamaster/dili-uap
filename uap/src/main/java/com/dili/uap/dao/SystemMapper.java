package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.System;

import java.util.List;

public interface SystemMapper extends MyMapper<System> {

    /**
     * 根据用户id查询其有权限的系统
     * @param userId
     * @return
     */
    List<System> listByUserId(Long userId);
}