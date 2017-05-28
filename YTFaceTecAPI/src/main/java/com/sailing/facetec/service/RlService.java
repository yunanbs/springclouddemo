package com.sailing.facetec.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlDetailEntity;
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
    DataEntity listRlDetailByRlidAndSupply(JSONArray detailInfo);

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

    /**
     * 修改人员信息
     * @param
     * @return
     */
    int altPersonalInfo(JSONObject jsonObject);

    /**
     * 删除人员
     * @param rlid
     * @return
     */
    int delPersonal(String rlid);

    /**
     * 人脸库人脸记录模糊查询
     * @param rlkid 人脸库编号
     * @param key 查询关键字
     * @param page 页码
     * @param size 分页大小
     * @return
     */
    RlDetailEntity listRlDetail(String rlkid,String key,int page,int size);

}
