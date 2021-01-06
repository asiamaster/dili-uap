package com.dili.uap.sdk.session;

import com.dili.uap.sdk.glossary.SystemType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * session动态配置
 */
@RefreshScope
@Component
public class DynaSessionConstants {
	//WEB端超时, 单位(秒)
	//默认为1800秒
	private Long WEB_ACCESS_TOKEN_TIMEOUT;
	//默认3600秒
	private Long WEB_REFRESH_TOKEN_TIMEOUT;
	//移动端超时, 单位(秒)
	//默认为1800秒
	private Long APP_ACCESS_TOKEN_TIMEOUT;
	// 默认为30天(2592000秒)
	private Long APP_REFRESH_TOKEN_TIMEOUT;
	//uap上下文路径
	private String UAP_CONTEXT_PATH;

	/**
	 * uap上下文路径
	 * @return
	 */
	public String getUapContextPath() {
		return UAP_CONTEXT_PATH;
	}

	/**
	 * uap上下文路径
	 * @return
	 */
	@Value("${uap.contextPath}")
	public void setUapContextPath(String uapContextPath) {
		UAP_CONTEXT_PATH = uapContextPath;
	}

	/**
	 * WEB端accessToken超时
	 * @return
	 */
	public Long getWebAccessTokenTimeout() {
		return WEB_ACCESS_TOKEN_TIMEOUT;
	}

	/**
	 * WEB端accessToken超时
	 * @param webAccessTokenTimeout
	 */
	@Value("${uap.webAccessTokenTimeout:1800}")
	public void setWebAccessTokenTimeout(Long webAccessTokenTimeout) {
		WEB_ACCESS_TOKEN_TIMEOUT = webAccessTokenTimeout;
	}

	/**
	 * WEB端refreshToken超时
	 * @return
	 */
	public Long getWebRefreshTokenTimeout() {
		return WEB_REFRESH_TOKEN_TIMEOUT;
	}

	/**
	 * WEB端refreshToken超时
	 * @param webRefreshTokenTimeout
	 */
	@Value("${uap.webRefreshTokenTimeout:3600}")
	public void setWebRefreshTokenTimeout(Long webRefreshTokenTimeout) {
		WEB_REFRESH_TOKEN_TIMEOUT = webRefreshTokenTimeout;
	}

	/**
	 * 移动端accessToken超时
	 * @return
	 */
	public Long getAppAccessTokenTimeout() {
		return APP_ACCESS_TOKEN_TIMEOUT;
	}

	/**
	 * 移动端accessToken超时
	 * @param appAccessTokenTimeout
	 */
	@Value("${uap.appAccessTokenTimeout:1800}")
	public void setAppAccessTokenTimeout(Long appAccessTokenTimeout) {
		APP_ACCESS_TOKEN_TIMEOUT = appAccessTokenTimeout;
	}

	/**
	 * 移动端refreshToken超时
	 * @return
	 */
	public Long getAppRefreshTokenTimeout() {
		return APP_REFRESH_TOKEN_TIMEOUT;
	}

	/**
	 * 移动端refreshToken超时
	 * @param appRefreshTokenTimeout
	 */
	@Value("${uap.appRefreshTokenTimeout:2592000}")
	public void setAppRefreshTokenTimeout(Long appRefreshTokenTimeout) {
		APP_REFRESH_TOKEN_TIMEOUT = appRefreshTokenTimeout;
	}



	// =================================================================================

	/**
	 * 获取accessToken超时
	 * @param systemType
	 * @return
	 */
	public Long getAccessTokenTimeout(Integer systemType){
		return systemType.equals(SystemType.WEB.getCode()) ? getWebAccessTokenTimeout() : getAppAccessTokenTimeout();
	}

	/**
	 * 获取refreshToken超时
	 * @param systemType
	 * @return
	 */
	public Long getRefreshTokenTimeout(Integer systemType){
		return systemType.equals(SystemType.WEB.getCode()) ? getWebRefreshTokenTimeout() : getAppRefreshTokenTimeout();
	}
}