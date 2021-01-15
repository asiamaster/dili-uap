package com.dili.uap.manager.impl;

import com.dili.uap.dao.MenuMapper;
import com.dili.uap.manager.MenuManager;
import com.dili.uap.sdk.config.DynamicConfig;
import com.dili.uap.sdk.domain.Menu;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 菜单redis管理器
 */
@Component
public class MenuManagerImpl implements MenuManager {
	private final static Logger LOG = LoggerFactory.getLogger(MenuManagerImpl.class);

	@SuppressWarnings("all")
	@Autowired
	private MenuMapper menuMapper;

	@Resource(name="manageRedisUtil")
	private ManageRedisUtil redisUtils;

	@Autowired
	private DynamicConfig dynamicConfig;

	@Override
	public void initWebUserMenuUrlsInRedis(Long userId) {
		initUserMenuUrlsInRedis(userId, SystemType.WEB.getCode());
	}

	@Override
	public void initAppUserMenuUrlsInRedis(Long userId) {
		initUserMenuUrlsInRedis(userId, SystemType.APP.getCode());
	}

	/**
	 * 初始化用户菜单URL到redis
	 * @param userId
	 * @param systemType
	 */
	@Override
	public void initUserMenuUrlsInRedis(Long userId, Integer systemType) {
		List<String> urls = new ArrayList<>();
		Map param = new HashMap<>();
		param.put("userId", userId);
		param.put("systemType", systemType);
		List<Menu> menus = this.menuMapper.listByUserAndSystemId(param);
		if (CollectionUtils.isEmpty(menus)) {
			return;
		}
		for (Menu menu : menus) {
			if (menu != null && StringUtils.isNotBlank(menu.getUrl())) {
				urls.add(menu.getUrl().trim().replace("http://", "").replace("https://", ""));
			}
		}
		String key = KeyBuilder.buildUserMenuUrlKey(userId.toString(), systemType);
		this.redisUtils.remove(key);
		BoundSetOperations<String, Object> ops = this.redisUtils.getRedisTemplate().boundSetOps(key);
		ops.expire(dynamicConfig.getWebRefreshTokenTimeout(), TimeUnit.SECONDS);
		ops.add(urls.toArray());
	}

}
