package com.sailing.demo.microservice.demorestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Created by yunan on 2017/4/21.
 * 测试服务
 */
@SpringBootApplication
public class app {
    public static void main(String...args){
        SpringApplication.run(app.class,args);
    }
}
