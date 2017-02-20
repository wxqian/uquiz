package com.leaf.uquiz.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/2
 */
@Component
public class StringCache {

    @Autowired
    @Qualifier("cacheRedisTemplate")
    public RedisTemplate redisTemplate;

    private ValueOperations<String, String> ops;

    @PostConstruct
    public void init() {
        ops = redisTemplate.opsForValue();
    }

    private static final ReentrantLock lock = new ReentrantLock();

    public <T> T get(String key) {
        return (T) ops.get(key);
    }

    public void set(String key, String value, long time) {
        ops.set(key, value, time, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        ops.set(key, value);
    }

    public <T> T cGet(String key) {
        lock.lock();
        try {
            return (T) ops.get(key);
        } finally {
            lock.unlock();
        }

    }

    public void cSet(String key, String value, long time) {
        lock.lock();
        try {
            ops.set(key, value, time, TimeUnit.SECONDS);
        } finally {
            lock.unlock();
        }

    }

    public void cSet(String key, String value) {
        lock.lock();
        try {
            ops.set(key, value);
        } finally {
            lock.unlock();
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
