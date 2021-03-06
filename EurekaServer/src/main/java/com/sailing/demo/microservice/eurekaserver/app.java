package com.sailing.demo.microservice.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by yunan on 2017/4/21.
 * 测试服务
 */
@EnableEurekaServer
@SpringBootApplication
public class app {
    public  static void main(String... args){
        SpringApplication.run(app.class,args);
    }
}
