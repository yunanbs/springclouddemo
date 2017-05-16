package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yunan on 2017/5/16.
 */
@FeignClient(
        name = "yt-api",
        url = "${ytface.api-url}"
)
public interface YTService
{
    @RequestMapping(value = "/",consumes = "application/json",method = {RequestMethod.POST})
    String addCamera(@RequestBody String params);

    @RequestMapping(value = "/",consumes = "application/json",method = {RequestMethod.POST})
    String enableCamera(@RequestBody String params);
}
