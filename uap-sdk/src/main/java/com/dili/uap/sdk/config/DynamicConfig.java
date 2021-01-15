package com.dili.uap.sdk.config;

import com.dili.uap.sdk.glossary.SystemType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * UAP动态配置
 */
@RefreshScope
@Component
public class DynamicConfig {
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
	//JWT公钥
	private String publicKey;
	//JWT私钥
	private String privateKey;

	public String getPublicKey() {
		return publicKey;
	}

	@Value("${uap.jwt.publicKey:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz4D01cJbbLdzUprznyrz4bueMWkLZNSBHuxXjynn4WnaELTidvA6280h7WHP+87iNmZAtvrmcEWGPCBvGrNRFzpqtN7c8h6E12SESVWjuF4VkH/tUN/F4UJLtNPEnsmmVAdarwn/c5RJqFVA2sFVlm6Zc2FV3QyPdrdMfa9AizwIDAQAB}")
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	@Value("${uap.jwt.privateKey:MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALPgPTVwltst3NSmvOfKvPhu54xaQtk1IEe7FePKefhadoQtOJ28DrbzSHtYc/7zuI2ZkC2+uZwRYY8IG8as1EXOmq03tzyHoTXZIRJVaO4XhWQf+1Q38XhQku008SeyaZUB1qvCf9zlEmoVUDawVWWbplzYVXdDI92t0x9r0CLPAgMBAAECgYEAqCPLc4G8MkOLsmfuG0njHOMmpIbXCAzmEMcr7hOdse517JYM3z0kEBYXwdzsCP0vnYVXRbuL6vxAUqBEvpFdlhMYDNeDbKlqfWbvAa2RP6stib4OWR85gYbssRn3kh4IY1VWn+GeSbc5ztjSVXKnRbS+ezd0OmXJqiKzPpQtNMECQQDylOWkFeKgegAEzMXM/9VjjgXFoNb8AJVT8QXj2/m4ndL17/n4YHOwbMo0PDy69NKKMDAG3EnTNKBL0xIq2NMhAkEAvdNkMoI7Cedd35xG5bqB+GxWvrZPZN/QHhmQiUGO/CvslHL7QKeit4auDi30g3aUKbo07w/WfxL/me6yJRkn7wJAcXAtv0C4vOCwV45GxWmxqR+GFXf0cN349ssUPQzmR24OdBHnrD22e/8zw5+Tqr3IIvUL0Hl9UHYgq7Sln0HL4QJBAKn0u3Axg5SRb04GyL9kpnt63IuyBRGnBdn9P5h0dwW2egJLlENGE/zHe808PgD6SRu3GS+1eXGa2/jBawSmKkcCQGxLhtbCa08GrcQOHNYrtSfKRn+hJRKvwAWK4K64OGC94spgtPX5H3Ks3QxUGBWAtdlP+OVugfIfZ3Esim+2xSA=}")
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

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
	@Value("${uap.contextPath:http://uap.diligrp.com}")
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
	@Value("${uap.appAccessTokenTimeout:3600}")
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
	 * 移动端refreshToken超时，默认一个月
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