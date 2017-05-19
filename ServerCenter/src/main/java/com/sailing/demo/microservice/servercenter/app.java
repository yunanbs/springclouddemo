package com.sailing.demo.microservice.servercenter;

import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by yunan on 2017/4/25.
 * Eureka 服务中心
 */
@EnableEurekaServer
@SpringBootApplication
public class app {
    public  static void main(String... args){
        SpringApplication.run(app.class,args);
    }
}
