package com.leaf.uquiz.core.config;

import com.leaf.uquiz.core.common.CookieHeaderHttpSessionStrategy;
import com.leaf.uquiz.core.redis.RedisConnectionFactoryBuilder;
import com.leaf.uquiz.core.redis.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
    @Autowired
    private RedisSettings redisSettings;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        return RedisConnectionFactoryBuilder.newBuilder().setProject("session").setRedisSettings(redisSettings).build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return redisTemplate;
    }

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new CookieHeaderHttpSessionStrategy();
    }
}
