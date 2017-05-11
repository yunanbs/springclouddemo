package com.sailing.facetec.service;


import com.alibaba.fastjson.JSONArray;

import java.io.IOException;

/**
 * Created by yunan on 2017/5/9.
 */
public interface FileService  {
    String createExcel(JSONArray sourceData, String fileFullName, String sheetName, boolean autoHeader) throws IOException;

    String createZip(String dirName,String zipFileFullName) throws IOException;

    String expDataWithPic(JSONArray sourceData) throws IOException;
}
