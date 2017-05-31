package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
     * 删除摄像头
     * @param cookie
     * @param id
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_video/camera",method = {RequestMethod.DELETE})
    String delCamera(@RequestHeader("Cookie") String cookie,@RequestParam("id")String id);



    /**
     * 布控人像库
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_video/surveillance", consumes = "application/json", method = {RequestMethod.POST})
    String setMonitorRepository(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    /**
     * 删除布控任务
     * @param cookie
     * @param bkid
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_video/surveillance", method = {RequestMethod.DELETE})
    String delMonitorRepository(@RequestHeader("Cookie") String cookie, @RequestParam("id") Long bkid);

    /**
     * 新增人脸库
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_image/repository", consumes = "application/json", method = {RequestMethod.POST})
    String addFaceLib(@RequestHeader("Cookie") String cookie, @RequestBody String params);


    /**
     * 删除人脸库
     * @param cookie
     * @param id
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_image/repository",method = {RequestMethod.DELETE})
    String delFaceLib(@RequestHeader("Cookie") String cookie,@RequestParam("id")String id);

    /**
     * 布控路人库
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/v2/surveillance", consumes = "application/json", method = {RequestMethod.POST})
    String setMonitorByCamera(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    /**
     * 修改人员信息
     * @param cookie
     * @param params 需要传入的json对象：rlid,xm,qybh,csnf,xb,mz,sfzh
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face/update",method = {RequestMethod.POST})
    String altPersonalInfo(@RequestHeader("Cookie") String cookie, @RequestBody String params);


    /**
     * 删除人员
     * @param cookie
     * @param
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face/delete",method = {RequestMethod.POST})
    String delPersonal(@RequestHeader("Cookie") String cookie, @RequestBody String params);



    /**
     * 人脸库上传人脸
     * @param cookie
     * @param params
     * @return
     */
    @RequestMapping(value = "/face/v1/framework/face_image/repository/picture/synchronized",method = RequestMethod.POST)
    String uploadPic(@RequestHeader("Cookie") String cookie, @RequestBody String params);

    /**
     * 图片下载
     * @param uri_base64
     * @return
     */
    @RequestMapping(value = "/storage/v1/image",method = {RequestMethod.GET})
    String downLoadPic(@RequestParam("uri_base64") String uri_base64,@RequestParam("sid")String sid);


}