package com.dili.uap.sdk.boot;

import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.exception.NotAccessPermissionException;
import com.dili.uap.sdk.exception.NotLoginException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

/**
 * 权限的web配置
 * Created by asiamaster on 2017/12/20 0020.
 */
@Configuration
public class AdminConfig {
	/**
	 * 统一错误页面处理
	 * 配置:
	 * error.page.default=error/default (错误页面的controller返回地址)
	 * error.page.indexPage=http://crm.diligrp.com:8085/crm/index.html (返回首页地址)
	 *
	 */
	@Bean("uapSimpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
		SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
//		定义默认的异常处理页面
		simpleMappingExceptionResolver.setDefaultErrorView("error/default");
//		定义异常处理页面用来获取异常信息的变量名，如果不添加exceptionAttribute属性，则默认为exception
		simpleMappingExceptionResolver.setExceptionAttribute("exception");
//		定义需要特殊处理的异常，用类名或完全路径名作为key，异常页面名作为值
		Properties mappings = new Properties();
		mappings.put("java.lang.RuntimeException", SpringUtil.getProperty("error.page.default", "error/default"));
		mappings.put("java.lang.Exception", SpringUtil.getProperty("error.page.default", "error/default"));
		mappings.put("java.lang.Throwable", SpringUtil.getProperty("error.page.default", "error/default"));
		//直接跳到/templates/error/uapNoLogin.html
		mappings.put(NotLoginException.class.getName(), "/error/noLogin");
		mappings.put(NotAccessPermissionException.class.getName(), "/error/noLogin");
		simpleMappingExceptionResolver.setExceptionMappings(mappings);
		return simpleMappingExceptionResolver;
	}
}
