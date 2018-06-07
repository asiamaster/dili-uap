package com.dili.uap.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DataAuthMapper;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.domain.dto.UserDataAuthCondition;
import com.dili.uap.manager.DataAuthManager;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据权限redis管理器
 * Created by asiam
 */
@Component
public class DataAuthManagerImpl implements DataAuthManager {
	private final static Logger LOG = LoggerFactory.getLogger(DataAuthManagerImpl.class);

	@Autowired
	private DataAuthMapper dataAuthMapper;
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private ManageRedisUtil redisUtil;

	@Override
	public void initUserDataAuthesInRedis(Long userId) {
		//查询数据权限，需要合并下面的部门数据权限列表
		List<DataAuth> dataAuths = this.dataAuthMapper.listByUserId(userId);
		//部门数据权限直接从用户部门关系表查询
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
		List<DataAuth> departmentsDataAuth = departmentMapper.listDataAuthes(param);
		//合并
		dataAuths.addAll(departmentsDataAuth);
		String key = SessionConstants.USER_DATA_AUTH_KEY + userId;
		this.redisUtil.remove(key);
		if(CollectionUtils.isEmpty(dataAuths)){
			return;
		}
		BoundSetOperations<String, Object> ops = this.redisUtil.getRedisTemplate().boundSetOps(key);
		for (DataAuth dataAuth : dataAuths) {
			ops.add(JSON.toJSONString(dataAuth));
		}
	}

	@Override
	public List<DataAuth> listUserDataAuthesByType(Long userId, String dataType) {
		String key = SessionConstants.USER_DATA_AUTH_KEY + userId + ":" + dataType;
		String json = this.redisUtil.get(key, String.class);
		List<DataAuth> dataAuthes = null;
		if (StringUtil.isEmpty(json)) {
			//单独处理用户部门数据权限
			if(dataType.equals(DataAuthType.DEPARTMENT.getCode())){
				//部门数据权限直接从用户部门关系表查询
				Map<String, Object> param = new HashMap<>();
				param.put("userId", userId);
				dataAuthes = departmentMapper.listDataAuthes(param);
			}else {
				UserDataAuthCondition userDataAuthCondition = DTOUtils.newDTO(UserDataAuthCondition.class);
				userDataAuthCondition.setUserId(userId);
				userDataAuthCondition.setType(dataType);
				dataAuthes = this.dataAuthMapper.list(userDataAuthCondition);
			}
			if (CollectionUtils.isEmpty(dataAuthes)) {
				return null;
			}
			BoundSetOperations<String, Object> ops = this.redisUtil.getRedisTemplate().boundSetOps(key);
			for (DataAuth dataAuth : dataAuthes) {
				ops.add(JSON.toJSONString(dataAuth));
			}
		}
		return dataAuthes;
	}

	@Override
	public List<DataAuth> listUserDataAuthes(Long userId) {
		//查询数据权限，需要合并下面的部门数据权限列表
		List<DataAuth> dataAuths = this.dataAuthMapper.listByUserId(userId);
		//部门数据权限直接从用户部门关系表查询
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		List<DataAuth> departmentsDataAuth = departmentMapper.listDataAuthes(param);
		//合并
		dataAuths.addAll(departmentsDataAuth);
		return dataAuths;
	}

	@Override
	public List<DataAuth> listUserDepartmentDataAuthes(Long userId, String firmCode){
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("firmCode", firmCode);
		return departmentMapper.listDataAuthes(param);
	}

}