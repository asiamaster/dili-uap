package com.dili.uap.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * UAP oauth动态配置
 */
@RefreshScope
@Component
public class OauthConfig {

	//uap上下文路径
	private String uapContextPath;
	//客户端id
	private String clientId;
	//客户端密钥
	private String clientSecret;



	/**
	 * uap上下文路径
	 * @return
	 */
	public String getUapContextPath() {
		return uapContextPath;
	}

	/**
	 * uap上下文路径
	 * @return
	 */
	@Value("${uap.contextPath:http://uap.diligrp.com}")
	public void setUapContextPath(String uapContextPath) {
		this.uapContextPath = uapContextPath;
	}

	/**
	 * 获取客户端id
	 * @return
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * 设置客户端id
	 * @param clientId
	 */
	@Value("${oauth.clientId:}")
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * 获取客户端密钥
	 * @return
	 */

	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * 设置客户端密钥
	 * @param clientSecret
	 */
	@Value("${oauth.clientSecret:}")
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}