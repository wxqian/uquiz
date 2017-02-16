package com.leaf.uquiz.core.messages;

import com.leaf.uquiz.core.exception.MyException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * i18n 的配置
 *
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/16
 */
@Configuration
public class MessageConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(getBaseNames());
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    private String[] getBaseNames() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resolver.getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "/messages/*.properties");
        } catch (IOException e) {
            throw new MyException("获取properties文件失败");
        }
        List<String> filePathList = new ArrayList<>(resources.length);
        for (Resource resource : resources) {
            String uri;
            try {
                uri = resource.getURI().toString();
            } catch (IOException e) {
                throw new MyException("获取文件uri失败");
            }
            filePathList.add(uri.substring(0, uri.lastIndexOf(".")));
        }
        return filePathList.toArray(new String[filePathList.size()]);
    }
}
