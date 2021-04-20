package com.dili.uap.oauth.component;

import com.dili.uap.oauth.config.OauthConfig;
import org.springframework.util.StringUtils;

/**
 * 单例配置类
 * 用于非spring类获取配置
 * @author: WM
 * @time: 2021/3/11 9:49
 */
public class SingletonConfig {
    private static volatile String AUTH_SERVER_PATH = null;
    private static volatile String AUTH_RESOURCE_PATH = null;
    private static volatile Integer AUTH_HTTP_TIMEOUT = null;
    private SingletonConfig() {}

    /**
     * 用于AuthUapSource中配置的验证服务器的路径(http://域名:IP)
     * @return
     */
    public static String getAuthServerPath() {
        if (AUTH_SERVER_PATH == null) {
            synchronized (SingletonConfig.class) {
                if (AUTH_SERVER_PATH == null) {
                    OauthConfig oauthConfig = ApplicationContextUtil.getBean("oauthConfig", OauthConfig.class);
                    if(oauthConfig == null){
                        throw new RuntimeException("com.dili.uap.oauth.config.OauthConfig类未被spring扫描");
                    }
                    AUTH_SERVER_PATH = oauthConfig.getOauthServerPath();
                    if(StringUtils.isEmpty(AUTH_SERVER_PATH)){
                        throw new RuntimeException("oauth.serverPath未配置");
                    }
                }
            }
        }
        return AUTH_SERVER_PATH;
    }

    /**
     * 用于AuthUapSource中配置的资源服务器的路径(http://域名:IP)
     * @return
     */
    public static String getAuthResourcePath() {
        if (AUTH_RESOURCE_PATH == null) {
            synchronized (SingletonConfig.class) {
                if (AUTH_RESOURCE_PATH == null) {
                    OauthConfig oauthConfig = ApplicationContextUtil.getBean("oauthConfig", OauthConfig.class);
                    if(oauthConfig == null){
                        throw new RuntimeException("com.dili.uap.oauth.config.OauthConfig类未被spring扫描");
                    }
                    AUTH_RESOURCE_PATH = oauthConfig.getOauthResourcePath();
                    if(StringUtils.isEmpty(AUTH_RESOURCE_PATH)){
                        throw new RuntimeException("oauth.serverPath未配置");
                    }
                }
            }
        }
        return AUTH_RESOURCE_PATH;
    }

    /**
     * 获取accessToken和刷新token的远程调用超时
     * @return
     */
    public static Integer getHttpTimeout() {
        if (AUTH_HTTP_TIMEOUT == null) {
            synchronized (SingletonConfig.class) {
                if (AUTH_HTTP_TIMEOUT == null) {
                    OauthConfig oauthConfig = ApplicationContextUtil.getBean("oauthConfig", OauthConfig.class);
                    if(oauthConfig == null){
                        throw new RuntimeException("com.dili.uap.oauth.config.OauthConfig类未被spring扫描");
                    }
                    AUTH_HTTP_TIMEOUT = oauthConfig.getHttpTimeout();
                    if(StringUtils.isEmpty(AUTH_HTTP_TIMEOUT)){
                        throw new RuntimeException("oauth.httpTimeout未配置");
                    }
                }
            }
        }
        return AUTH_HTTP_TIMEOUT;
    }
}
