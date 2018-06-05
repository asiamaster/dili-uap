package com.dili.uap.sdk.redis;

import com.dili.uap.sdk.domain.System;
import com.dili.uap.sdk.exception.ParameterException;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 用户和系统 redis操作
 * Created by asiamastor on 2018/6/5.
 */
@Service
public class UserSystemRedis {
    private static final Logger log = LoggerFactory.getLogger(UserSystemRedis.class);

    @Autowired
    private ManageRedisUtil redisUtil;

    /**
     * 根据用户ID判断访问系统的权限
     * @param userId
     * @param systemCode    系统编码
     * @return
     */
    public boolean checkUserSystemRight(Long userId, String systemCode) {
        if (userId == null || StringUtils.isBlank(systemCode)) {
            throw new ParameterException("用户id或系统编码不能为空");
        }
        // 去掉http和https前缀, 判断用户权限
        return checkRedisUserSystemByCode(userId, systemCode);
    }

    /**
     * 获取用户有权限的系统列表
     * @param userId
     * @return
     */
    public Set<System> getRedisUserSystems(Long userId){
        return redisUtil.getRedisTemplate().boundSetOps(SessionConstants.USER_SYSTEM_KEY + userId.toString()).members();
    }

    /**
     * 从redis根据用户id判断是否有菜单URL的访问权限
     *
     * @param userId
     * @param systemCode
     * @return
     */
    private boolean checkRedisUserSystemByCode(Long userId, String systemCode) {
        Set<System> systems = getRedisUserSystems(userId);
        for(System system : systems){
            if(system.getCode().equals(systemCode)){
                return true;
            }
        }
        return false;
    }

}