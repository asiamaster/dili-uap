package com.dili.uap.sdk.session;


import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.service.AuthDataApiService;
import com.dili.uap.sdk.service.UserInfoApiService;
import com.dili.uap.sdk.util.WebContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionContext {
	private static ThreadLocal<SessionContext> holder = new ThreadLocal<>();

	private PermissionContext pc;

	private UserTicket userTicket;

	private AuthDataApiService authDataApiService;

	private UserInfoApiService userInfoApiService;

	public static void resetLocal() {
		holder.remove();
	}

	private SessionContext() {
		PermissionContext config = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
		this.pc = config;
		holder.set(this);
	}

	public static synchronized SessionContext getSessionContext() {
		SessionContext context = holder.get();
		if (context == null) {
			new SessionContext();
		}
		return holder.get();
	}

	public static synchronized void remove() {
		holder.remove();
	}

	public UserTicket getUserTicket() {
		if (userTicket == null) {
			userTicket = pc.getUserRedis().getUser(pc.getSessionId());
		}
		return userTicket;
	}

	public ManageConfig getManageConfig(){
		return pc.getConfig();
	}

	/**
	 * 这个方法好像是个后门，暂时改为私有方法
	 * @return
	 */
	private UserTicket getAuthorizer(){
		return pc.getAuthorizer();
	}

	/**
	 * 获取当前数据权限DataAuth 的Map
	 * @return
	 */
	public List<Map> dataAuth() {
		return pc.getDataAuthRedis().dataAuth(getUserTicket().getId());
	}

	/**
	 * 刷新数据权限
	 * @param type
	 */
	public void refreshAuthData(String type){
		if (authDataApiService == null) {
			authDataApiService = new AuthDataApiService("", pc.getConfig().getDomain());
		}
		authDataApiService.refreshAuthData(type);
	}

	public UserInfoApiService fetchUserApi(){
		if (userInfoApiService == null) {
			userInfoApiService = new UserInfoApiService("", pc.getConfig().getDomain());
		}
		return userInfoApiService;
	}

	/**
	 * 获取指定的数据权限
	 * 
	 * @param type
	 * @return
	 */
	public List<Map> dataAuth(String type) {
		List<Map> list = new ArrayList<>();
		if (getUserTicket() == null || getUserTicket().getId() == null) {
			return list;
		}
		return WebContent.getPC().getDataAuthRedis().dataAuth(type, getUserTicket().getId());
	}

	/**
	 * 判断是否可以访问
	 * 
	 * @param method
	 * @param url
	 * @return
	 */
	public static synchronized boolean hasAccess(String method, String url) {
		return WebContent.getPC().getUserResRedis().checkUserMenuUrlRight(WebContent.getPC().getUserId(), url);
	}

}
