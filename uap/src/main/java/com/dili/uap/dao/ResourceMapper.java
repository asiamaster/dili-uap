package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.Resource;

import java.util.List;

/**
 * 资源管理者
 */
public interface ResourceMapper extends MyMapper<Resource> {

    /**
     * 根据用户id查询资源列表
     * @param userId
     * @return
     */
    List<Resource> listByUserId(Long userId);
}