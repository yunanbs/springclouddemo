package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yunan on 2017/5/16.
 */
@FeignClient(
        name = "yt-api",
        url = "${ytface.api-url}"
)
public interface YTApi {
    @RequestMapping(value = "/resource_manager/user/login", consumes = "application/json", method = {RequestMethod.POST})
    String login(@RequestBody String params);

    @RequestMapping(value = "/face/v1/framework/face_video/camera", consumes = "application/json", method = {RequestMethod.POST})
    String addCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    @RequestMapping(value = "/face/v1/framework/face_video/camera", consumes = "application/json", method = {RequestMethod.PUT})
    String updateCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    @RequestMapping(value = "/face/v1/framework/face_video/surveillance", consumes = "application/json", method = {RequestMethod.POST})
    String setMonitorRepository(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    @RequestMapping(value = "/v2/surveillance", consumes = "application/json", method = {RequestMethod.POST})
    String setMonitorByCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);
}