package com.sailing.facetec.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunan on 2017/6/4.
 */
@Configuration
@ConfigurationProperties(prefix = "plant")
@Data
public class PlantConfig {
    private String remotePlantRoot;
    private Map<String,String> plantMap = new HashMap<>();
}
