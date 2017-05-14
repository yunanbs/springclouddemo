package com.sailing.facetec.service;


import com.alibaba.fastjson.JSONArray;

import java.io.IOException;

/**
 * 文件服务接口
 * Created by yunan on 2017/5/9.
 */
public interface FileService  {
    /**
     * jsonArray 导出 excel
     * @param sourceData 原始数据
     * @param fileFullName 导出文件全路径
     * @param sheetName sheet名称
     * @param autoHeader 是否按照jsonObject创建表头
     * @return 导出文件路径
     * @throws IOException
     */
    String createExcel(JSONArray sourceData, String fileFullName, String sheetName, boolean autoHeader) throws IOException;

    /**
     * 压缩文件夹
     * @param dirName 待压缩文件夹路径
     * @param zipFileFullName 压缩文件全路径
     * @return 压缩文件路径
     * @throws IOException
     */
    String createZip(String dirName,String zipFileFullName) throws IOException;

    /**
     * jsonArray 导出为zip （包含关联文件）
     * @param sourceData
     * @return
     * @throws IOException
     */
    String expDataWithPic(JSONArray sourceData) throws IOException;
}
