package com.dili.uap.oauth.component;

import com.dili.uap.oauth.config.OauthConfig;
import org.springframework.util.StringUtils;

/**
 * 单例配置类
 * @author: WM
 * @time: 2021/3/11 9:49
 */
public class SingletonConfig {
    private static volatile String AUTH_SERVER_HOST = null;

    private SingletonConfig() {}

//    public static SingletonConfig getInstance() {
//        if (INSTANCE == null) {
//            synchronized (SingletonConfig.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new SingletonConfig();
//                }
//            }
//        }
//        return INSTANCE;
//    }

    public static String getAuthServerHost() {
        if (AUTH_SERVER_HOST == null) {
            synchronized (SingletonConfig.class) {
                if (AUTH_SERVER_HOST == null) {
                    OauthConfig oauthConfig = ApplicationContextUtil.getBean("oauthConfig", OauthConfig.class);
                    if(oauthConfig == null){
                        throw new RuntimeException("com.dili.uap.oauth.config.OauthConfig类未被spring扫描");
                    }
                    AUTH_SERVER_HOST = oauthConfig.getOauthServerPath();
                    if(StringUtils.isEmpty(AUTH_SERVER_HOST)){
                        throw new RuntimeException("oauth.serverPath未配置");
                    }
                }
            }
        }
        return AUTH_SERVER_HOST;
    }
}
