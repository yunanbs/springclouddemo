package com.sailing.baoshan.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.baoshan.service.ExportService;
import com.sailing.baoshan.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by yunan on 2017/6/20.
 */
@Service
public class ExportServiceImp implements ExportService {
    /**
     * 违法记录导出接口
     *
     * @param sourceData 导出数据
     * {
     * "camera-data":[],路口统计结果
     * "licence-data":[],车牌统计结果
     * "illegal-data":[] 违法行为
     * }
     * @return
     */
    @Value("${path.upload-path}")
    private String basePath;
    @Value("${path.upload-uri}")
    private String baseUri;

    @Override
    public String expIllegalAccountData(JSONObject sourceData, String fileName) throws IOException {
        // 获取uuid 作为临时目录
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // 获取存储目录
        String path = basePath + "exp/" + uuid;
        // 创建目录
        makeDirs(path);
        // 获取导出文件的文件名
        String expFileName = String.format("%s/%s.xls", path, fileName);
        // 导出数据
        // 获取设备统计的数据
        JSONArray tmp = sourceData.getJSONArray("camera-data");
        // 组织设备统计的表头
        JSONObject header = new JSONObject(true);
        header.put("id", "编号");
        header.put("accountTag", "路口名称");
        header.put("accountValue", "违法数量");
        tmp.add(0, header);
        // 导出结果
        FileUtils.arrayToExcel(tmp, expFileName, "路口违法行为统计", false);


        // 获取号牌统计的结果
        tmp = sourceData.getJSONArray("licence-data");
        // 添加表头
        header.put("accountTag", "号牌号码");
        tmp.add(0, header);
        // 导出结果
        FileUtils.arrayToExcel(tmp, expFileName, "号牌违法行为统计", false);

        // 获取违法行为统计结果
        tmp = sourceData.getJSONArray("illegal-data");
        // 添加表头
        header.put("accountTag", "违法行为");
        tmp.add(0, header);
        FileUtils.arrayToExcel(tmp, expFileName, "违法行为统计", false);

        // 返回文件发布路径
        return String.format("%sexp/%s/%s.xls", baseUri, uuid, fileName);
    }

    private void makeDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}
