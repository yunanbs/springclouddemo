package com.sailing.facetec.task;

import com.alibaba.fastjson.JSONObject;
import com.netflix.ribbon.Ribbon;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/7.
 */
@Component
public class TaskScheduler {

    @Autowired
    private RllrService rllrService;

    @Autowired
    private RlgjService rlgjService;

    @Autowired
    private RedisService redisService;

    private final String CAPTURE_LOCK_KEY = "capture-lock";
    private final String ALERT_LOCK_KEY = "alert-lock";
    private final String LOCK_VAL = "ok";
    private final String CAPTURE_DATA_KEY = "capture-data";
    private final String ALERT_DATA_KEY = "alert-data";

    @Scheduled(cron = "${tasks.capture}")
    public void captureScheduler() {
        if(redisService.setValNX(CAPTURE_LOCK_KEY,LOCK_VAL,1, TimeUnit.SECONDS)){
            DataEntity result = rllrService.listRllrDetail("", "", "", 1, 10, "", "", "", "", "", "");
            redisService.setVal(CAPTURE_DATA_KEY, JSONObject.toJSONString(result),1,TimeUnit.DAYS);
            redisService.delKey(CAPTURE_LOCK_KEY);
        }
    }

    @Scheduled(cron = "${tasks.alert}")
    public void alterScheduler() {
        if(redisService.setValNX(ALERT_LOCK_KEY,LOCK_VAL,1, TimeUnit.SECONDS)){
            DataEntity result = rlgjService.listRlgjDetail("", "", "", 1, 10,0d, "", "", "", "", "","","","");
            redisService.setVal(ALERT_DATA_KEY, JSONObject.toJSONString(result),1,TimeUnit.DAYS);
            redisService.delKey(ALERT_LOCK_KEY);
        }
    }

}
