package com.sailing.facetec.service;

import com.sailing.facetec.entity.BkrwEntity;

import java.text.ParseException;

/**
 * Created by GuoWang on 2017/5/29.
 */
public interface RlbkrwService {
    /**
     * 摄像头布控人脸库
     * @param bkrwEntity
     * @return
     * @throws ParseException
     */
    int addMonitorReposity(BkrwEntity bkrwEntity) throws ParseException;

    /**
     * 删除布控任务
     * @param bkid
     * @return
     */
    int delMonitorReposity(String bkid);

}
