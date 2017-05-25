package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;

import java.text.ParseException;

/**
 * Created by yunan on 2017/4/28.
 * 摄像头Service
 */
public interface RlsxtService {
    /**
     * 获取摄像头信息
     * @return
     */
    DataEntity<SxtDetailEntity> listAllXST();

    /**
     * 获取摄像头相关单位信息
     * @return
     */
    DataEntity<SxtdwEntity> listAllSXTDW();

    /**
     * 添加摄像头
     * @param sxtEntity
     * @return
     */
    int addSXT(SxtEntity sxtEntity);

    /**
     * 摄像头布控人脸库
     * @param bkrwEntity
     * @return
     * @throws ParseException
     */
    int addMonitorReposity(BkrwEntity bkrwEntity) throws ParseException;

    /**
     * 摄像头布控路人库
     * @param bkrwEntity
     * @return
     */
    int addMonitorByCamera(BkrwEntity bkrwEntity);

    /**
     * 删除摄像头
     * @param cameraID
     * @return
     */
    int removeCamera(String cameraID);
}
