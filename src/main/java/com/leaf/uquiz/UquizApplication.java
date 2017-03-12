package com.leaf.uquiz;

import com.leaf.uquiz.core.config.KaptchaConfig;
import com.leaf.uquiz.core.config.SystemConfig;
import com.leaf.uquiz.core.config.WeixinConfig;
import com.leaf.uquiz.core.redis.RedisSettings;
import com.leaf.uquiz.file.config.FileSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/14
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class})
@EnableConfigurationProperties({RedisSettings.class, FileSettings.class, WeixinConfig.class, SystemConfig.class, KaptchaConfig.class})
public class UquizApplication {

    public static void main(String[] args) {
        SpringApplication.run(UquizApplication.class, args);
    }
}
