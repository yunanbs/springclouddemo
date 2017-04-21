package com.sailing.demo.microservice.consumerapi.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yunan on 2017/4/21.
 */
@FeignClient("DEMO-API")
public interface DemoAPIProxy {
    @RequestMapping("/add")
    int add(@RequestBody String params);
}
