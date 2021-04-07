package com.dili.uap.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.cms.sdk.dto.AnnunciateQueryDto;
import com.dili.cms.sdk.rpc.AnnunciateRpc;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.exception.AppException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.dili.uap.oauth.constant.ResultCode;

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

	@Autowired
	private RabbitMQMessageService rabbitMQMessageService;

	@Autowired
	private AnnunciateRpc annunciateRpc;

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
			saveLog(request,userTicket,null,null,"issLogin","issLogin");
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
			modelMap.put("accessToken", getAccessToken(req));
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
	 * @param id annunciate的id，用于显示消息详情
	 * @return
	 */
	@GetMapping(value = "/messageDetail.html")
	public String messageDetail(@RequestParam Long id, HttpServletRequest request) {
		BaseOutput<String> output = annunciateRpc.getContentById(id);
		if(!output.isSuccess()){
			return output.getMessage();
		}

		request.setAttribute("content","<html><p style='color:red;'>"+output.getData()+"</p></html>");
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
//		Map<String, Object> message = new HashMap<>();
//		message.put("id", "1");
//		message.put("title", "标题1");
//		message.put("type", 1);
//		message.put("readState", 1);
//		message.put("sendTime", new Date());
//		List<Map<String, Object>> messages = new ArrayList<>();
//		messages.add(message);
//		message = new HashMap<>();
//		message.put("id", "2");
//		message.put("type", 2);
//		message.put("readState", 2);
//		message.put("title", "标题2");
//		message.put("sendTime", new Date());
//		messages.add(message);
//		List<Map> maps = ValueProviderUtils.buildDataByProvider(idto, messages);
//		return new EasyuiPageOutput((long)maps.size(), maps).toString();

		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket  == null){
			return "";
		}
		AnnunciateQueryDto annunciateDto = DTOUtils.asInstance(idto, AnnunciateQueryDto.class);
		annunciateDto.setTargetId(userTicket.getId());
//		return "";
		BaseOutput<String> output = annunciateRpc.getListByTargetId(annunciateDto);
		if(!output.isSuccess()){
			return "";
		}
		return output.getData();
//		BaseOutput<String> output = annunciateRpc.getListByTargetId(annunciateDto);
//		if(!output.isSuccess()){
//			return "";
//		}
//		return output.getData();
//		List<AnnunciateVo> data = output.getData();
//		data.stream().forEach(t->t.setType(1));
//		return new EasyuiPageOutput((long)data.size(), data).toString();
	}

	/**
	 * 标记消息为已读
	 *
	 * @return
	 */
	@PostMapping(value = "/message/getUnreadCount.action")
	@ResponseBody
	public BaseOutput<Integer> getUnreadCount(@RequestParam(required = false) Long userId) {
		if(userId != null){
			return annunciateRpc.getNoReadCountByTargetId(userId);
		}
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket  == null){
			return BaseOutput.failure("用户未登录");
		}
		try {
			return annunciateRpc.getNoReadCountByTargetId(userTicket.getId());
		} catch (Exception e) {
			return BaseOutput.failure(ResultCode.REMOTE_ERROR, e.getMessage());
		}
	}

	/**
	 * 标记消息为已读
	 *
	 * @return
	 */
	@PostMapping(value = "/message/markAsRead.action")
	@ResponseBody
	public BaseOutput markAsRead(@RequestParam Long id) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket  == null){
			return BaseOutput.failure("用户未登录");
		}
		return annunciateRpc.readByIdAndTargetId(id, userTicket.getId());
	}

	/**
	 * 标记全部消息为已读
	 *
	 * @return
	 */
	@PostMapping(value = "/message/markAllRead.action")
	@ResponseBody
	public BaseOutput markAllRead() {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			return BaseOutput.failure("未登录!");
		}
		return annunciateRpc.readByTargetId(userTicket.getId());
	}

	/**
	 * 删除全部消息
	 *
	 * @return
	 */
	@PostMapping(value = "/message/deleteAllMessage.action")
	@ResponseBody
	public BaseOutput deleteAllMessage() {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			return BaseOutput.failure("未登录!");
		}
		return annunciateRpc.deleteByTargetId(userTicket.getId());
	}

	/**
	 * 删除消息
	 *
	 * @return
	 */
	@PostMapping(value = "/message/deleteMessage.action")
	@ResponseBody
	public BaseOutput deleteMessage(@RequestParam Long id) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			return BaseOutput.failure("未登录!");
		}
		return annunciateRpc.deleteByIdAndTargetId(id, userTicket.getId());
	}

	/**
	 * 从URL参数、header和Cookie中获取Token
	 * 没有取到，返回null
	 * @return
	 */
	public String getAccessToken(HttpServletRequest req) {
		// 首先读取链接中的token
		String accessToken = req.getParameter(SessionConstants.ACCESS_TOKEN_KEY);
		if (StringUtils.isBlank(accessToken)) {
			accessToken = req.getParameter(SessionConstants.OAUTH_ACCESS_TOKEN_KEY);
		}
		if (StringUtils.isBlank(accessToken)) {
			accessToken = req.getHeader(SessionConstants.ACCESS_TOKEN_KEY);
		}
		if (StringUtils.isNotBlank(accessToken)) {
			WebContent.setCookie(SessionConstants.ACCESS_TOKEN_KEY, accessToken);
		} else {
			accessToken = WebContent.getCookieVal(SessionConstants.ACCESS_TOKEN_KEY);
		}
		return accessToken;
	}

	/**
	 * 从URL参数、header和Cookie中获取Token
	 * 没有取到，返回null
	 * @return
	 */
	public String getRefreshToken(HttpServletRequest req) {
		// 首先读取链接中的token
		String refreshToken = req.getParameter(SessionConstants.REFRESH_TOKEN_KEY);
		if (StringUtils.isBlank(refreshToken)) {
			refreshToken = req.getParameter(SessionConstants.OAUTH_REFRESH_TOKEN_KEY);
		}
		if (StringUtils.isBlank(refreshToken)) {
			refreshToken = req.getHeader(SessionConstants.REFRESH_TOKEN_KEY);
		}
		if (StringUtils.isNotBlank(refreshToken)) {
			WebContent.setCookie(SessionConstants.REFRESH_TOKEN_KEY, refreshToken);
		} else {
			refreshToken = WebContent.getCookieVal(SessionConstants.REFRESH_TOKEN_KEY);
		}
		return refreshToken;
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
			Map<String,Object> map = new HashMap<>();
			map.put("departmentId",userTicket.getDepartmentId());
			log.setNotes(JSONObject.toJSONString(map));
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
			return businessLogRpc.save(log, request.getHeader("referer"));
		} else {
			return BaseOutput.failure("请先登录");
		}
	}

}
