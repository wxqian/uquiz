package com.leaf.uquiz.core.config;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/16
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    @Autowired
    private SystemConfig systemConfig;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/testUpload").setViewName("test_upload");
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (systemConfig.isDevMode()) {
            configuration.setAllowedOrigins(Lists.newArrayList("*"));
        } else {
            configuration.setAllowedOrigins(Lists.newArrayList(systemConfig.getFrontEndUrl(), systemConfig.getBaseUrl(), systemConfig.getMobileEndUrl()));
        }
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Lists.newArrayList("X-requested-with", "x-auth-token", "Content-Type"));
        configuration.setAllowedMethods(Lists.newArrayList("POST", "GET", "OPTIONS", "DELETE"));
        configuration.setExposedHeaders(Lists.newArrayList("x-auth-token"));
        configuration.setMaxAge(3600l);
        UrlBasedCorsConfigurationSource ccs = new UrlBasedCorsConfigurationSource();
        ccs.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(ccs);
    }
}
