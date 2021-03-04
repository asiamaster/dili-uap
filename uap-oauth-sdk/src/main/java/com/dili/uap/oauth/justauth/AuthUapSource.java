package com.dili.uap.oauth.justauth;

import me.zhyd.oauth.config.AuthSource;

/**
 * 为了提供OAuth平台的API地址的统一接口
 * @author: WM
 * @time: 2021/3/2 15:10
 */
public enum AuthUapSource implements AuthSource {
    UAP{
        /**
         * 授权的api
         * @return
         */
        @Override
        public String authorize() {
            return "http://uap.diligrp.com:8081/api/oauth-server/authorize";
        }

        /**
         * 获取accessToken的api
         * @return
         */
        @Override
        public String accessToken() {
            return "http://uap.diligrp.com:8081/api/oauth-server/token";
        }

        /**
         * 获取用户信息的api
         * @return
         */
        @Override
        public String userInfo() {
            return "http://uap.diligrp.com:8081/api/oauth-server/user";
        }

        /**
         * 获取刷新token的api
         * @return
         */
        @Override
        public String refresh() {
            return "http://uap.diligrp.com:8081/api/oauth-server/token";
        }
    }
}
