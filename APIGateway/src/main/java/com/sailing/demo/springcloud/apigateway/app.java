package com.sailing.demo.springcloud.apigateway;

import com.sailing.demo.springcloud.apigateway.filter.JWTFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * Created by yunan on 2017/4/22.
 */
@SpringCloudApplication
@EnableZuulProxy
public class app {
    public static void main(String... args){
        SpringApplication.run(app.class,args);
    }

    @Bean
    public JWTFilter jwtFilter(){
        return new JWTFilter();
    }
}
