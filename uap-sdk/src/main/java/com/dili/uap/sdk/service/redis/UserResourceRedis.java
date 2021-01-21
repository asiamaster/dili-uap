package com.dili.uap.sdk.service.redis;

import com.dili.uap.sdk.exception.ParameterException;
import com.dili.uap.sdk.util.KeyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户资源redis操作
 * Created by asiamastor on 2017/7/10.
 */
@Service
public class UserResourceRedis {
    private static final Logger log = LoggerFactory.getLogger(UserResourceRedis.class);

    @Resource(name="manageRedisUtil")
    private ManageRedisUtil redisUtil;

    /**
     * 从redis根据用户id判断是否有resourceCode的访问权限
     * @param userId
     * @param resourceCode
     * @return
     */
    public boolean checkUserResourceRight(Long userId, Integer systemType, String resourceCode) {
        if (userId == null) {
            log.debug("用户id或资源编码不能为空!");
            throw new ParameterException();
        }
        return isMemberKey(KeyBuilder.buildUserResourceCodeKey(userId.toString(), systemType), resourceCode);

    }

    /**
     * 判断redis的map中的key value匹配
     * @param key
     * @param value
     * @return
     */
    private Boolean isMemberKey(String key, Object value){
        return redisUtil.getRedisTemplate().boundSetOps(key).isMember(value);
    }
}