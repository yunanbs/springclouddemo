package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yunan on 2017/6/3.
 */
@FeignClient(
        name = "alert-api",
        url = "${alert-api}"
)
public interface AlertApi {
    @RequestMapping(value = "/accept/rlbk",consumes = "application/josn",method = RequestMethod.POST)
    String sendAlert(@RequestBody String params);
}
