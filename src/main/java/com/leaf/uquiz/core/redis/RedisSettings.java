package com.leaf.uquiz.core.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * redis 配置
 *
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
@ConfigurationProperties(prefix = "leaf.redis")
public class RedisSettings {
    private Pool pool;

    private Map<String,ProjectSetting> project;

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Map<String, ProjectSetting> getProject() {
        return project;
    }

    public void setProject(Map<String, ProjectSetting> project) {
        this.project = project;
    }

    public static class ProjectSetting{
        /**
         * ex:redis://host:port:password/dbIndex
         */
        private String url="redis://10.1.245.167:6379:/0";

        private int timeOut=100000;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }
    }

    public static class Pool{
        private int maxIdle = 200;
        private int minIdle = 10;
        private int maxWait = 60000;
        private int maxTotal = 1024;

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

        public int getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }
    }
}
