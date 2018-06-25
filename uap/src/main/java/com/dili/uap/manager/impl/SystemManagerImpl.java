package com.dili.uap.manager.impl;

import com.dili.uap.dao.SystemMapper;
import com.dili.uap.manager.SystemManager;
import com.dili.uap.sdk.domain.System;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.dili.uap.sdk.util.SerializeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

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
		BASE64Encoder enc=new BASE64Encoder();
		//使用BASE64编码被序列化为byte[]的对象
		this.redisUtils.set(key, enc.encodeBuffer(SerializeUtil.serialize(systems)));
    }

}
