package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Systems;

import java.util.List;
import java.util.Map;

public interface SystemMapper extends MyMapper<Systems> {

    /**
     * 根据用户id查询其有权限的系统
     * @param userId
     * @return
     */
    List<Systems> listByUserId(Long userId);

    List<Map> listByUserId2(Long userId);
}