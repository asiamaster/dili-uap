package com.dili.uap.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.bpmc.sdk.dto.GroupUserDto;
import com.dili.bpmc.sdk.dto.TaskIdentityDto;
import com.dili.bpmc.sdk.rpc.restful.TaskRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.dao.UserRoleMapper;
import com.dili.uap.domain.UserRole;
import com.dili.uap.sdk.domain.dto.ProcessHandleInfoDto;
import com.dili.uap.sdk.session.SessionContext;

import tk.mybatis.mapper.entity.Example;

/**
 * 流程引擎工具类
 */
@Component
public class BpmcUtil {

	@Autowired
	private TaskRpc taskRpc;
	@Autowired
	private UserRoleMapper userRoleMapper;

	/**
	 * 设置流程属性
	 * 
	 * @param dtos
	 */
	public void fitLoggedUserIsCanHandledProcess(List<? extends ProcessHandleInfoDto> dtos) {
		// 根据流程实例批量查询任务
		Set<String> processInstanceIds = new HashSet<>();
		dtos.forEach(d -> {
			if (StringUtils.isNotBlank(d.getProcessInstanceId())) {
				processInstanceIds.add(d.getProcessInstanceId());
			}
		});
		if (CollectionUtils.isEmpty(processInstanceIds)) {
			return;
		}
		BaseOutput<List<TaskIdentityDto>> tiOutput = this.taskRpc.listTaskIdentityByProcessInstanceIds(new ArrayList<String>(processInstanceIds));
		if (!tiOutput.isSuccess()) {
			return;
		}
		if (CollectionUtils.isEmpty(tiOutput.getData())) {
			return;
		}
		Set<Long> roleIds = new HashSet<>();
		tiOutput.getData().forEach(ti -> {
			ti.getGroupUsers().forEach(gu -> {
				if (StringUtils.isNotBlank(gu.getGroupId())) {
					roleIds.add(Long.valueOf(gu.getGroupId()));
				}
			});
		});
		Example example = new Example(UserRole.class);
		example.createCriteria().andIn("roleId", roleIds);
		List<UserRole> userRoleList = this.userRoleMapper.selectByExample(example);
		Long userId = SessionContext.getSessionContext().getUserTicket().getId();
		dtos.forEach(d -> {
			if (StringUtils.isBlank(d.getProcessInstanceId())) {
				return;
			}
			for (TaskIdentityDto taskIdentity : tiOutput.getData()) {
				if (taskIdentity.getProcessInstanceId().equals(d.getProcessInstanceId())) {
					d.setFormKey(taskIdentity.getFormKey());
					d.setTaskId(taskIdentity.getTaskId());
					if (StringUtils.isNotBlank(taskIdentity.getAssignee()) && Long.valueOf(taskIdentity.getAssignee()).equals(userId)) {
						d.setIsHandleProcess(true);
						d.setIsNeedClaim(false);
					} else {
						GroupUserDto groupUser = taskIdentity.getGroupUsers().stream().filter(gu -> {
							return userRoleList.stream().filter(ru -> ru.getUserId().toString().equals(gu.getUserId()) || ru.getRoleId().toString().equals(gu.getGroupId())).findFirst()
									.orElse(null) != null;
						}).findFirst().orElse(null);
						d.setIsHandleProcess(groupUser != null);
						d.setIsNeedClaim(groupUser != null);
					}
				}

			}
		});
	}

}
