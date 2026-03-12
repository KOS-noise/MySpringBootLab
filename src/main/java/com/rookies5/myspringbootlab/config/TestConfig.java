package com.rookies5.myspringbootlab.config;

import com.rookies5.myspringbootlab.property.MyPropProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfig {
    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder()
                .mode("개발 환경")
                .build();
    }
}
