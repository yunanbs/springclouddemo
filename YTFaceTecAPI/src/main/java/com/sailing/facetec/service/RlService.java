package com.sailing.facetec.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlDetailEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.entity.RlShowDetailEntity;

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
     * 模糊查询获取人脸
     * @param rlkid 人脸库id
     * @param status 人脸库标志位
     * @param key 筛选条件
     * @param page 查询页码
     * @param size 每页包含的人脸数
     * @return
     */
   DataEntity<RlShowDetailEntity> listRlShowDetail(String rlkid, String status, String key, int page, int size);


    /**
     * 获取人像库人脸
     * @param rlkid
     * @param status
     * @param page
     * @param size
     * @return
     */
    DataEntity<RlShowDetailEntity> listQueryRlShowDetail(String rlkid, String status, int page, int size);

	/**
     * 保存人像信息到数据库
     * @return
     */
	int saveFaceDetailToDB(String detail, int indexGroup, String faceDetailIndexRespoity);
}
