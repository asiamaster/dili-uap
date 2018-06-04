package com.dili.uap.manager.impl;

import com.dili.uap.dao.SystemMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.domain.System;
import com.dili.uap.manager.SystemManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class SystemManagerImpl implements SystemManager {
	private final static Logger LOG = LoggerFactory.getLogger(SystemManagerImpl.class);

	/**
	 * 缓存菜单KEY
	 */
	private static final String REDIS_MENU_TREE_KEY = "manage:menu:";

	@Autowired
	private SystemMapper systemMapper;

	@Autowired
	private ManageRedisUtil redisUtils;

	@Override
	public void initUserSystemInRedis(Long userId) {
		List<System> systems = this.systemMapper.listByUserId(userId);
		if (CollectionUtils.isEmpty(systems)) {
			return;
		}
        String key = SessionConstants.USER_SYSTEM_KEY + userId;
        this.redisUtils.remove(key);
        BoundSetOperations<String, Object> ops = this.redisUtils.getRedisTemplate().boundSetOps(key);
        for(System system : systems){
			ops.add(system);
		}
    }

}
