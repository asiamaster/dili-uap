package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.domain.Resource;
import com.dili.uap.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-21 16:46:27.
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Long> implements ResourceService {

	public ResourceMapper getActualDao() {
		return (ResourceMapper) getDao();
	}

	@Override
	public List<String> listResourceCodeByUserId(Long userId) {
		return this.getActualDao().findResourceCodeByUserId(userId);
	}

	@Override
	public List<String> listResourceCodeByMenuUrl(String url, Long userId) {
		Map param = new HashMap(2);
		param.put("url", url);
		param.put("userId", userId);
		return this.getActualDao().listResourceCodeByMenuUrl(param);
	}

	@Override
	public List<String> listResourceCodesByUserId(Long userId, List<String> resourceCodes) {
		return this.getActualDao().listResourceCodesByUserId(userId, resourceCodes);
	}
}