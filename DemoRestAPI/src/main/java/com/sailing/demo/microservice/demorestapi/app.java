package com.sailing.demo.microservice.demorestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by yunan on 2017/4/21.
 */
@SpringCloudApplication
public class app {
    public static void main(String...args){
        SpringApplication.run(app.class,args);
    }
}
