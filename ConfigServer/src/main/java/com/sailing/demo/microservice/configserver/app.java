package com.sailing.demo.microservice.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by yunan on 2017/6/2.
 */
@EnableConfigServer
@SpringCloudApplication
public class app {
    public static void main(String... args){
        SpringApplication.run(app.class,args);
    }
}
