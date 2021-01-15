package com.dili.uap.manager.impl;

import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.domain.Resource;
import com.dili.uap.manager.ResourceManager;
import com.dili.uap.sdk.config.DynamicConfig;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.service.redis.ManageRedisUtil;
import com.dili.uap.sdk.util.KeyBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

	@javax.annotation.Resource(name="manageRedisUtil")
	private ManageRedisUtil redisUtils;

	@Autowired
	private DynamicConfig dynamicConfig;

	@Override
	public void initWebUserResourceCodeInRedis(Long userId) {
		initUserResourceCodeInRedis(userId, SystemType.WEB.getCode());
	}

	@Override
	public void initAppUserResourceCodeInRedis(Long userId) {
		initUserResourceCodeInRedis(userId, SystemType.APP.getCode());
	}

	/**
	 * 初始化用户资源权限到redis
	 * @param userId
	 * @param systemType
	 */
	@Override
	public void initUserResourceCodeInRedis(Long userId, Integer systemType) {
		List<Resource> resources = this.resourceMapper.listByUserId(userId, systemType);
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
		String key = KeyBuilder.buildUserResourceCodeKey(userId.toString(), systemType);
		this.redisUtils.remove(key);
		BoundSetOperations<String, Object> ops = this.redisUtils.getRedisTemplate().boundSetOps(key);
		ops.expire(dynamicConfig.getRefreshTokenTimeout(systemType), TimeUnit.SECONDS);
		ops.add(codes.toArray());
	}

}
