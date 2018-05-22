package com.dili.uap.manager.impl;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.manager.MenuManager;
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
public class MenuManagerImpl implements MenuManager {
	private final static Logger LOG = LoggerFactory.getLogger(MenuManagerImpl.class);

	/**
	 * 缓存菜单KEY
	 */
	private static final String REDIS_MENU_TREE_KEY = "manage:menu:";

	private static final String ALL_TREE_KEY = REDIS_MENU_TREE_KEY + "allTree";

	private static final String LIST_CHILDREN_KEY = REDIS_MENU_TREE_KEY + "children:";

	private static final String ITEM_KEY = REDIS_MENU_TREE_KEY + "item:";

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private ManageRedisUtil redisUtils;

	public Boolean save(Menu menu) {
		// 去重
		Menu record = DTOUtils.newDTO(Menu.class);
		menu.setParentId(menu.getParentId());
		menu.setName(menu.getName());
		if (menuMapper.selectCount(record) > 0) {
			return false;
		}

		if (menu.getParentId() != null) {
			redisUtils.remove(LIST_CHILDREN_KEY + menu.getParentId());
		}
		redisUtils.remove(ALL_TREE_KEY);

		return menuMapper.insert(menu) > 0;
	}

	public Boolean update(Menu menu) {

		if (menu.getParentId() != null) {
			redisUtils.remove(LIST_CHILDREN_KEY + menu.getParentId());
		}
		redisUtils.remove(ITEM_KEY + menu.getId());
		redisUtils.remove(ALL_TREE_KEY);
		return menuMapper.updateByPrimaryKey(menu) > 0;
	}

	@Override
	public void initUserMenuUrlsInRedis(Long userId) {
		List<String> urls = new ArrayList<>();
		List<Menu> menus = this.menuMapper.findByUserId(userId);
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
		BoundSetOperations<Object, Object> ops = this.redisUtils.getRedisTemplate().boundSetOps(key);
		ops.add(urls.toArray());
	}

}
