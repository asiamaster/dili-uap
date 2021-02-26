package com.dili.uap.component;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.SystemConfigMapper;
import com.dili.uap.domain.ScheduleMessage;
import com.dili.uap.glossary.UserState;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 恢复锁定用户调度器
 * Created by asiamaster on 2018/5/24
 */
@Component
//public class ResumeLockedUserJob implements ApplicationListener<ContextRefreshedEvent> {
public class ResumeLockedUserJob implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResumeLockedUserJob.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SystemConfigMapper systemConfigMapper;

//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//		if(!(contextRefreshedEvent.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext)) {
//			return;
//		}
//		//系统启动时扫描一次锁定用户
//        resumeLockedUser();
//	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		resumeLockedUser();
	}

	/**
	 * 调度主方法
	 * 恢复锁定用户
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
        resumeLockedUser();
	}

	/**
	 * 恢复锁定用户
	 */
	private void resumeLockedUser(){
		User user = DTOUtils.newInstance(User.class);
		user.setState(UserState.LOCKED.getCode());
		List<User> lockedUsers = userService.listByExample(user);
		if(lockedUsers.isEmpty()){
			return;
		}
		Long now = System.currentTimeMillis();
		//查询锁定用户恢复时长
		SystemConfig systemConfigCondition = DTOUtils.newInstance(SystemConfig.class);
		systemConfigCondition.setCode(UapConstants.RESUME_DURATION);
        systemConfigCondition.setSystemCode(UapConstants.UAP_SYSTEM_CODE);
		SystemConfig systemConfig = systemConfigMapper.selectOne(systemConfigCondition);
        Long resumeDuration = Long.parseLong(systemConfig.getValue()) * 1000;
		lockedUsers.forEach( lockedUser -> {
		    //超过锁定时长就解锁
			if((now - lockedUser.getLocked().getTime()) >= resumeDuration){
                lockedUser.setState(UserState.NORMAL.getCode());
                userService.updateSelective(lockedUser);
			}
		});
	}

}
