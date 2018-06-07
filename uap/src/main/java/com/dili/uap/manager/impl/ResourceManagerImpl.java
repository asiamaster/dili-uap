package com.dili.uap.manager.impl;

import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.domain.Resource;
import com.dili.uap.manager.ResourceManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @createTime 2018-5-21
 * @author wangmi
 */
@Component
public class ResourceManagerImpl implements ResourceManager {
	private final static Logger LOG = LoggerFactory.getLogger(ResourceManagerImpl.class);

	@Autowired
	private ResourceMapper resourceMapper;

	@Autowired
	private ManageRedisUtil redisUtils;

	@Override
	public void initUserResourceCodeInRedis(Long userId) {
		List<Resource> resources = this.resourceMapper.listByUserId(userId);
		if (CollectionUtils.isEmpty(resources)) {
			return;
		}
		List<String> codes = new ArrayList<>(resources.size());
		for (Resource resource : resources) {
			if (StringUtils.isNotBlank(resource.getCode())) {
				codes.add(resource.getCode());
			}
		}
		if (CollectionUtils.isEmpty(codes)) {
			return;
		}
		String key = SessionConstants.USER_RESOURCE_CODE_KEY + userId;
		this.redisUtils.remove(key);
		this.redisUtils.getRedisTemplate().boundSetOps(key).add(codes.toArray());
	}
}
