package com.dili.uap.manager.impl;

import com.dili.uap.dao.MenuMapper;
import com.dili.uap.manager.MenuManager;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.session.DynaSessionConstants;
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

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private ManageRedisUtil redisUtils;

	@Autowired
	private DynaSessionConstants dynaSessionConstants;

	@Override
	public void initUserMenuUrlsInRedis(Long userId) {
		List<String> urls = new ArrayList<>();
		Map param = new HashMap<>();
		param.put("userId", userId);
		List<Menu> menus = this.menuMapper.listByUserAndSystemId(param);
		if (CollectionUtils.isEmpty(menus)) {
			return;
		}
		for (Menu menu : menus) {
			if (menu != null && StringUtils.isNotBlank(menu.getUrl())) {
				urls.add(menu.getUrl().trim().replace("http://", "").replace("https://", ""));
			}
		}
        String key = SessionConstants.USER_MENU_URL_KEY + userId;
        this.redisUtils.remove(key);
        BoundSetOperations<String, Object> ops = this.redisUtils.getRedisTemplate().boundSetOps(key);
        ops.expire(dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        ops.add(urls.toArray());
    }

}
