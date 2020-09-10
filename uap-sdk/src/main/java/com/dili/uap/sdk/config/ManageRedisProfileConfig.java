package com.dili.uap.sdk.config;

/**
 * Created by asiamastor on 2017/1/3.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 权限 redis 缓存配置;
 * @author asiamastor
 */

@Configuration
//@ConfigurationProperties(prefix = "manage.redis",locations = {"classpath:conf/manage-${spring.profiles.active}.properties"})
@ConfigurationProperties(prefix = "manage.redis")
@PropertySource({"classpath:conf/manage-${spring.profiles.active}.properties"})
public class ManageRedisProfileConfig {

//    @Value("${manage.redis.host}")
    private String host;
//    @Value("${manage.redis.port}")
    private Integer port = 6379;
    private String password;
    private Integer database = 0;
    //连接超时时间（毫秒）
    private Long timeout = 1000L;
    //关闭时的超时时间
    private Long shutdownTimeout = 0L;
    //连接池最大连接数（使用负值表示没有限制） 默认 8
    private Integer maxActive= 8;
    //连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
    private Integer maxWait = -1;
    //连接池中的最大空闲连接 默认 8
    private Integer maxIdle = 8;
    //连接池中的最小空闲连接 默认 0
    private Integer minIdle = 0;

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

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(Long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }
}

