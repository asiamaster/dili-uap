package com.dili.uap.sdk.session;

public class SessionConstants {
    
	public static final String SESSION_KEY_PREFIX = "DILI_MANAGE_SESSION_";
	public static final String CACHED_RESOURCE_LIST_KEY = "DILI_MANAGE_RESOURCE_LIST";
    //COMMON begin
    public static final String  LOGGED_USER             =        "common:loggedUser";
    public static final String  SESSION_ID              =       "SessionId";
    public static final String  AUTH_KEY              =       "authKey";

    public static final String MANAGE_PERMISSION_CONTEXT = "manage.permission_context";

    public  static Long SESSION_TIMEOUT = 60 * 30L;  // 30分钟
    public static Long SESSIONID_USERID_TIMEOUT = 60 * 60 * 24L;
    public  static Integer COOKIE_TIMEOUT = SESSION_TIMEOUT.intValue() * 48;

    // 新的redis关系 - kv定义表 - START
    // userRole(用户角色) redis的key
    public static final String USER_CURRENT_KEY = "manage:current:userId:";

    // userUrl(用户和菜单URL关系)
    public static final String USER_MENU_URL_KEY = "manage:userMenuUrl:userId:";
    public static final String USER_RESOURCE_CODE_KEY ="manage:userResourceCode:userId";
    // sessionId - userId
    public static final String SESSION_USERID_KEY = "manage:sessionUserId:sessionId";
    // userId - sessionId
    public static final String USERID_SESSION_KEY = "manage:userIdSession:userId:";

    // 限制用户唯一登陆 - START
    public static final String KICK_OLDSESSIONID_KEY = "manage:kickOldSessionId:";
    // 限制用户唯一登陆 - END
    //登录会话id
    public static final String COOKIE_SESSION_ID = "SessionId";

    //登录密码错误锁定的key
    public static final String USER_PWD_ERROR_KEY = "manage:user_pwd_error:";
}