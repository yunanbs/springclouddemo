package com.sailing.baoshan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yunan on 2017/4/27.
 * 操作编码配置项
 */
// 配置注解
@Configuration
// 读取特定的配置节
@ConfigurationProperties(prefix = "actioncodes")
@Data
public class ActionCodeConfig {

    private String SUCCEED_CODE;
    private String SUCCEED_MSG;

    private String SERVER_ERROR_CODE;
    private String SERVER_ERROR_MSG;

    private String PARAMS_ERROR_CODE;
    private String PARAMS_ERROR_MSG;


}
