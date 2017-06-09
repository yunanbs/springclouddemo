package com.sailing.facetec.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RllrDetailEntity;

/**
 * Created by yunan on 2017/4/28.
 * 路人库Service
 */
public interface RllrService {
    /**
     * 获取历史路人信息
     * @param beginTime
     * @param endTime
     * @param orderBy
     * @param page
     * @param size
     * @param lrkids
     * @param sex
     * @param age
     * @param glass
     * @param fringe
     * @param uygur
     * @return
     */
    DataEntity<RllrDetailEntity> listRllrDetail(String beginTime,String endTime,String orderBy, int page, int size,String lrkids,String sex,String age,String glass,String fringe,String uygur);

    /**
     * 获取实时路人信息
     * @param lrkids
     * @return
     */
    String listRllrDetailReal(String lrkids);

    /**
     * 人脸信息检索
     * @param picStr
     * @param repositorys
     * @param threshold
     * @param beginTime
     * @param endTime
     * @return
     */
    JSONArray listFaceQueryInfo(String picStr, Integer[] repositorys, double threshold, String beginTime, String endTime);

    /**
     * 地图多人脸检索
     * @param picStrs 人脸图片base64字符串
     * @param repositorys 检索人脸库
     * @param threshold 最低相似度
     * @param beginTime 检索开始时间
     * @param endTime 检索结束时间
     * @param mergeFlag 多结果集合并标记位 1 交集 2并集
     * @param offset 并集情况下的时间偏移量
     * @return
     */
    JSONObject mapMutilFaceQuery(String[] picStrs, Integer[] repositorys, double threshold, String beginTime, String endTime, int mergeFlag, long offset);
}
