package com.rookies5.myspringbootlab.config;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyEnvironment {
    String mode;
}
