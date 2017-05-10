package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.sailing.facetec.service.FileService;
import com.sailing.facetec.util.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by yunan on 2017/5/9.
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String createExcel(JSONArray sourceData, String fileFullName, String sheetName, boolean autoHeader) throws IOException {
        return FileUtils.arrayToExcel(sourceData,fileFullName,sheetName,autoHeader);
    }

    @Override
    public String createZip(String dirName, String zipFileFullName) throws IOException {
        return FileUtils.zipFiles(dirName,zipFileFullName);
    }
}
