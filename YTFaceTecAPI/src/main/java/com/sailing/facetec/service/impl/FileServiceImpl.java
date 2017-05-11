package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.service.FileService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yunan on 2017/5/9.
 */
@Service
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${exp.root-dir}")
    private String rootDir;

    @Value("${exp.web-path}")
    private String webDir;

    @Override
    public String createExcel(JSONArray sourceData, String fileFullName, String sheetName, boolean autoHeader) throws IOException {
        return FileUtils.arrayToExcel(sourceData, fileFullName, sheetName, autoHeader);
    }

    @Override
    public String createZip(String dirName, String zipFileFullName) throws IOException {
        return FileUtils.zipFiles(dirName, zipFileFullName);
    }

    @Override
    public String expDataWithPic(JSONArray sourceData) throws IOException {
        String result = "";
        JSONObject linkHeads = linkColumns((JSONObject) sourceData.get(0));
        String filePath = createFilePath(UUID.randomUUID().toString());
        sourceData.forEach(s -> {
            JSONObject subData = (JSONObject) s;
            linkHeads.keySet().forEach(linkHead -> {
                if (!subData.getString(linkHead).equals(linkHeads.getString(linkHead))) {
                    String sourceFilename = subData.getString(linkHead);
                    StringBuilder desFileName = new StringBuilder();
                    desFileName.append(String.format("%s-%s.%s", subData.getString("id"), linkHeads.getString(linkHead), sourceFilename.substring(sourceFilename.lastIndexOf(".") + 1)));
                    JSONObject tmp = new JSONObject();
                    tmp.put("link-text",desFileName.toString());
                    desFileName.insert(0, filePath + "\\");
                    tmp.put("link-value",desFileName.toString());
                    subData.remove(linkHead);
                    subData.put(linkHead, tmp);

                    try {
                        copyFile(sourceFilename, desFileName.toString());
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            });
        });

        createExcel(sourceData, String.format("%s\\%s-查询结果.xls", filePath,CommUtils.dateToStr(new Date(),"yyyyMMddHHmmss")), "查询结果", false);

        return filePath;
    }

    /**
     * 获取json中包含图片信息的属性
     *
     * @param sourceData
     * @return
     */
    private JSONObject linkColumns(JSONObject sourceData) {
        JSONObject result = new JSONObject();

        sourceData.keySet().forEach(s -> {
            if (s.startsWith("link-")) {
                result.put(s, sourceData.getString(s));
            }
        });
        return result;
    }

    /**
     * 创建随机文件夹
     *
     * @param uuid
     * @return
     */
    private String createFilePath(String uuid) {
        String result = "";
        File file = new File(rootDir + "\\" + uuid);
        if (file.mkdirs()) {
            result = file.getPath();
        }
        return result;
    }

    private boolean copyFile(String sourceFile, String desFile) throws IOException {
        boolean result = false;
        File inFile = new File(sourceFile);
        File outFile = new File(desFile);
        if (!inFile.exists()) {
            return result;
        }

        if (outFile.exists()) {
            new File(desFile).delete();
        }

        InputStream inputStream = new FileInputStream(inFile);
        OutputStream outputStream = new FileOutputStream(outFile);
        byte[] buff = new byte[4096];

        try {
            while (inputStream.read(buff) > 0) {
                outputStream.write(buff);
            }
            result = true;
        } finally {
            outputStream.close();
            inputStream.close();
        }

        return result;
    }
}
