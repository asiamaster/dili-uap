package com.dili.uap.controller;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.redis.UserSystemRedis;
import com.dili.uap.sdk.redis.UserUrlRedis;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.MenuService;
import com.dili.uap.service.SystemService;
import com.dili.uap.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 首页控制器
 */
@Api("/index")
@Controller
@RequestMapping("/index")
public class IndexController {

	//跳转到首页
	public static final String INDEX_PATH = "index/leftMenuIndex";
	//跳转到平台首页
	public static final String PLATFORM_PATH = "index/platform";
	//跳转到首页Controller
	public static final String REDIRECT_INDEX_PAGE = "redirect:/index/index.html";

	public static final String USERDETAIL_PATH = "index/userDetail";
	public static final String CHANGEPWD_PATH = "index/changePwd";

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

	/**
	 * 跳转到权限主页面
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@ApiOperation("跳转到权限主页面")
	@RequestMapping(value = "/index.html", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap modelMap, HttpServletRequest request) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			//设置页面使用的用户id、名称和用户状态
			modelMap.put("userid", userTicket.getId());
			modelMap.put("username", userTicket.getRealName());
			//这里必须要从数据库取，因为从cookie取的话，用户修改完密码，再回到此页面也会弹出修改密码框，因为cookie没有刷新
			modelMap.put("userState", userService.get(userTicket.getId()).getState());
			String systemCode = request.getParameter("systemCode") == null ? UapConstants.UAP_SYSTEM_CODE : request.getParameter("systemCode");
			modelMap.put("systemCode", systemCode);
			if(systemCode.equals(UapConstants.UAP_SYSTEM_CODE)){
				Systems condition = DTOUtils.newInstance(Systems.class);
				condition.setCode(UapConstants.UAP_SYSTEM_CODE);
				List<Systems> uap = systemService.listByExample(condition);
				if(CollectionUtils.isEmpty(uap)){
					throw new AppException("未配置统一权限系统");
				}
				modelMap.put("system", DTOUtils.as(uap.get(0), Systems.class));
				//如果用户有任务中心的权限，则显示任务导航
				String taskCenterUrl = "http://bpmc.diligrp.com:8617/task/taskCenter.html";
				if(userResRedis.checkUserMenuUrlRight(userTicket.getId(), taskCenterUrl)){
					modelMap.put("taskCenterUrl", taskCenterUrl);
				}
				return INDEX_PATH;
			}
			List<Systems> systems = userSystemRedis.getRedisUserSystems(userTicket.getId());
			if(null == systems){
				return LoginController.REDIRECT_INDEX_PAGE;
			}
			for(Systems system : systems){
				if(systemCode.equals(system.getCode())){
					modelMap.put("system", system);
					break;
				}
			}
			//没有系统权限，则弹回登录页
			if(!modelMap.containsKey("system")){
				return LoginController.REDIRECT_INDEX_PAGE;
			}
			//如果用户有任务中心的权限，则显示任务导航
			String taskCenterUrl = "http://bpmc.diligrp.com:8617/task/taskCenter.html";
			if(userResRedis.checkUserMenuUrlRight(userTicket.getId(), taskCenterUrl)){
				modelMap.put("taskCenterUrl", taskCenterUrl);
			}
			return INDEX_PATH;
		} else {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
	}

	/**
	 * 跳转到平台页面
	 * @param modelMap
	 * @param req
	 * @return
	 */
	@ApiOperation("跳转到平台页面")
	@RequestMapping(value = "/platform.html", method = RequestMethod.GET)
	public String platform(ModelMap modelMap, HttpServletRequest req) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			List<Systems> systems = systemService.listByUserId(userTicket.getId());
			User user = userService.get(userTicket.getId());
			if(user == null){
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
	 * 获取sessionId
	 * @param req
	 * @return
	 */
	public String getSessionId(HttpServletRequest req) {
		String sessionId = null;
			//首先读取链接中的session
			sessionId = req.getParameter(SessionConstants.SESSION_ID);
			if(StringUtils.isBlank(sessionId)) {
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
	 * 跳转到功能列表页面
	 * @param systemCode
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到功能列表页面")
	@RequestMapping(value = "/featureList.html", method = RequestMethod.GET)
	public String featureList(String systemCode, ModelMap modelMap){
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		//获取系统下该用户有权限显示的菜单
		List<Map> menus = menuService.listDirAndLinks(userTicket.getId(), systemCode);
		modelMap.put("menus", menus);
		return "index/featureList";
	}

	/**
	 * 跳转到园区管理首页
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到园区管理首页")
	@RequestMapping(value = "/parkIndex.html", method = RequestMethod.GET)
	public String parkIndex(ModelMap modelMap) {
		return "index/parkIndex";
	}

	/**
	 * 判断是否包含统一权限平台
	 * @param systems
	 * @return
	 */
	private boolean containsUap(List<Systems> systems){
		for(Systems system : systems){
			if(UapConstants.UAP_SYSTEM_CODE.equals(system.getCode())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 跳转到个人信息页面
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到个人信息页面")
	@RequestMapping(value = "/userDetail.html", method = RequestMethod.GET)
	public String userDetail(ModelMap modelMap) {
		return USERDETAIL_PATH;
	}

	/**
	 * 跳转到修改密码页面
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到修改密码页面")
	@RequestMapping(value = "/changePwd.html", method = RequestMethod.GET)
	public String changePwd(ModelMap modelMap) {
		return CHANGEPWD_PATH;
	}

}
