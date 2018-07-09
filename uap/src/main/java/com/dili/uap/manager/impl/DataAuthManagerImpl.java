package com.dili.uap.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.UserDataAuthMapper;
import com.dili.uap.domain.UserDataAuth;
import com.dili.uap.manager.DataAuthManager;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 数据权限redis管理器
 * Created by asiam
 */
@Component
public class DataAuthManagerImpl implements DataAuthManager {
	private final static Logger LOG = LoggerFactory.getLogger(DataAuthManagerImpl.class);

	@Autowired
	private UserDataAuthMapper userDataAuthMapper;

	@Autowired
	private ManageRedisUtil redisUtil;

	@Override
	public void initUserDataAuthesInRedis(Long userId) {
		UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
		userDataAuth.setUserId(userId);
		//查询数据权限，需要合并下面的部门数据权限列表
		List<UserDataAuth> userDataAuths = this.userDataAuthMapper.select(userDataAuth);
		String key = SessionConstants.USER_DATA_AUTH_KEY + userId;
		this.redisUtil.remove(key);
		if(CollectionUtils.isEmpty(userDataAuths)){
			return;
		}
		BoundSetOperations<String, Object> ops = this.redisUtil.getRedisTemplate().boundSetOps(key);
		for (UserDataAuth dataAuth : userDataAuths) {
			ops.add(JSON.toJSONString(dataAuth));
		}
	}

	@Override
	public List<UserDataAuth> listUserDataAuthesByRefCode(Long userId, String refCode) {
		UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
		userDataAuth.setUserId(userId);
		userDataAuth.setRefCode(refCode);
		return this.userDataAuthMapper.select(userDataAuth);
	}

	@Override
	public List<UserDataAuth> listUserDataAuthes(Long userId) {
		UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
		userDataAuth.setUserId(userId);
		return this.userDataAuthMapper.select(userDataAuth);
	}

}