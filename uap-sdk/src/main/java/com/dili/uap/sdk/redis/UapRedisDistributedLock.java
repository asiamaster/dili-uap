package com.dili.uap.sdk.redis;

import com.dili.ss.redis.service.RedisDistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * UAP redis分布式锁
 * @author: WM
 * @time: 2021/1/6 14:24
 */
@Component
public class UapRedisDistributedLock extends RedisDistributedLock {
    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Resource(name = "manageRedisTemplate")
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
