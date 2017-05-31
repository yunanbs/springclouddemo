package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlkEntity;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 * 人脸库Service
 */
public interface RlkService {
    /**
     * 获取人脸库
     * @return
     */
    DataEntity<RlkEntity> listAllRlk();

    /**
     * 新增人脸库
     * @param rlkEntity
     * @return
     */
    String addFaceLib(RlkEntity rlkEntity);

    /**
     * 删除人脸库
     * @param rlkID
     * @return
     */
    int delFaceLib(String rlkID);
}
