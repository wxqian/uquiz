package com.leaf.uquiz.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/3
 */
@Component
public class HashCache {

    @Autowired
    @Qualifier("hashRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Object> opsForHash;

    private static final ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void init() {
        opsForHash = redisTemplate.opsForHash();
    }

    public void set(String catalog, String key, Object object) {
        Assert.hasLength(catalog, "catalog 不能为空");
        Assert.hasLength(key, "key 不能为空");
        Assert.notNull(object, "object 不能为空");
        opsForHash.put(catalog, key, object);
    }

    public Object get(String catalog, String key) {
        Assert.hasLength(catalog, "catalog 不能为空");
        Assert.hasLength(key, "key 不能为空");
        return opsForHash.get(catalog, key);
    }

    public void cSet(String catalog, String key, Object object) {
        Assert.hasLength(catalog, "catalog 不能为空");
        Assert.hasLength(key, "key 不能为空");
        Assert.notNull(object, "object 不能为空");
        lock.lock();
        try {
            opsForHash.put(catalog, key, object);
        } finally {
            lock.unlock();
        }
    }

    public Object cGet(String catalog, String key) {
        Assert.hasLength(catalog, "catalog 不能为空");
        Assert.hasLength(key, "key 不能为空");
        lock.lock();
        try {
            return opsForHash.get(catalog, key);
        } finally {
            lock.unlock();
        }
    }

    public Map<String, Object> getMap(String catalog) {
        Assert.hasLength(catalog, "catalog 不能为空");
        return opsForHash.entries(catalog);
    }

    public void remove(String catalog, String key) {
        Assert.hasLength(catalog, "catalog 不能为空");
        Assert.hasLength(key, "key 不能为空");
        opsForHash.delete(catalog, key);
    }

    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    public void flush(String catalog) {
        Assert.hasText(catalog, "目录不能为空!");

        Set<String> keys = opsForHash.keys(catalog);
        opsForHash.delete(catalog, keys.toArray());
    }
}
