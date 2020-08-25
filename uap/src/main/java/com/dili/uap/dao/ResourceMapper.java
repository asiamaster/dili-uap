package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.Resource;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据用户id和系统id查询资源列表
     * @param userId
     * @param systemId
     * @return
     */
    List<Resource> listByUserIdAndSystemId(Long userId, Long systemId);

	List<String> findResourceCodeByUserId(Long userId);

    /**
     * 根据菜单url，查询资源id
     * @param param key为url和userId
     * @return
     */
	List<String> listResourceCodeByMenuUrl(Map param);
}