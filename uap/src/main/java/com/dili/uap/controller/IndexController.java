package com.dili.uap.controller;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.service.redis.UserSystemRedis;
import com.dili.uap.sdk.service.redis.UserUrlRedis;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.MenuService;
import com.dili.uap.service.SystemService;
import com.dili.uap.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 首页控制器
 */
@Controller
@RequestMapping("/index")
public class IndexController {
	// 跳转到首页
	public static final String INDEX_PATH = "index/leftMenuIndex";
	// 跳转到平台首页
	public static final String PLATFORM_PATH = "index/platform";
	// 跳转到首页Controller
	public static final String REDIRECT_INDEX_PAGE = "redirect:/index/index.html";

	public static final String USERDETAIL_PATH = "index/userDetail";
	public static final String CHANGEPWD_PATH = "index/changePwd";

	@Value("${bpmc.server.address:https://bpmc.diligrp.com}")
	private String bpmcUrl;

	@Autowired
	private UserSystemRedis userSystemRedis;

	@Autowired
	private SystemService systemService;

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private UserUrlRedis userResRedis;

	@Autowired
	private BusinessLogRpc businessLogRpc;

	/**
	 * 跳转到权限主页面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap modelMap, HttpServletRequest request) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
		// 设置页面使用的用户id、名称和用户状态
		modelMap.put("userid", userTicket.getId());
		modelMap.put("username", userTicket.getRealName());
		// 这里必须要从数据库取，因为从cookie取的话，用户修改完密码，再回到此页面也会弹出修改密码框，因为cookie没有刷新
		modelMap.put("userState", userService.get(userTicket.getId()).getState());
		String systemCode = request.getParameter("systemCode") == null ? UapConstants.UAP_SYSTEM_CODE : request.getParameter("systemCode");
		modelMap.put("systemCode", systemCode);
		if (systemCode.equals(UapConstants.UAP_SYSTEM_CODE)) {
			Systems condition = DTOUtils.newInstance(Systems.class);
			condition.setCode(UapConstants.UAP_SYSTEM_CODE);
			List<Systems> uap = systemService.listByExample(condition);
			if (CollectionUtils.isEmpty(uap)) {
				throw new AppException("未配置统一权限系统");
			}
			modelMap.put("system", DTOUtils.as(uap.get(0), Systems.class));
			// 如果用户有任务中心的权限，则显示任务导航
			String taskCenterUrl = this.bpmcUrl + "/task/taskCenter.html";
			if (userResRedis.checkUserMenuUrlRight(userTicket.getId(), userTicket.getSystemType(), taskCenterUrl)) {
				modelMap.put("taskCenterUrl", taskCenterUrl);
			}
			return INDEX_PATH;
		}
		List<Systems> systems = userSystemRedis.getRedisUserSystems(userTicket.getId(), userTicket.getSystemType());
		if (null == systems) {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
		for (Systems system : systems) {
			if (systemCode.equals(system.getCode())) {
				modelMap.put("system", system);
				break;
			}
		}
		// 没有系统权限，则弹回登录页
		if (!modelMap.containsKey("system")) {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
		// 如果用户有任务中心的权限，则显示任务导航
		String taskCenterUrl = this.bpmcUrl + "/task/taskCenter.html";
		if (userResRedis.checkUserMenuUrlRight(userTicket.getId(), userTicket.getSystemType(), taskCenterUrl)) {
			modelMap.put("taskCenterUrl", taskCenterUrl);
		}
		if(UapConstants.ISS_SYSTEM_CODE.equals(systemCode)){
			saveLog(request,userTicket,null,null,"login","login");
		}
		return INDEX_PATH;
	}

	/**
	 * 跳转到平台页面
	 * 
	 * @param modelMap
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/platform.html", method = RequestMethod.GET)
	public String platform(ModelMap modelMap, HttpServletRequest req) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			List<Systems> systems = systemService.listByUserId(userTicket.getId(), SystemType.WEB.getCode());
			User user = userService.get(userTicket.getId());
			if (user == null) {
				throw new NotLoginException("登录用户不存在");
			}
			modelMap.put("sessionId", getSessionId(req));
			modelMap.put("userName", user.getUserName());
			modelMap.put("password", user.getPassword());
			modelMap.put("systems", systems);
			modelMap.put("userid", userTicket.getId());
			modelMap.put("username", userTicket.getRealName());
			modelMap.put("systemCode", UapConstants.UAP_SYSTEM_CODE);
			return PLATFORM_PATH;
		} else {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
	}

	/**
	 * 跳转到功能列表页面
	 *
	 * @param systemCode
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/featureList.html", method = RequestMethod.GET)
	public String featureList(String systemCode, ModelMap modelMap) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		// 获取系统下该用户有权限显示的菜单
		List<Map> menus = menuService.listDirAndLinks(userTicket.getId(), systemCode);
		modelMap.put("menus", menus);
		return "index/featureList";
	}

	/**
	 * 跳转到园区管理首页
	 *
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/parkIndex.html", method = RequestMethod.GET)
	public String parkIndex(ModelMap modelMap) {
		return "index/parkIndex";
	}

	/**
	 * 跳转到我的消息页面
	 *
	 * @return
	 */
	@GetMapping(value = "/messages.html")
	public String messages() {
		return "index/messages";
	}

