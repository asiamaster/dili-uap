package com.dili.uap.manager.impl;

import com.dili.uap.dao.SystemMapper;
import com.dili.uap.manager.SystemManager;
import com.dili.uap.sdk.config.DynamicConfig;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.service.redis.ManageRedisUtil;
import com.dili.uap.sdk.util.KeyBuilder;
import com.dili.uap.sdk.util.SerializeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

@Component
public class SystemManagerImpl implements SystemManager {

	@Autowired
	private SystemMapper systemMapper;

	@Resource(name="manageRedisUtil")
	private ManageRedisUtil redisUtils;

	@Autowired
	private DynamicConfig dynamicConfig;

	@Override
	public void initWebUserSystemInRedis(Long userId) {
		initUserSystemInRedis(userId, SystemType.WEB.getCode());
	}

	@Override
	public void initAppUserSystemInRedis(Long userId) {
		initUserSystemInRedis(userId, SystemType.APP.getCode());
	}

	/**
	 * 初始化用户系统到redis
	 * @param userId
	 * @param systemType
	 */
	@Override
	public void initUserSystemInRedis(Long userId, Integer systemType) {
		List<Systems> systems = this.systemMapper.listByUserId(userId, systemType);
		if (CollectionUtils.isEmpty(systems)) {
			return;
		}
		String key = KeyBuilder.buildUserSystemKey(userId.toString(), systemType);
		this.redisUtils.remove(key);
		// 使用BASE64编码被序列化为byte[]的对象
		this.redisUtils.set(key, Base64.getEncoder().encodeToString(SerializeUtil.serialize(systems)), dynamicConfig.getWebRefreshTokenTimeout());
	}
}
