package com.sailing.facetec;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * Created by yunan on 2017/4/26.
 */
@SpringCloudApplication
@EnableScheduling
@MapperScan("com.sailing.facetec.dao")
public class app {
public static void main(String... args){
    SpringApplication.run(app.class,args);
}
}
