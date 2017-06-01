package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yunan on 2017/5/31.
 */
@FeignClient(name="test-api",url = "http://172.20.25.226:7120")
public interface TestApi {
    @RequestMapping("/storage/v1/data?id=562949953421403")
    byte[] testtzm(@RequestParam("id")String id);
}
