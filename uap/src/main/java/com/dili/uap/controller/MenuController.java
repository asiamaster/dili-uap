package com.dili.uap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.domain.Resource;
import com.dili.uap.glossary.MenuType;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.MenuService;
import com.dili.uap.service.ResourceService;
import com.google.common.collect.Lists;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-21 16:08:04.
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
	@Autowired
	private MenuService menuService;

	@Autowired
	private ResourceService resourceService;

	/**
	 * 跳转到Menu页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "menu/index";
	}

	/**
	 * 查询系统菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listSystemMenu.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Map> listSystemMenu() {
		return this.menuService.listSystemMenu();
	}

	/**
	 * 查询菜单列表
	 * 
	 * @param systemId
	 * @return
	 */
	@RequestMapping(value = "/listMenus.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Map> listMenus(@RequestParam Long systemId) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			throw new NotLoginException();
		}
		List<Menu> menus = this.menuService.listDirAndLinksByUserIdAndSystemId(userTicket.getId(), systemId);
		if (CollectionUtils.isEmpty(menus)) {
			return Lists.newArrayList();
		}
		List<Map> menuMaps = new ArrayList<>(menus.size());
		menus.forEach(item -> {
			Map menuMap = DTOUtils.go(item);
			menuMap.put("iconCls", item.getIconCls());
			menuMap.put("order", item.getOrderNumber());
			if (item.getParentId() == null) {
				menuMap.put("state", "open");
			}
			Map<String, String> attr = new HashMap<>(6);
			attr.put("type", item.getType().toString());
			attr.put("systemId", item.getSystemId().toString());
			attr.put("url", item.getUrl());
			menuMap.put("attributes", attr);
			menuMaps.add(menuMap);
		});
		menuMaps.sort((o1, o2) -> {
			Object order1 = o1.get("order");
			Object order2 = o2.get("order");
			if (order1 == null && order2 == null) {
				return 0;
			}
			if (order1 == null) {
				return 1;
			}
			if (order2 == null) {
				return -1;
			}
			try {
				return Integer.parseInt(order1.toString()) - Integer.parseInt(order2.toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return 0;
			}
		});
		return menuMaps;
	}

	/**
	 * 查询菜单列表
	 * 
	 * @param menuId
	 * @return
	 */
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Menu> list(@RequestParam String menuId) {
		Menu query = DTOUtils.newInstance(Menu.class);
		if (menuId.startsWith("menu_")) {
			query.setParentId(Long.parseLong(menuId.substring(5)));
		} else if (menuId.startsWith("sys_")) {
			query.setSystemId(Long.parseLong(menuId.substring(4)));
			query.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
		}
		query.setSort("order_number");
		query.setOrder("asc");
		return this.menuService.listByExample(query);
	}

	/**
	 * 查询内部链接列表
	 * 
	 * @param menuId
	 * @return
	 */
	@RequestMapping(value = "/listInternalLinks.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Menu> listInternalLinks(@RequestParam String menuId) {
		Menu query = DTOUtils.newInstance(Menu.class);
		if (menuId.startsWith("menu_")) {
			query.setParentId(Long.parseLong(menuId.substring(5)));
		} else if (menuId.startsWith("sys_")) {
			query.setSystemId(Long.parseLong(menuId.substring(4)));
			query.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
		}
		query.setType(MenuType.INTERNAL_LINKS.getCode());
		query.setSort("order_number");
		query.setOrder("asc");
		return this.menuService.listByExample(query);
	}

	/**
	 * 新增Menu
	 * 
	 * @param menu
	 * @return
	 */
	@BusinessLogger(businessType = "menu_management", content = "新增菜单", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(Menu menu) {
		String menuId = menu.aget("menuId").toString();
		if (menuId.startsWith("menu_")) {
			// 如果菜单树上点的节点是菜单， 需要设置当前节点的父节点
			menu.setParentId(Long.parseLong(menuId.substring(5)));
		} else if (menuId.startsWith("sys_")) {
			// 如果菜单树上点的节点是系统，需要把parent_id清空
			menu.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
		}
		menu.setCode(UUID.randomUUID().toString());
		menuService.insertSelective(menu);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, menu.getId());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, menu.getId());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("新增成功").setData(menu);
	}

	/**
	 * 修改Menu
	 * 
	 * @param menu
	 * @return
	 */
	@BusinessLogger(businessType = "menu_management", content = "修改菜单", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(Menu menu) {
		String menuId = menu.aget("menuId").toString();
		if (menuId.startsWith("menu_")) {
			// 如果菜单树上点的节点(menuId)是菜单， 需要设置当前节点的父节点
			menu.setParentId(Long.parseLong(menuId.substring(5)));
		}
		Menu oldMenu = menuService.get(menu.getId());
		// 如果修改了菜单类型，需要判断菜单下面不能有子菜单或资源
		if (!oldMenu.getType().equals(menu.getType())) {
			Menu condition = DTOUtils.newInstance(Menu.class);
			condition.setParentId(menu.getId());
			List children = menuService.list(condition);
			if (!CollectionUtils.isEmpty(children)) {
				return BaseOutput.failure("菜单有子节点，不允许修改类型");
			}
			Resource resouce = DTOUtils.newInstance(Resource.class);
			resouce.setMenuId(menu.getId());
			List<Resource> resources = resourceService.list(resouce);
			if (!CollectionUtils.isEmpty(resources)) {
				return BaseOutput.failure("菜单下有资源，不允许修改类型");
			}
		}
		menuService.updateExactSimple(menu);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, menu.getId());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, menu.getId());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("修改成功");
	}

	/**
	 * 删除Menu
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		String msg = menuService.deleteMenu(id);
		return msg == null ? BaseOutput.success("删除成功") : BaseOutput.failure(msg);
	}

	/**
	 * 拖动到菜单下面，需要修改parentId, 并且判断目标菜单是否目录，是目录则不处理menu类型，是链接则需要改为内键
	 * 不允许跨系统拖动，不允许将目录拖到链接下面
	 * 
	 * @param sourceId
	 * @param targetId
	 * @return
	 */
	@BusinessLogger(businessType = "menu_management", content = "移动菜单", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/shiftMenu.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput shiftMenu(String sourceId, String targetId) {
		// 源节点肯定是菜单，所以是以menu_开头
		Menu sourceMenu = menuService.get(Long.parseLong(sourceId.substring(5)));
		// 如果拖动到系统下面，需要判断不允许跨系统拖动
		if (targetId.startsWith("sys_") && !sourceMenu.getSystemId().equals(Long.parseLong(targetId.substring(4)))) {
			return BaseOutput.failure("不允许跨系统拖动");
		}
		// 如果拖到系统下面, 需要清空parentId
		if (targetId.startsWith("sys_")) {
			sourceMenu.setParentId(null);
			// 修改内链菜单类型为链接
			if (sourceMenu.getType().equals(MenuType.INTERNAL_LINKS.getCode())) {
				sourceMenu.setType(MenuType.LINKS.getCode());
			}
		} else {
			// 拖动到菜单下面，判断不允许跨系统拖动，不允许将目录拖动下链接下面
			// 验证通过后需要修改parentId, 并且判断目标菜单是否目录，是目录则不处理menu类型，是链接则需要改为内键
			Long targetMenuId = Long.parseLong(targetId.substring(5));
			Menu targetMenu = menuService.get(targetMenuId);
			if (!targetMenu.getSystemId().equals(sourceMenu.getSystemId())) {
				return BaseOutput.failure("不允许跨系统拖动");
			}
			// 如果源是目录，目标非目录
			if (sourceMenu.getType().equals(MenuType.DIRECTORY.getCode()) && !targetMenu.getType().equals(MenuType.DIRECTORY.getCode())) {
				return BaseOutput.failure("不允许将目录拖动到链接下面");
			}
			// 如果源是链接，目标是链接或内链, 修改源为内链
			if (sourceMenu.getType().equals(MenuType.LINKS.getCode()) && (targetMenu.getType().equals(MenuType.INTERNAL_LINKS.getCode()) || targetMenu.getType().equals(MenuType.LINKS.getCode()))) {
				sourceMenu.setType(MenuType.INTERNAL_LINKS.getCode());
			}
			// 如果源是内链，目标是目录，修改源为链接
			if (sourceMenu.getType().equals(MenuType.INTERNAL_LINKS.getCode()) && targetMenu.getType().equals(MenuType.DIRECTORY.getCode())) {
				sourceMenu.setType(MenuType.LINKS.getCode());
			}
			sourceMenu.setParentId(targetMenuId);
			if (targetMenu.getType().equals(MenuType.LINKS.getCode())) {
				sourceMenu.setType(MenuType.INTERNAL_LINKS.getCode());
			}
		}
		menuService.updateExactSimple(sourceMenu);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, sourceId);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, sourceId);
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("移动菜单成功");
	}

}