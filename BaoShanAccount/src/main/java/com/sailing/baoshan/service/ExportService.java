package com.sailing.baoshan.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * Created by yunan on 2017/6/20.
 * 数据导出接口
 */
public interface ExportService {
    /**
     * 违法记录导出接口
     * @param sourceData 导出数据
     * {
     *   "camera-data":[],路口统计结果
     *   "licence-data":[],车牌统计结果
     *   "illegal-data":[] 违法行为
     * }
     * @param fileName 导出文件名
     * @return
     */
    String expIllegalAccountData(JSONObject sourceData,String fileName) throws IOException;
}
