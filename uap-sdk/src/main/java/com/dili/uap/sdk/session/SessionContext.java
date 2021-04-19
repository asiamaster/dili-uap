package com.dili.uap.sdk.session;

import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.config.ManageConfig;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.service.AuthService;
import com.dili.uap.sdk.service.UserInfoApiService;
import com.dili.uap.sdk.service.redis.DataAuthRedis;
import com.dili.uap.sdk.util.WebContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionContext {
	private static ThreadLocal<SessionContext> holder = new ThreadLocal<>();

	private PermissionContext pc;

	private UserTicket userTicket;

	private UserInfoApiService userInfoApiService;

	private DataAuthRedis dataAuthRedis;

	private AuthService authService;

	public static void resetLocal() {
		holder.remove();
	}

	private SessionContext() {
		PermissionContext config = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
		this.pc = config;
		dataAuthRedis = SpringUtil.getBean(DataAuthRedis.class);
		authService = SpringUtil.getBean(AuthService.class);
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
			userTicket = authService.getUserTicket(pc.getAccessToken(), pc.getRefreshToken());
		}
		return userTicket;
	}

	public UserTicket getOAuthUserTicket() {
		if (userTicket == null) {
			userTicket = authService.getOAuthUserTicket(pc.getAccessToken(), pc.getRefreshToken());
		}
		return userTicket;
	}

	public ManageConfig getManageConfig() {
		return pc.getConfig();
	}

	public String getDomain() {
		return pc.getDomain();
	}

	/**
	 * 获取当前数据权限DataAuth 的Map
	 * 
	 * @return
	 */
	public List<Map> dataAuth() {
		return dataAuthRedis.dataAuth(getUserTicket().getId());
	}

	public UserInfoApiService fetchUserApi() {
		if (userInfoApiService == null) {
			userInfoApiService = new UserInfoApiService("", pc.getDomain());
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
		return dataAuthRedis.dataAuth(type, getUserTicket().getId());
	}

}
