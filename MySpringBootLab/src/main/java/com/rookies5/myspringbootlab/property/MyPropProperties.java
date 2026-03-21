package com.rookies5.myspringbootlab.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("myprop")
@Getter
@Setter
public class MyPropProperties {
    private String username;
    private int port;
}