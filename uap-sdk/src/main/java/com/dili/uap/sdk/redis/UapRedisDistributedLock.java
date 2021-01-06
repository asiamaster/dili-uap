package com.dili.uap.sdk.redis;

import com.dili.ss.redis.service.RedisDistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * UAP redis分布式锁
 * @author: WM
 * @time: 2021/1/6 14:24
 */
@Component
public class UapRedisDistributedLock {
    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

    @Resource(name = "manageRedisTemplate")
    private RedisTemplate redisTemplate;

    public static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取锁，key作为获取锁的条件，key和value将作为释放锁的条件
     * @param key
     * @param value
     * @param expire 单位秒
     * @return  获取失败返回false
     */
    public boolean tryGetLock(String key, String value,  long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.set(key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8")), Expiration.seconds(expire), RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return (Boolean)redisTemplate.execute(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lettuce方式释放锁
     * 根据lockKey从redis获取到的值和lockValue对比，如果相同则根据key删除缓存并返回true，不同则返回false
     * @param lockKey
     * @param lockValue
     * @return
     */
    public boolean releaseLock(String lockKey, String lockValue) {
        RedisCallback<Boolean> callback = (connection) -> {
            return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN ,1, lockKey.getBytes(Charset.forName("UTF-8")), lockValue.getBytes(Charset.forName("UTF-8")));
        };
        return (Boolean)redisTemplate.execute(callback);
    }
}
