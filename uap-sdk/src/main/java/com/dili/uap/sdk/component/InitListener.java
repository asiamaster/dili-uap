package com.dili.uap.sdk.component;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.constant.UapSdkConstants;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.sdk.rpc.SystemConfigRpc;
import com.dili.uap.sdk.session.SessionConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 配置初始化监听
 * Created by asiamaster on 2018/8/20
 */
@Component("uapSdkInitListener")
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitListener.class);

	@Autowired
	private SystemConfigRpc systemConfigRpc;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		//根据系统配置设置登录超时时长，默认为30分钟
		SystemConfig systemConfig = DTOUtils.newDTO(SystemConfig.class);
		systemConfig.setSystemCode(UapSdkConstants.UAP_SYSTEM_CODE);
		systemConfig.setCode(SessionConstants.SESSION_TIMEOUT_CONFIG_KEY);
		BaseOutput<List<SystemConfig>> output = systemConfigRpc.list(systemConfig);
		if(output.isSuccess()) {
			List<SystemConfig> systemConfigs = output.getData();
			if(CollectionUtils.isNotEmpty(systemConfigs)) {
				SessionConstants.SESSION_TIMEOUT = Long.parseLong(systemConfigs.get(0).getValue()) * 60;
			}
		}
	}


}
