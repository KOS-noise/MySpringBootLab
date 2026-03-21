package com.rookies5.myspringbootlab.runner;

import com.rookies5.myspringbootlab.config.MyEnvironment;
import com.rookies5.myspringbootlab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {
    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyEnvironment myEnvironment;
    @Autowired
    private MyPropProperties myProperties;

    // Logger 객체 생성
    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {


        System.out.println("Logger 구현체 클래스명= " + logger.getClass().getName());

        logger.info("username : " + username);
        logger.info("port : " + port);
        logger.debug("MyBootProperties getUsername() = " + myProperties.getUsername());
        logger.debug("MyBootProperties getPort() = " + myProperties.getPort());
        logger.info("Environment getMode :" + myEnvironment.getMode());
    }

}
