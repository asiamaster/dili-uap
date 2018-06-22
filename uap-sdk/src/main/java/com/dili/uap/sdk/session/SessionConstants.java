package com.dili.uap.sdk.session;

public class SessionConstants {

    //Session Data的key prefix，后面加sessionId
	public static final String SESSION_KEY_PREFIX = "DILI_MANAGE_SESSION_";
    //SessionData中取登录用户的key
    public static final String  LOGGED_USER             =        "common:loggedUser";
    //Cookie, Request, Header, Session中取sessionId的key
    public static final String  SESSION_ID              =       "SessionId";
    //SessionFilter中checkUser方法最后从Redis中获取UserTicket的key
    public static final String  AUTH_KEY              =       "authKey";

    //WebContent中获取PermissionContext的key
    public static final String MANAGE_PERMISSION_CONTEXT = "manage.permission_context";

    //系统配置表中超时配置的code
    public static final String SESSION_TIMEOUT_CONFIG_KEY = "loginTimeout";
    // sessionId - SessionData的Redis 过期时间(秒)
    // sessionId - userId和UserIdSessionData的Redis 过期时间(秒)
    public static Long SESSION_TIMEOUT = 60 * 30L;  // 30分钟
    // sessionId - userId和UserIdSessionData的Redis 过期时间(秒)
//    public static Long SESSIONID_USERID_TIMEOUT = 60 * 60 * 24L;
    //Cookie过期时间(秒)
    public  static Integer COOKIE_TIMEOUT = SESSION_TIMEOUT.intValue() * 48;

    // 新的redis关系 - kv定义表 - START
    // 用户数据权限 redis的key
    public static final String USER_DATA_AUTH_KEY = "manage:dataAuth:userId:";

    // userSystem(用户和系统关系)
    public static final String USER_SYSTEM_KEY = "manage:userSystem:userId:";
    // userUrl(用户和菜单URL关系)
    public static final String USER_MENU_URL_KEY = "manage:userMenuUrl:userId:";
    // key:userId - value:resources Set
    public static final String USER_RESOURCE_CODE_KEY ="manage:userResourceCode:userId:";
    // key:sessionId - value:userId
    public static final String SESSIONID_USERID_KEY = "manage:sessionIdUserId:sessionId:";
    // key:userId - value:sessionId
    public static final String USERID_SESSIONID_KEY = "manage:userIdSessionId:userId:";

    // 限制用户唯一登陆 - START
    public static final String KICK_OLDSESSIONID_KEY = "manage:kickOldSessionId:";
    // 限制用户唯一登陆 - END
    //登录会话id
    public static final String COOKIE_SESSION_ID = "SessionId";

    //登录密码错误锁定的key
    public static final String USER_PWD_ERROR_KEY = "manage:user_pwd_error:";
}