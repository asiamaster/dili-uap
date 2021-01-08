package com.dili.uap.controller;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.ResourceLink;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.ResourceLinkService;
import com.dili.uap.service.ResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-21 16:46:27.
 */
@Controller
@RequestMapping("/resource")
public class ResourceController {
	@Autowired
	ResourceService resourceService;
	@Autowired
	ResourceLinkService resourceLinkService;

	/**
	 * 跳转到Resource页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "resource/index";
	}

	/**
	 * 查询资源列表
	 * 
	 * @param menuId
	 * @return
	 */
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Resource> list(@RequestParam String menuId) {
		Resource query = DTOUtils.newInstance(Resource.class);
		if (menuId.startsWith("menu_")) {
			query.setMenuId(Long.parseLong(menuId.substring(5)));
		}
		query.setSort("id");
		query.setOrder("asc");
		return this.resourceService.listByExample(query);
	}

	/**
	 * 查询资源名称
	 * 
	 * @param resource
	 * @return
	 */
	@RequestMapping(value = "/getName.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getName(Resource resource) {
		List<Resource> resources = this.resourceService.list(resource);
		if (CollectionUtils.isEmpty(resources)) {
			return null;
		}
		return resources.get(0).getName();
	}

	/**
	 * 查询资源列表
	 * 
	 * @param resourceLink
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listResourceLink.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Map> listResourceLink(ResourceLink resourceLink) throws Exception {
		List<ResourceLink> resourceLinks = this.resourceLinkService.list(resourceLink);
		return ValueProviderUtils.buildDataByProvider(resourceLink, resourceLinks);
	}

	/**
	 * 新增ResourceLink
	 * 
	 * @param resourceLink
	 * @return
	 */
	@BusinessLogger(businessType = "resource_management", content = "菜单资源绑定：菜单id${menuId}, 资源id${resourceId}", operationType = "addResourceLink", systemCode = "UAP")
	@PostMapping(value = "/addResourceLink.action")
	public @ResponseBody BaseOutput addResourceLink(ResourceLink resourceLink) {

		try {
			resourceLinkService.addResourceLink(resourceLink);
			return BaseOutput.success("绑定资源链接成功").setData(resourceLink.getId());
		} catch (Exception e) {
			return BaseOutput.success(e.getMessage()).setData(resourceLink.getId());
		}
	}

	/**
	 * 删除ResourceLink
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "resource_management", content = "菜单资源绑定：菜单id${menuId}, 资源id${resourceId}", operationType = "deleteResourceLink", systemCode = "UAP")
	@RequestMapping(value = "/deleteResourceLink.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput deleteResourceLink(@RequestParam Long id) {
		resourceLinkService.deleteResourceLink(id);
		return BaseOutput.success("删除资源链接成功");
	}

	/**
	 * 新增Resource
	 * 
	 * @param resource
	 * @return
	 */
	@BusinessLogger(businessType = "resource_management", content = "新增资源", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(Resource resource) {
		String menuId = resource.aget("menuId").toString();
		if (menuId.startsWith("menu_")) {
			// 如果菜单树上点的节点是菜单， 需要设置当前节点的父节点
			resource.setMenuId(Long.parseLong(menuId.substring(5)));
		}
		resourceService.insertSelective(resource);
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, resource.getCode());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, resource.getId());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("新增成功").setData(resource.getId());
	}

	/**
	 * 修改Resource
	 * 
	 * @param resource
	 * @return
	 */
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(Resource resource) {
		String menuId = resource.aget("menuId").toString();
		if (menuId.startsWith("menu_")) {
			// 如果菜单树上点的节点是菜单， 需要设置当前节点的父节点
			resource.setMenuId(Long.parseLong(menuId.substring(5)));
		}
		resourceService.updateSelective(resource);
		// 级联更新ResourceLink,先获取原始ResourceCode
		Resource resource1 = resourceService.get(resource.getId());
		ResourceLink resourceLinkCondition = DTOUtils.newInstance(ResourceLink.class);
		resourceLinkCondition.setResourceId(resource1.getId());
		ResourceLink resourceLink = DTOUtils.newInstance(ResourceLink.class);
		resourceLink.setResourceId(resource.getId());
		resourceLinkService.updateSelectiveByExample(resourceLink, resourceLinkCondition);
		return BaseOutput.success("修改成功");
	}

	/**
	 * 删除Resource
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		// 级联更新ResourceLink,先获取原始ResourceCode
		try {
			resourceService.deleteResourceAndLink(id);
			return BaseOutput.success("删除成功");
		} catch (Exception e) {
			return BaseOutput.success("删除失败:"+e.getMessage());
		}

	}
}