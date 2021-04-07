package com.dili.uap.sdk.config;

/**
 * Created by asiamastor on 2017/1/3.
 */
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.ObjectUtils;

import java.time.Duration;

/**
 * 权限 redis 缓存配置;
 * @author asiamastor
 */

@Configuration
public class ManageRedisInitializer {
    @Autowired
    private ManageRedisConfig manageRedisConfig;

//    @Bean("manageRedisConnectionFactory")
    private LettuceConnectionFactory getLettuceConnectionFactory() {

        /* ========= 基本配置 ========= */
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        System.out.println("--redis host:"+manageRedisConfig.getHost()+",port:"+manageRedisConfig.getPort()+",db:"+manageRedisConfig.getDatabase());
        configuration.setHostName(manageRedisConfig.getHost());
        configuration.setPort(manageRedisConfig.getPort());
        configuration.setDatabase(manageRedisConfig.getDatabase());
        if (!ObjectUtils.isEmpty(manageRedisConfig.getPassword())) {
            RedisPassword redisPassword = RedisPassword.of(manageRedisConfig.getPassword());
            configuration.setPassword(redisPassword);
        }

        /* ========= 连接池通用配置 ========= */
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(manageRedisConfig.getMaxActive());
        genericObjectPoolConfig.setMinIdle(manageRedisConfig.getMinIdle());
        genericObjectPoolConfig.setMaxIdle(manageRedisConfig.getMaxIdle());
        genericObjectPoolConfig.setMaxWaitMillis(manageRedisConfig.getMaxWait());

        /* ========= lettuce pool ========= */
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder lettucePoolingClientConfigurationBuilder = LettucePoolingClientConfiguration.builder();
        lettucePoolingClientConfigurationBuilder.poolConfig(genericObjectPoolConfig);
        lettucePoolingClientConfigurationBuilder.commandTimeout(Duration.ofMillis(manageRedisConfig.getTimeout()));
        lettucePoolingClientConfigurationBuilder.shutdownTimeout(Duration.ofMillis(manageRedisConfig.getShutdownTimeout()));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration, lettucePoolingClientConfigurationBuilder.build());
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    /**
     * 获取缓存操作助手对象
     *
     * @return
     */
    @Bean("manageRedisTemplate")
//    public RedisTemplate<String, String> redisTemplate(@Qualifier("manageRedisConnectionFactory") LettuceConnectionFactory factory) {
    public RedisTemplate<String, String> redisTemplate() {
        LettuceConnectionFactory factory = getLettuceConnectionFactory();
        //创建Redis缓存操作助手RedisTemplate对象
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        //以下代码为将RedisTemplate的Value序列化方式由JdkSerializationRedisSerializer更换为Jackson2JsonRedisSerializer
        //此种序列化方式结果清晰、容易阅读、存储字节少、速度快，所以推荐更换
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;//StringRedisTemplate是RedisTempLate<String, String>的子类
    }

}