	/**
	 * 跳转到消息详情
	 * @param request
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/messageDetail.html")
	public String messageDetail(@RequestParam Long id, HttpServletRequest request) {
		request.setAttribute("content","<html><p style='color:red;'>["+id+"]内容...</p></html>");
		return "index/messageDetail";
	}

	/**
	 * 跳转到个人信息页面
	 *
	 * @param modelMap
	 * @return
	 */
	@GetMapping(value = "/userDetail.html")
	public String userDetail(ModelMap modelMap) {
		return USERDETAIL_PATH;
	}

	/**
	 * 跳转到修改密码页面
	 *
	 * @param modelMap
	 * @return
	 */
	@GetMapping(value = "/changePwd.html")
	public String changePwd(ModelMap modelMap) {
		return CHANGEPWD_PATH;
	}

	/**
	 * 获取消息
	 *
	 * @return
	 */
	@PostMapping(value = "/message/list.action")
	@ResponseBody
	public String messagesData(IBaseDomain idto) throws Exception {
		Map<String, Object> message = new HashMap<>();
		message.put("id", "1");
		message.put("title", "标题1");
		message.put("type", 1);
		message.put("sendTime", new Date());
		List<Map<String, Object>> messages = new ArrayList<>();
		messages.add(message);
		message = new HashMap<>();
		message.put("id", "2");
		message.put("type", 2);
		message.put("title", "标题2");
		message.put("sendTime", new Date());
		messages.add(message);
		List<Map> maps = ValueProviderUtils.buildDataByProvider(idto, messages);
		return JSON.toJSONString(maps);
	}

	/**
	 * 标记消息为已读
	 *
	 * @return
	 */
	@PostMapping(value = "/message/markAsRead.action")
	@ResponseBody
	public BaseOutput markAsRead(@RequestParam Long id) {
		return BaseOutput.success();
	}

	/**
	 * 删除消息
	 *
	 * @return
	 */
	@PostMapping(value = "/message/deleteMessage.action")
	@ResponseBody
	public BaseOutput deleteMessage(@RequestParam Long id) {
		return BaseOutput.success();
	}

	/**
	 * 获取sessionId
	 * 
	 * @param req
	 * @return
	 */
	private String getSessionId(HttpServletRequest req) {
		String sessionId = null;
		// 首先读取链接中的session
		sessionId = req.getParameter(SessionConstants.SESSION_ID);
		if (StringUtils.isBlank(sessionId)) {
			sessionId = req.getHeader(SessionConstants.SESSION_ID);
		}
		if (StringUtils.isNotBlank(sessionId)) {
			WebContent.setCookie(SessionConstants.SESSION_ID, sessionId);
		} else {
			sessionId = WebContent.getCookieVal(SessionConstants.SESSION_ID);
		}
		return sessionId;
	}

	/**
	 * 判断是否包含统一权限平台
	 *
	 * @param systems
	 * @return
	 */
	private boolean containsUap(List<Systems> systems) {
		for (Systems system : systems) {
			if (UapConstants.UAP_SYSTEM_CODE.equals(system.getCode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 保存日志记录
	 *
	 * @param userTicket
	 * @param businessId    记录主键ID
	 * @param businessCode  业务编号
	 * @param businessType  业务类型
	 * @param operationType 操作类型
	 * @param content       操作内容
	 * @return
	 */
	private BaseOutput saveLog(HttpServletRequest request, UserTicket userTicket, Long businessId, String businessCode, String businessType, String operationType, String... content) {
		if (null != userTicket) {
			BusinessLog log = new BusinessLog();
			String remoteIp = RequestUtils.getIpAddress(request);
			log.setRemoteIp(StringUtils.isNotBlank(remoteIp) ? remoteIp.split(",")[0] : "");
			log.setServerIp(request.getLocalAddr());
			log.setOperatorId(userTicket.getId());
			log.setOperatorName(userTicket.getRealName());
			log.setMarketId(userTicket.getFirmId());
			log.setCreateTime(LocalDateTime.now());
			if (null != businessId && businessId > 0) {
				log.setBusinessId(businessId);
			}
			if (StringUtils.isNotBlank(businessCode)) {
				log.setBusinessCode(businessCode);
			}
			if (StringUtils.isNotBlank(businessType)) {
				log.setBusinessType(businessType);
			}
			if (StringUtils.isNotBlank(operationType)) {
				log.setOperationType(operationType);
			}
			if (content != null && content.length != 0) {
				log.setContent(String.join(",", content));
			}
			log.setSystemCode("iss");
			BaseOutput baseOutput = businessLogRpc.save(log, request.getHeader("referer"));
			return baseOutput;
		} else {
			return BaseOutput.failure("请先登录");
		}
	}

}
