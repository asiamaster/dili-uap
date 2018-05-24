package com.dili.uap.component;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.quartz.service.ScheduleJobService;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.SystemConfig;
import com.dili.uap.domain.User;
import com.dili.uap.glossary.UserState;
import com.dili.uap.service.SystemConfigService;
import com.dili.uap.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * 恢复锁定用户调度器
 * Created by asiamaster on 2018/5/24
 */
//@Component
public class ResumeLockedUserJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResumeLockedUserJob.class);

	@Autowired
	private ScheduleJobService scheduleJobService;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemConfigService systemConfigService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
//			List<ScheduleJob> scheduleJobs = scheduleJobService.list(null);
//			for (ScheduleJob job : scheduleJobs) {
//				scheduleJobService.addJob(job, true);
//			}
//		}
	}

	/**
	 * 抽取客户数据
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {

	}

	/**
	 * 恢复锁定用户
	 */
	private void resumeLockedUser(){
		User user = DTOUtils.newDTO(User.class);
		user.setState(UserState.LOCKED.getCode());
		List<User> lockedUsers = userService.listByExample(user);
		if(lockedUsers.isEmpty()){
			return;
		}
		Long now = System.currentTimeMillis();
		SystemConfig systemConfig = DTOUtils.newDTO(SystemConfig.class);
		systemConfig.setYn(1);
		systemConfig.setCode(UapConstants.SYSTEM_CODE);
//		systemConfig.set
		systemConfigService.listByExample(systemConfig);
		lockedUsers.forEach( lockedUser -> {
			if((now - lockedUser.getModified().getTime()) > 0){

			}
		});
	}

}
