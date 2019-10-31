package com.dili.uap.sdk.config;

/**
 * Created by asiamastor on 2017/1/3.
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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

@Configuration("manageRedisConfig")
//@ConfigurationProperties(prefix = "manage.redis",locations = {"classpath:conf/manage-${spring.profiles.active}.properties"})
@ConfigurationProperties(prefix = "manage.redis")
@PropertySource({"classpath:conf/manage-${spring.profiles.active}.properties"})
public class ManageRedisConfig {

//    @Value("${manage.redis.host}")
    private String host;
//    @Value("${manage.redis.port}")
    private Integer port = 6379;
    private String password;
    private Integer database = 0;
    //连接超时时间（毫秒）
    private long timeout = 1000;
    //关闭时的超时时间
    private long shutdownTimeout = 0;
    //连接池最大连接数（使用负值表示没有限制） 默认 8
    private int maxActive= 8;
    //连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
    private int maxWait = -1;
    //连接池中的最大空闲连接 默认 8
    private int maxIdle = 8;
    //连接池中的最小空闲连接 默认 0
    private int minIdle = 0;

//    @Bean("manageRedisConnectionFactory")
    private LettuceConnectionFactory getLettuceConnectionFactory() {
        /* ========= 基本配置 ========= */
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(database);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }

        /* ========= 连接池通用配置 ========= */
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);

        /* ========= lettuce pool ========= */
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder lettucePoolingClientConfigurationBuilder = LettucePoolingClientConfiguration.builder();
        lettucePoolingClientConfigurationBuilder.poolConfig(genericObjectPoolConfig);
        lettucePoolingClientConfigurationBuilder.commandTimeout(Duration.ofMillis(timeout));
        lettucePoolingClientConfigurationBuilder.shutdownTimeout(Duration.ofMillis(shutdownTimeout));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration, lettucePoolingClientConfigurationBuilder.build());
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    /**
     * redis模板操作类,类似于jdbcTemplate的一个类;
     * <p>
     * 虽然CacheManager也能获取到Cache对象，但是操作起来没有那么灵活；
     * <p>
     * 这里在扩展下：RedisTemplate这个类不见得很好操作，我们可以在进行扩展一个我们
     * <p>
     * 自己的缓存类，比如：RedisStorage类;
     *
     * @param redisConnectionFactory : 通过Spring进行注入，参数在application.properties进行配置；
     * @return
     */
//    @Bean("manageRedisTemplate")
//    public RedisTemplate<String, String> redisTemplate(@Qualifier("manageRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
//        //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
//        //或者JdkSerializationRedisSerializer序列化方式;
////           RedisSerializer<String>redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
////           redisTemplate.setKeySerializer(redisSerializer);
////           redisTemplate.setHashKeySerializer(redisSerializer);
//
//        StringRedisSerializer stringRedisSerializer= new StringRedisSerializer();
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }
}

