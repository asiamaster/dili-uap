package com.dili.uap.manager.impl;

import com.dili.uap.dao.SystemMapper;
import com.dili.uap.manager.SystemManager;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.session.DynaSessionConstants;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.dili.uap.sdk.util.SerializeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
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

	@Autowired
	private DynaSessionConstants dynaSessionConstants;

	@Override
	public void initUserSystemInRedis(Long userId) {
		List<Systems> systems = this.systemMapper.listByUserId(userId);
		if (CollectionUtils.isEmpty(systems)) {
			return;
		}
		String key = SessionConstants.USER_SYSTEM_KEY + userId;
		this.redisUtils.remove(key);
		//使用BASE64编码被序列化为byte[]的对象
		this.redisUtils.set(key, Base64.getEncoder().encodeToString(SerializeUtil.serialize(systems)), dynaSessionConstants.getSessionTimeout());
	}

}
