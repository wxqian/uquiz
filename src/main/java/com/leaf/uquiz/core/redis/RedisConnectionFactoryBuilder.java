package com.leaf.uquiz.core.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
public class RedisConnectionFactoryBuilder {

    private String project;

    private RedisSettings redisSettings;

    private RedisConnectionFactoryBuilder() {

    }

    public static RedisConnectionFactoryBuilder newBuilder() {
        return new RedisConnectionFactoryBuilder();
    }

    public RedisConnectionFactoryBuilder setProject(String project) {
        this.project = project;
        return this;
    }

    public RedisConnectionFactoryBuilder setRedisSettings(RedisSettings redisSettings) {
        this.redisSettings = redisSettings;
        return this;
    }

    public RedisConnectionFactory build() {
        RedisConnectionFactory factory = new LeafRedisConnectionFactory(project, redisSettings);
        try {
            ((InitializingBean) factory).afterPropertiesSet();
        } catch (Exception e) {
            throw new IllegalArgumentException("this RedisConnectionFactory is not spring's InitializingBean!");
        }
        return factory;
    }
}
