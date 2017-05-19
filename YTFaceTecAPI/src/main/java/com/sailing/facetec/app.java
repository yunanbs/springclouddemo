package com.sailing.facetec;

import com.alibaba.fastjson.util.TypeUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by yunan on 2017/4/26.
 * 人像识别服务
 */
@SpringCloudApplication
// 设置mybais Mapper包名
@MapperScan("com.sailing.facetec.dao")
// 启用事物
@EnableTransactionManagement
// 启用FeignClient
@EnableFeignClients
public class app {
    public static void main(String... args) {
        SpringApplication.run(app.class, args);

        // 解决fastjson首字母大小写问题
        // 默认情况下 如果属性首字母为大写，fastjson处理后的jsonobject的属性首字母会自动被转为小写
        // 设置后 fastjson不会再对首字母进行处理
        TypeUtils.compatibleWithJavaBean = true;
    }
}
