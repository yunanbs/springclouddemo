package com.sailing.facetec.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/7.
 * 抽取实时数据任务
 */
@Component(value = "com.sailing.facetec.task.TaskScheduler")
public class TaskScheduler {

    @Autowired
    private RllrService rllrService;

    @Autowired
    private RlgjService rlgjService;

    @Autowired
    private RedisService redisService;

    @Value("${redis-keys.capture-lock}")
    private String captureLock;
    @Value("${redis-keys.alert-lock}")
    private String alertLock;
    @Value("${redis-keys.lock-val}")
    private String lockVal;
    @Value("${redis-keys.capture-data}")
    private String captureData;
    @Value("${redis-keys.alert-data}")
    private String alertData;
    @Value("${redis-keys.capture-cache}")
    private int captureCache;
    @Value("${redis-keys.alert-cache}")
    private int alertCache;
    @Value("${redis-keys.alert-limit}")
    private double alertLimit;

    /**
     * 抽取抓拍记录
     */
    @Scheduled(cron = "${tasks.capture}")
    public void captureScheduler() {
        // 获取锁
        if(redisService.setValNX(captureLock, lockVal,1, TimeUnit.SECONDS)){
            // 按配置抽取历史数据
            DataEntity result = rllrService.listRllrDetail("", "", "", 1, captureCache, "", "", "", "", "", "");
            // 更新实时数据
            redisService.setVal(captureData, JSON.toJSONString(result, FastJsonUtils.nameFilter, SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero),1,TimeUnit.DAYS);
            // 释放锁
            redisService.delKey(captureLock);
        }
    }

    /**
     * 抽取告警记录
     */
    @Scheduled(cron = "${tasks.alert}")
    public void alterScheduler() {
        // 获取锁
        if(redisService.setValNX(alertLock, lockVal,1, TimeUnit.SECONDS)){
            // 按配置抽取历史数据
            DataEntity result = rlgjService.listRlgjDetail("", "", "", 1, alertCache,alertLimit, "", "", "", "", "","","","");
            // 更新实时数据
            redisService.setVal(alertData, JSON.toJSONString(result,FastJsonUtils.nameFilter,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero),1,TimeUnit.DAYS);
            // 释放锁
            redisService.delKey(alertLock);
        }
    }

}
