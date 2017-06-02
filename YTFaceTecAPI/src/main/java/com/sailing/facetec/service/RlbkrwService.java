package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.BkrwEntity;

import java.text.ParseException;

/**
 * Created by GuoWang on 2017/5/29.
 */
public interface RlbkrwService {
    /**
     * 摄像头布控人脸库
     * @param bkrwEntities
     * @return
     * @throws ParseException
     */
    int addMonitorReposity(BkrwEntity[] bkrwEntities) throws ParseException;

    /**
     * 删除布控任务
     * @param bkids
     * @return
     */
    int delMonitorReposity(String bkids);

    /**
     * 查询布控任务
     * @return
     */
    DataEntity<BkrwEntity> queryMonitorReposity(String rlkid);



}
