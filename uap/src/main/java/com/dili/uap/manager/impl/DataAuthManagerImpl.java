package com.dili.uap.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dili.uap.dao.DataAuthMapper;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.manager.DataAuthManager;
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
 *
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
	public void initUserDataAuthsInRedis(Long userId) {
		//查询数据权限，需要合并下面的部门数据权限列表
		List<DataAuth> dataAuths = this.dataAuthMapper.findByUserId(userId);
		//部门数据权限直接从用户部门关系表查询
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
		List<DataAuth> departmentsDataAuth = departmentMapper.findDataAuthes(param);
		//合并
		dataAuths.addAll(departmentsDataAuth);
		String key = SessionConstants.USER_DATA_AUTH_KEY + userId;
		this.redisUtil.remove(key);
		BoundSetOperations<String, Object> ops = this.redisUtil.getRedisTemplate().boundSetOps(key);
		for (DataAuth dataAuth : dataAuths) {
			ops.add(JSON.toJSONString(dataAuth));
		}
	}

	@Override
	public DataAuth getUserCurrentDataAuth(Long userId, String dataType) {
		DataAuth currentDataAuth = null;
		String key = SessionConstants.USER_DATA_AUTH_KEY + userId + ":" + dataType;
		String json = this.redisUtil.get(key, String.class);
		if (StringUtil.isEmpty(json)) {
			List<DataAuth> dataAuths = this.dataAuthMapper.findByUserId(userId);
			if (CollectionUtils.isEmpty(dataAuths)) {
				return null;
			}
			currentDataAuth = dataAuths.get(0);
			this.redisUtil.set(key, JSON.toJSONString(currentDataAuth));
		}
		currentDataAuth = JSON.parseObject(json, DataAuth.class);
		return currentDataAuth;
	}

	@Override
	public List<DataAuth> getUserDataAuth(Long userId) {
		return this.dataAuthMapper.findByUserId(userId);
	}

}