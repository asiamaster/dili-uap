package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.domain.Resource;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:46:27.
 */
public interface ResourceService extends BaseService<Resource, Long> {
	 /**
     * 根据用户ID获取用户所属权限信息名称
     * @param userId 用户名
     * @return  
     */
	List<String> listResourceCodeByUserId(Long userId);

	/**
	 * 根据菜单url，查询资源id
	 * @param url
	 * @param userId
	 * @return
	 */
	List<String> listResourceCodeByMenuUrl(String url, Long userId);
}