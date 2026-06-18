package com.example.check.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // 开启驼峰命名映射
            configuration.setMapUnderscoreToCamelCase(true);
            // 其他配置
            configuration.setCacheEnabled(true);
            configuration.setLazyLoadingEnabled(true);
        };
    }
}