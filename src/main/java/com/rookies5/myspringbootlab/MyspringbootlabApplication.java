package com.rookies5.myspringbootlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// Security 기능을 자동 설정에서 제외시킵니다.
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
})
public class MyspringbootlabApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyspringbootlabApplication.class, args);
    }
}
