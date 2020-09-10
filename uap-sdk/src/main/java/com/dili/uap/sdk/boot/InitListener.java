package com.dili.uap.sdk.boot;

import com.dili.uap.sdk.session.ManageConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 配置初始化监听
 * 载入Includes和Excludes的扩展Ext配置
 * Created by asiamaster on 2018/8/20
 */
@Component("uapSdkInitListener")
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ManageConfig manageConfig;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if(!(contextRefreshedEvent.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext)) {
			return;
		}
		if (CollectionUtils.isNotEmpty(manageConfig.getIncludes()) && CollectionUtils.isNotEmpty(manageConfig.getIncludesExt())) {
			manageConfig.getIncludes().addAll(manageConfig.getIncludesExt());
			manageConfig.setIncludesExt(null);
		}
		if (CollectionUtils.isNotEmpty(manageConfig.getExcludes()) && CollectionUtils.isNotEmpty(manageConfig.getExcludesExt())) {
			manageConfig.getExcludes().addAll(manageConfig.getExcludesExt());
			manageConfig.setExcludesExt(null);
		}
	}

}
