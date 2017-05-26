package com.sailing.facetec.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.queue.DataQueue;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FastJsonUtils;
import com.sailing.facetec.util.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/7.
 * 抽取实时数据任务
 */
@Component(value = "com.sailing.facetec.task.TaskScheduler")
public class TaskScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    private RllrService rllrService;

    @Autowired
    private RlgjService rlgjService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RlService rlService;

    @Value("${redis-keys.capture-lock}")
    private String captureLock;
    @Value("${redis-keys.alert-lock}")
    private String alertLock;
    @Value("${redis-keys.lock-val}")
    private String lockVal;
    @Value("${redis-keys.capture-data}")
    private String captureData;
    @Value("${redis-keys.alert-data}")
    private String alertData;
    @Value("${redis-keys.capture-cache}")
    private int captureCache;
    @Value("${redis-keys.alert-cache}")
    private int alertCache;
    @Value("${redis-keys.alert-limit}")
    private double alertLimit;


    /**
     * 抽取抓拍记录
     */
    @Scheduled(cron = "${tasks.capture}")
    public void captureScheduler() {
        // 获取锁
        if (redisService.setValNX(captureLock, lockVal, 1, TimeUnit.SECONDS)) {
            long use = System.currentTimeMillis();
            // 按配置抽取历史数据
            DataEntity result = rllrService.listRllrDetail("", "", "", 1, captureCache, "", "", "", "", "", "");
            // 更新实时数据
            redisService.setVal(captureData, JSON.toJSONString(result, FastJsonUtils.nameFilter, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero), 1, TimeUnit.DAYS);
            // 释放锁
            redisService.delKey(captureLock);
            use = use-System.currentTimeMillis();
            LOGGER.info("抽取抓拍数据 {} 用时 {} ms",result.getDataContent().size(),use);
        }
    }

    /**
     * 抽取告警记录
     */
    @Scheduled(cron = "${tasks.alert}")
    public void alterScheduler() {
        // 获取锁
        if (redisService.setValNX(alertLock, lockVal, 1, TimeUnit.SECONDS)) {
            long use = System.currentTimeMillis();
            // 按配置抽取历史数据
            DataEntity result = rlgjService.listRlgjDetail("", "", "", 1, alertCache, alertLimit, "", "", "", "", "", "", "", "");
            // 更新实时数据
            redisService.setVal(alertData, JSON.toJSONString(result, FastJsonUtils.nameFilter, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero), 1, TimeUnit.DAYS);
            // 释放锁
            redisService.delKey(alertLock);
            use = use-System.currentTimeMillis();
            LOGGER.info("抽取报警数据 {} 用时 {} ms",result.getDataContent().size(),use);
        }
    }

    /**
     * 人像批量导入任务
     */
    @Scheduled(fixedDelay = 500L)
    @Transactional
    public void impRepositoryScheduler() {
        if(DataQueue.isEmpty()){
            LOGGER.info("没有人像导入任务");
            return;
        }

        try {
            String xlsFile = DataQueue.takeFromQueue();
            LOGGER.info("获取人像文件 {}",xlsFile);
            RlEntity[] rlEntities = getRlEntityByXls(xlsFile);
            LOGGER.info("获取人像列表 {} ",rlEntities.length);
            Arrays.asList(rlEntities).forEach(rlEntity -> {
                String picPath = rlEntity.getBase64Pic();
                try {
                    rlEntity.setBase64Pic(FileUtils.fileToBase64(picPath));
                    rlEntity.setRLKID("2");
                    rlService.addRlData(rlEntity);
                } catch (Exception e) {
                    LOGGER.info("人像 {} 导入失败 {}",rlEntity.getXM(),e.getMessage());
                }
            });
            LOGGER.info("人像导入完成");
        } catch (Exception e) {
            LOGGER.info("人像导入失败 {}",e.getMessage());
        }
    }

    /**
     * 获取人像库数据
     *
     * @param excelPath
     * @return
     * @throws IOException
     */
    private RlEntity[] getRlEntityByXls(String excelPath) throws IOException {
        File excelFile = new File(excelPath);
        // 返回结果集
        List<RlEntity> rlEntities = new ArrayList();
        // 打开工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(excelFile));
        hssfWorkbook.getSheetAt(0).forEach(r -> {
            if (0 != r.getRowNum()) {
                RlEntity rlEntity = new RlEntity();
                rlEntity.setXM(r.getCell(1).getStringCellValue());
                rlEntity.setSFZH(r.getCell(2).getStringCellValue());
                rlEntity.setCSNF(r.getCell(3).getStringCellValue());
                switch (r.getCell(4).getStringCellValue()) {
                    case "男":
                        rlEntity.setXB(1);
                        break;
                    case "女":
                        rlEntity.setXB(2);
                        break;
                    default:
                        rlEntity.setXB(0);
                        break;
                }
                rlEntity.setRLSF(r.getCell(5).getStringCellValue());
                rlEntity.setRLCS(r.getCell(6).getStringCellValue());
                rlEntity.setBase64Pic(String.format("%s\\%s.jpg",excelFile.getParent(), r.getCell(7).getStringCellValue()));
                rlEntity.setXGSJ(CommUtils.getCurrentDate());
                rlEntity.setTJSJ(CommUtils.getCurrentDate());
                rlEntities.add(rlEntity);
            }
        });
        RlEntity[] results = new RlEntity[rlEntities.size()];
        return rlEntities.toArray(results);
    }
}
