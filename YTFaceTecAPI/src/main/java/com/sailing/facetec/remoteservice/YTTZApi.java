package com.sailing.facetec.remoteservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yunan on 2017/6/1.
 */
@FeignClient(
        name = "tz-api",
        url = "${ytface.tz-api-url}"
)
/**
 * 获取人脸特征码
 */
public interface YTTZApi {
    /**
     * 通过id获取特征码
     * @param id
     * @return
     */
    @RequestMapping("/storage/v1/data")
    byte[] getSpecByID(@RequestParam("id")String id);

    /**
     * 通过库id获取特征码
     * @param repo_id
     * @return
     */
    @RequestMapping("/storage/v1/data/all_repo")
    String getSpecByRepoID(@RequestParam("repo_id")String repo_id);
}
