package com.sailing.baoshan;

import com.alibaba.fastjson.util.TypeUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by yunan on 2017/6/20.
 * 宝山分局统计用api接口
 */
@SpringBootApplication
@MapperScan( "com.sailing.baoshan.dao")
public class app {
    public static void main(String...args){
        SpringApplication.run(app.class,args);

        TypeUtils.compatibleWithJavaBean = true;
    }
}
