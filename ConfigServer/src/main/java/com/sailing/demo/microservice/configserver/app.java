package com.sailing.demo.microservice.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by yunan on 2017/4/22.
 * ConfigServer
 * 使用注解 @EnableConfigServer 开启ConfigServer
 *
 * 注解 @SpringCloudApplication 包含以下3个注解
 * 注解 @SpringBootApplication springboot应用
 * 注解 @EnableDiscoveryClient 启用服务发现与服务注册
 * 注解 @EnableCircuitBreaker 启用熔断
 */
@EnableConfigServer
@SpringCloudApplication
public class app
{
    public static void main (String...args){
        SpringApplication.run(app.class,args);
    }
}
