package com.dili.uap.controller;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.sdk.domain.System;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserSystemRedis;
import com.dili.uap.sdk.session.SessionContext;
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
import java.util.*;

@Api("/index")
@Controller
@RequestMapping("/index")
public class IndexController {

	//跳转到首页
	public static final String INDEX_PATH = "index/index";
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
				com.dili.uap.domain.System condition = DTOUtils.newDTO(com.dili.uap.domain.System.class);
				condition.setCode(UapConstants.UAP_SYSTEM_CODE);
				List<com.dili.uap.domain.System> uap = systemService.listByExample(condition);
				if(CollectionUtils.isEmpty(uap)){
					throw new AppException("未配置统一权限系统");
				}
				modelMap.put("system", DTOUtils.as(uap.get(0), System.class));
				return INDEX_PATH;
			}
			List<System> systems = userSystemRedis.getRedisUserSystems(userTicket.getId());
			for(System system : systems){
				if(systemCode.equals(system.getCode())){
					modelMap.put("system", system);
					break;
				}
			}
			//没有系统权限，则弹回登录页
			if(!modelMap.containsKey("system")){
				return LoginController.REDIRECT_INDEX_PAGE;
			}
			return INDEX_PATH;
		} else {
			return LoginController.REDIRECT_INDEX_PAGE;
		}
	}


	@ApiOperation("跳转到平台页面")
	@RequestMapping(value = "/platform.html", method = RequestMethod.GET)
	public String platform(ModelMap modelMap) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			List<System> systems = userSystemRedis.getRedisUserSystems(userTicket.getId());
//			判断是否包含统一权限平台，如果不包含则添加，保证用户一定能看到平台页面
//			if(!containsUap(systems)) {
//				com.dili.uap.domain.System condition = DTOUtils.newDTO(com.dili.uap.domain.System.class);
//				condition.setCode(UapConstants.UAP_SYSTEM_CODE);
//				List<com.dili.uap.domain.System> uap = systemService.listByExample(condition);
//				if (CollectionUtils.isEmpty(uap)) {
//					throw new AppException("未配置统一权限系统");
//				}
//				systems.add(DTOUtils.as(uap.get(0), System.class));
//			}
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
	 * 判断是否包含统一权限平台
	 * @param systems
	 * @return
	 */
	private boolean containsUap(List<System> systems){
		for(System system : systems){
			if(UapConstants.UAP_SYSTEM_CODE.equals(system.getCode())){
				return true;
			}
		}
		return false;
	}


	@ApiOperation("跳转到个人信息页面")
	@RequestMapping(value = "/userDetail.html", method = RequestMethod.GET)
	public String userDetail(ModelMap modelMap) {
		return USERDETAIL_PATH;
	}

	@ApiOperation("跳转到修改密码页面")
	@RequestMapping(value = "/changePwd.html", method = RequestMethod.GET)
	public String changePwd(ModelMap modelMap) {
		return CHANGEPWD_PATH;
	}

}
