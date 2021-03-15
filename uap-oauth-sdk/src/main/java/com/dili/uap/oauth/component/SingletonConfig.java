package com.dili.uap.oauth.component;

/**
 * 单例配置类
 * @author: WM
 * @time: 2021/3/11 9:49
 */
public class SingletonConfig {
    private static volatile SingletonConfig INSTANCE = null;
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
                    AUTH_SERVER_HOST = ApplicationContextUtil.getProperty("uap.oauthServer.contextPath", "https://as.diligrp.com");
                }
            }
        }
        return AUTH_SERVER_HOST;
    }
}
