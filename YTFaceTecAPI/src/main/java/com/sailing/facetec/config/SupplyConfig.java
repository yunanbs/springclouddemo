package com.sailing.facetec.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunan on 2017/5/4.
 */
@Configuration
@ConfigurationProperties(prefix = "supplys")
@Data
public class SupplyConfig {
    private Map<String,String> supplyMap =new HashMap<>();
}
