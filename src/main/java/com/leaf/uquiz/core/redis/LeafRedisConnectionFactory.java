package com.leaf.uquiz.core.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 重写redis 数据源工厂连接类,实现自定义连接方法
 *
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
public class LeafRedisConnectionFactory extends JedisConnectionFactory {
    private static final String URL_PREFIX = "redis://";

    private RedisSettings redisSettings;

    public LeafRedisConnectionFactory(String dataSourceName, RedisSettings redisSettings) {
        this.redisSettings = redisSettings;
        RedisSettings.ProjectSetting projectSetting = redisSettings.getProject().get(dataSourceName);

        String url = projectSetting.getUrl();
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url must be not null");
        }
        if (!StringUtils.startsWith(url, URL_PREFIX)) {
            throw new IllegalArgumentException("url needs starts with " + URL_PREFIX);
        }
        url = url.substring(URL_PREFIX.length());
        String[] urlArr = StringUtils.split(url, "/");
        url = urlArr[0];
        String dbIndex = urlArr[1];
        String[] hostArr = StringUtils.split(url, ":");
        setHostName(hostArr[0]);
        setPort(Integer.valueOf(hostArr[1]));
        if (hostArr.length > 2 && StringUtils.isNotBlank(hostArr[2])) {
            setPassword(hostArr[2]);
        }
        setDatabase(Integer.valueOf(dbIndex));
        setPoolConfig(getJedisConfig());
        setUsePool(Boolean.TRUE);
    }

    private JedisPoolConfig getJedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }
}
