package com.dili.uap.component;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.SystemConfigMapper;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.sdk.session.SessionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 配置初始化监听
 * Created by asiamaster on 2018/5/24
 */
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

//	private static final Logger LOGGER = LoggerFactory.getLogger(InitListener.class);

	@Autowired
	private SystemConfigMapper systemConfigMapper;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		//根据系统配置设置登录超时时长，默认为30分钟
		SystemConfig systemConfig = DTOUtils.newDTO(SystemConfig.class);
		systemConfig.setSystemCode(UapConstants.UAP_SYSTEM_CODE);
		systemConfig.setCode(SessionConstants.SESSION_TIMEOUT_CONFIG_KEY);
		systemConfig = systemConfigMapper.selectOne(systemConfig);
		SessionConstants.SESSION_TIMEOUT = Long.parseLong(systemConfig.getValue()) * 60;
	}


}
