package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yunan on 2017/5/16.
 * 依图算法api代理
 */
@FeignClient(
        name = "yt-api",
        url = "${ytface.api-url}"
)
public interface YTApi {

    /**
     * 用户登录
     * @param params
     * @return
     */
    @RequestMapping(value = "/resource_manager/user/login", consumes = "application/json", method = {RequestMethod.POST})
    String login(@RequestBody String params);

    /**
     * 添加摄像头
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_video/camera", consumes = "application/json", method = {RequestMethod.POST})
    String addCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    /**
     * 更新摄像头信息
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_video/camera", consumes = "application/json", method = {RequestMethod.PUT})
    String updateCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    /**
     * 布控人像库
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_video/surveillance", consumes = "application/json", method = {RequestMethod.POST})
    String setMonitorRepository(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    /**
     * 布控路人库
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/v2/surveillance", consumes = "application/json", method = {RequestMethod.POST})
    String setMonitorByCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);
}