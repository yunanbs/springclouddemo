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
        // 获取带有链接的字段
        JSONObject linkHeads = linkColumns((JSONObject) sourceData.get(0));

        String localFilePath = String.format("%s%s\\查询结果",rootDir,UUID.randomUUID().toString());
        // 创建临时目录
        String filePath = createFilePath(localFilePath);

        sourceData.forEach(s -> {
            // 读取记录
            JSONObject subData = (JSONObject) s;
            linkHeads.keySet().forEach(linkHead -> {
                // 判断 如果是表头数据 则不处理
                if (!subData.getString(linkHead).equals(linkHeads.getString(linkHead))) {

                    // 获取链接字段中的原始文件地址
                    String sourceFilename = subData.getString(linkHead);

                    // 拼接目标文件的文件名
                    StringBuilder desFileName = new StringBuilder();
                    // desFileName.append(String.format("%s-%s.%s", subData.getString("id"), linkHeads.getString(linkHead), sourceFilename.substring(sourceFilename.lastIndexOf(".") + 1)));
                    desFileName.append(String.format("%s-%s.%s", subData.getString("id"), linkHead, sourceFilename.substring(sourceFilename.lastIndexOf(".") + 1)));

                    JSONObject linkJson = new JSONObject();
                    linkJson.put("text",desFileName.toString().replace(linkHead,linkHeads.getString(linkHead)));
                    linkJson.put("val",desFileName.toString());

                    // 更新原始数据中的数据 将链接地址字段内容变为json
                    subData.remove(linkHead);
                    subData.put(linkHead, linkJson);

                    desFileName.insert(0, filePath + "\\");

                    try {
                        // 复制文件
                        copyFile(sourceFilename, desFileName.toString());
                    } catch (IOException e) {
                        // 文件复制失败进行记录
                        logger.error(e.getMessage());
                    }
                }
            });
        });

        // 创建Excel
        createExcel(sourceData, String.format("%s\\%s-查询结果.xls", filePath,CommUtils.dateToStr(new Date(),"yyyyMMddHHmmss")), "查询结果", false);

        String zipLocalPath = FileUtils.zipFiles(filePath,String.format("%s\\查询结果.zip",filePath));
        return zipLocalPath.replace(rootDir,webDir).replace("\\","/");
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
     * @param filePath
     * @return
     */
    private String createFilePath(String filePath) {
        String result = "";
        File file = new File(filePath);
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
