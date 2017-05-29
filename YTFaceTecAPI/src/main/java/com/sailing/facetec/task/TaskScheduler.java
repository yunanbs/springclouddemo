package com.sailing.facetec.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.PersonIDEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.queue.DataQueue;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FastJsonUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.PersonIDUntils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.*;
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
    @Value("${tasks.capture-cache}")
    private int captureCache;
    @Value("${tasks.alert-cache}")
    private int alertCache;
    @Value("${tasks.alert-limit}")
    private double alertLimit;

    @Value("${redis-keys.repository-lock}")
    private String repositoryLock;
    @Value("${facepic.repository}")
    private String faceRepository;
    @Value("${tasks.sacn-limit}")
    private int faceScanLimit;

    /**
     * 抽取抓拍记录
     */
    //@Scheduled(cron = "${tasks.capture}")
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
            use = use - System.currentTimeMillis();
            LOGGER.info("抽取抓拍数据 {} 用时 {} ms", result.getDataContent().size(), use);
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
            use = use - System.currentTimeMillis();
            LOGGER.info("抽取报警数据 {} 用时 {} ms", result.getDataContent().size(), use);
        }
    }

    /**
     * 人像批量导入任务
     */
    @Scheduled(fixedDelay = 500L)
    @Transactional
    public void impRepositoryScheduler() {
        if (DataQueue.isEmpty()) {
            LOGGER.info("没有人像导入任务");
            return;
        }

        try {
            String xlsFile = DataQueue.takeFromQueue();
            LOGGER.info("获取人像文件 {}", xlsFile);
            RlEntity[] rlEntities = getRlEntityByXls(xlsFile);
            LOGGER.info("获取人像列表 {} ", rlEntities.length);
            Arrays.asList(rlEntities).forEach(rlEntity -> {
                String picPath = rlEntity.getBase64Pic();
                try {
                    rlEntity.setBase64Pic(FileUtils.fileToBase64(picPath));
                    rlEntity.setRLKID("2");
                    rlService.addRlData(rlEntity);
                } catch (Exception e) {
                    LOGGER.info("人像 {} 导入失败 {}", rlEntity.getXM(), e.getMessage());
                }
            });
            LOGGER.info("人像导入完成");
        } catch (Exception e) {
            LOGGER.info("人像导入失败 {}", e.getMessage());
        }
    }

    @Scheduled(cron = "${tasks.scan-face-repository}")
    public void scanRepositoryScheduler() {
        if (!redisService.setValNX(repositoryLock, lockVal, 2, TimeUnit.HOURS)) {
            return;
        }

        long use = System.currentTimeMillis();
        LOGGER.info("启动人像扫描任务 扫描根文件夹 {}", faceRepository);

        LOGGER.info("启动解压缩任务");
        unZipFiles();
        LOGGER.info("解压缩任务耗时 {} ms", System.currentTimeMillis() - use);

        use = System.currentTimeMillis();
        LOGGER.info("启动读取人脸信息任务");
        RlEntity[] rlEntities = getRlEntitys();
        LOGGER.info("读取人脸任务耗时 {} ms 共读取 {} 张人脸", System.currentTimeMillis() - use, rlEntities.length);

        redisService.delKey(repositoryLock);

    }

    /**
     * 解压缩文件包
     */
    private void unZipFiles() {
        // 获取根路径
        File root = new File(faceRepository);
        // 获取根路径下的文件
        File[] zips = root.listFiles();
        if (0 == zips.length) {
            LOGGER.info("没有需要处理的zip文件");
        }
        for (File zip : zips) {
            // 文件夹不需要处理
            if (zip.isDirectory()) {
                continue;
            }
            // 从zip文件名获取库id
            String repositoryID = zip.getName().split("-")[0];
            try {
                // 解压缩文件
                FileUtils.unZipFile(zip.getPath(), Paths.get(faceRepository, repositoryID).toString(), false);
                dealFile(zip.getPath(),true,true);
            } catch (IOException e) {
                dealFile(zip.getPath(),false,true);
            }
        }
    }

    /**
     * 获取人脸信息
     *
     * @return
     */
    private RlEntity[] getRlEntitys() {
        List<RlEntity> result = new ArrayList<>();
        File root = new File(faceRepository);
        File[] repositorys = root.listFiles();
        for (File repository : repositorys) {
            if (repository.isDirectory() && !repository.getName().startsWith("#")) {
                File[] faces = repository.listFiles();
                for (File face : faces) {
                    try {
                        RlEntity tmp = getRlEntityByFile(face);
                        tmp.setRLKID(face.getParentFile().getName());
                        if (!CommUtils.isNullObject(tmp)&&rlService.addRlData(tmp)>0) {
                            result.add(tmp);
                            dealFile(face.getPath(),true,false);
                            if (0 != faceScanLimit && faceScanLimit == result.size()) {
                                break;
                            }
                        } else {
                            dealFile(face.getPath(),false,true);
                        }
                    } catch (Exception e) {
                        dealFile(face.getPath(),false,true);
                    }
                }
            }
        }
        RlEntity[] tmp = new RlEntity[result.size()];
        tmp = result.toArray(tmp);
        return tmp;
    }

    private void dealFile(String Filepath, boolean succeed, boolean keep) {
        try {
            if (succeed) {
                if(keep) {
                    LOGGER.info("文件处理成功: {} 将被移动到 {}", Filepath, Paths.get(faceRepository, "#succeed"));
                    Files.move(Paths.get(Filepath), Paths.get(faceRepository, "#succeed", Paths.get(Filepath).getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                }else{
                    LOGGER.info("文件处理成功: {} 将被删除", Filepath);
                }
            } else {
                LOGGER.error("文件处理失败: {} 将被移动到 {}", Filepath, Paths.get(faceRepository, "#fail"));
                Files.move(Paths.get(Filepath), Paths.get(faceRepository, "#fail",Paths.get(Filepath).getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }finally {
            try {
                Files.delete(Paths.get(Filepath));
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private RlEntity getRlEntityByFile(File faceFile) throws IOException {
        RlEntity result = new RlEntity();
        String[] faceInfo = FileUtils.dealPicFileName(faceFile.getName());
        if (18 != faceInfo[1].length()) {
            return null;
        }

        PersonIDEntity personIDEntity = PersonIDUntils.getPersonInfo(faceInfo[0], faceInfo[1]);
        result.setXM(personIDEntity.getName());
        result.setSFZH(personIDEntity.getId());
        result.setCSNF(personIDEntity.getBirthDay());
        switch (personIDEntity.getGender()) {
            case "男":
                result.setXB(1);
                break;
            case "女":
                result.setXB(2);
                break;
            default:
                result.setXB(0);
                break;
        }
        result.setRLSF(personIDEntity.getProvince());
        result.setRLCS(personIDEntity.getCity());
        result.setBase64Pic(FileUtils.fileToBase64(faceFile.getPath()));
        result.setXGSJ(CommUtils.getCurrentDate());
        result.setTJSJ(CommUtils.getCurrentDate());
        return result;
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
                rlEntity.setBase64Pic(String.format("%s\\%s.jpg", excelFile.getParent(), r.getCell(7).getStringCellValue()));
                rlEntity.setXGSJ(CommUtils.getCurrentDate());
                rlEntity.setTJSJ(CommUtils.getCurrentDate());
                rlEntities.add(rlEntity);
            }
        });
        RlEntity[] results = new RlEntity[rlEntities.size()];
        return rlEntities.toArray(results);
    }
}
