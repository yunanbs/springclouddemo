package com.sailing.facetec.task;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/7.
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

    @Scheduled(cron = "${tasks.capture}")
    public void captureScheduler() {
        if(redisService.setValNX(captureLock, lockVal,1, TimeUnit.SECONDS)){
            DataEntity result = rllrService.listRllrDetail("2017-01-01 00:00:00", "", "", 1, captureCache, "", "", "", "", "", "");
            redisService.setVal(captureData, JSONObject.fromObject(result).toString(),1,TimeUnit.DAYS);
            redisService.delKey(captureLock);
        }
    }

    @Scheduled(cron = "${tasks.alert}")
    public void alterScheduler() {
        if(redisService.setValNX(alertLock, lockVal,1, TimeUnit.SECONDS)){
            DataEntity result = rlgjService.listRlgjDetail("2017-01-01 00:00:00", "", "", 1, alertCache,alertLimit, "", "", "", "", "","","","");
            redisService.setVal(alertData, JSONObject.fromObject(result).toString(),1,TimeUnit.DAYS);
            redisService.delKey(alertLock);
        }
    }

}
