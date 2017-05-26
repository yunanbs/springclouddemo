package com.sailing.facetec.service;


import com.alibaba.fastjson.JSONArray;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlEntity;

import java.io.IOException;

/**
 * Created by yunan on 2017/5/4.
 * 人脸检索Service
 */
public interface RlService {
    /**
     * 从人脸库或路人库中按算法厂商库编号及人脸编号进行检索
     * 支持多算法厂商记录检索
     * @param detailInfo
     * @return
     */
    DataEntity listRlDetail(JSONArray detailInfo);

    /**
     * 添加人像记录
     * @param rlEntity
     * @return
     */
    int addRlData(RlEntity rlEntity) throws Exception;

    /**
     * 批量导入人像记录
     * @return
     */
    int impRlDatas(String repositoryID,String zipFile) throws IOException, InterruptedException;
}
