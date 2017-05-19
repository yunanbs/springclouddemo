package com.sailing.demo.springcloud.apigateway;

import com.sailing.demo.springcloud.apigateway.filter.JWTFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * Created by yunan on 2017/4/22.
 * ApiGateway
 */
@SpringCloudApplication
@EnableZuulProxy
public class app {
    public static void main(String... args){
        SpringApplication.run(app.class,args);
    }

    /**
     * 创建jwt过滤器
     * @return
     */
    @Bean
    public JWTFilter jwtFilter(){
        return new JWTFilter();
    }
}
