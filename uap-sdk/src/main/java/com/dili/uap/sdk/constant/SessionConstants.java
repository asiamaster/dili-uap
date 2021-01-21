package com.dili.uap.sdk.constant;

/**
 * session常量
 */
public class SessionConstants {
	// 系统名称前缀
	public static final String APPLICATION_NAME = "UAP_";
	// Session Data的key prefix，后面加sessionId
	public static final String REFRESH_TOKEN_KEY_PREFIX = APPLICATION_NAME + "refreshToken:";
	// Cookie, Request, Header, Session中取sessionId的key
	public static final String SESSION_ID = APPLICATION_NAME + "sessionId";
	//web socket放入属性的用户id key
	public static final String USER_ID_KEY = APPLICATION_NAME + "userId";

	public static final String ACCESS_TOKEN_KEY = APPLICATION_NAME + "accessToken";
	public static final String REFRESH_TOKEN_KEY = APPLICATION_NAME + "refreshToken";

	// WebContent中获取PermissionContext的key
	public static final String MANAGE_PERMISSION_CONTEXT = APPLICATION_NAME + "manage.permission_context";

	// userSystem(用户和系统关系)
	public static final String USER_SYSTEM_KEY = APPLICATION_NAME + "userSystem:userId:";
	// userUrl(用户和菜单URL关系)
	public static final String USER_MENU_URL_KEY = APPLICATION_NAME + "userMenuUrl:userId:";
	// 用户resource关系
	public static final String USER_RESOURCE_CODE_KEY = APPLICATION_NAME + "userResourceCode:userId:";
	// 用户数据权限关系
	public static final String USER_DATA_AUTH_KEY = APPLICATION_NAME + "dataAuth:userId:";

	// key:userId - value:refreshToken
	public static final String USERID_REFRESH_TOKEN_KEY = APPLICATION_NAME + "userIdRefreshToken:userId:";

	// 登录密码错误锁定的key
	public static final String USER_PWD_ERROR_KEY = APPLICATION_NAME + "user_pwd_error:";

	// Cookie key
	// Cookie过期时间(秒) 1天
	public static Integer COOKIE_TIMEOUT = 86400;
	// 登录accessToken
//	public static final String COOKIE_ACCESS_TOKEN = ACCESS_TOKEN_KEY;
	// 登录refreshToken
//	public static final String COOKIE_REFRESH_TOKEN = REFRESH_TOKEN_KEY;
//	// 用户id
//	public static final String COOKIE_USER_ID_KEY = APPLICATION_NAME + "userId";
//	// 用户名
//	public static final String COOKIE_USER_NAME_KEY = APPLICATION_NAME + "username";
//	// 市场id
//	public static final String COOKIE_FIRM_ID_KEY = APPLICATION_NAME + "firmId";
	// 登录地址referer
	public static final String COOKIE_LOGIN_PATH_KEY = APPLICATION_NAME + "loginPath";

}