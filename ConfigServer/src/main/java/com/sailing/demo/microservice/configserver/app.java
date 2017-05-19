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
 */
@EnableConfigServer
@SpringCloudApplication
public class app
{
    public static void main (String...args){
        SpringApplication.run(app.class,args);
    }
}
