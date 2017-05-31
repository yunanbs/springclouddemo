package com.sailing.facetec.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.PersonIDEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FastJsonUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.PersonIDUntils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
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

    @Value("${gc.exp}")
    private int gcExpLimit;
    @Value("${gc.repo}")
    private int gcRepoLimit;
    @Value("${gc.tmpimage}")
    private int gcTmpImageLimit;
    @Value("${gc.capture}")
    private int gcCaptureLimit;

    @Value("${exp.root-dir}")
    private String gcExpPath;
    @Value("${facepic.repository}")
    private String gcRepoPath;
    @Value("${upload.image-folder}")
    private String gcTmpImagepPath;
    @Value("${facepic.capture}")
    private String gcCaturePath;

    /**
     * 抽取抓拍记录
     */
    @Scheduled(cron = "${tasks.capture}")
    public void captureScheduler() {
        // 获取锁
        if (!redisService.setValNX(captureLock, lockVal, 1, TimeUnit.SECONDS)) {
            return;
        }
        try {
            long use = System.currentTimeMillis();
            // 按配置抽取历史数据
            DataEntity result = rllrService.listRllrDetail("", "", "", 1, captureCache, "", "", "", "", "", "");
            // 更新实时数据
            redisService.setVal(captureData, JSON.toJSONString(result, FastJsonUtils.nameFilter, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero), 1, TimeUnit.DAYS);

            use = use - System.currentTimeMillis();
            LOGGER.info("capture logs {} used {} ms", result.getDataContent().size(), use);
        } finally {
            // 释放锁
            redisService.delKey(captureLock);
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
            LOGGER.info("alert logs {} used {} ms", result.getDataContent().size(), use);
        }
    }


    @Scheduled(cron = "${tasks.scan-face-repository}")
    public void scanRepositoryScheduler() {
        // 获取锁
        if (!redisService.setValNX(repositoryLock, lockVal, 1, TimeUnit.MINUTES)) {
            return;
        }

        try {
            long use = System.currentTimeMillis();
            LOGGER.info("Start scan path {}", faceRepository);

            LOGGER.info("start unzip");
            unZipFiles();
            LOGGER.info("unzip used {} ms", System.currentTimeMillis() - use);

            use = System.currentTimeMillis();
            LOGGER.info("start deal face files");
            RlEntity[] rlEntities = getRlEntitys();
            LOGGER.info("deal face files uesc {} ms get {} faces", System.currentTimeMillis() - use, rlEntities.length);
        } finally {
            // 释放锁
            redisService.delKey(repositoryLock);
        }
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
            LOGGER.info("no file to deal");
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
                dealFile(zip.getPath(), true, true);
            } catch (IOException e) {
                dealFile(zip.getPath(), false, true);
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
                        if (!CommUtils.isNullObject(tmp) && rlService.addRlData(tmp) > 0) {
                            result.add(tmp);
                            dealFile(face.getPath(), true, false);
                            if (0 != faceScanLimit && faceScanLimit == result.size()) {
                                break;
                            }
                        } else {
                            dealFile(face.getPath(), false, true);
                        }
                    } catch (Exception e) {
                        dealFile(face.getPath(), false, true);
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
                if (keep) {
                    LOGGER.info("succeed file: {} will be move to {}", Filepath, Paths.get(faceRepository, "#succeed"));
                    Files.move(Paths.get(Filepath), Paths.get(faceRepository, "#succeed", Paths.get(Filepath).getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    LOGGER.info("succeed file: {} will be delete", Filepath);
                }
            } else {
                LOGGER.error("fail file: {} will bu move to  {}", Filepath, Paths.get(faceRepository, "#fail"));
                Files.move(Paths.get(Filepath), Paths.get(faceRepository, "#fail", Paths.get(Filepath).getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
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

    @Scheduled(cron = "*/30 * * * * ?")
    public void GCScheduler() {
        // 删除exp文件
        long used = System.currentTimeMillis();
        LOGGER.info("start gc");

        LOGGER.info("gc {} delete {} files", gcExpPath,gc(gcExpPath, gcExpLimit, null));

        LOGGER.info("gc {} delete {} files", gcTmpImagepPath,gc(gcTmpImagepPath, gcTmpImageLimit, null));

        LOGGER.info("gc {} delete {} files", gcCaturePath,gc(gcCaturePath, gcCaptureLimit, null));

        List<String> exclude = new ArrayList<>();
        exclude.add("#succeed");
        exclude.add("#fail");
        LOGGER.info("gc {} delete {} files", gcRepoPath,gc(gcRepoPath, gcRepoLimit,exclude));

        LOGGER.info("end gc used {} ms",used);
    }

    /**
     * 文件清理
     * @param path
     * @param limitTime
     * @param exclude
     * @return
     */
    private long gc(String path, long limitTime, List<String> exclude) {
        long result=0L;
        long gclimit = System.currentTimeMillis();
        gclimit = gclimit - limitTime * 24 * 60 * 60 * 1000;
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        if(null==files){
            return  result;
        }
        for (File sub : files) {
            if (sub.lastModified() < gclimit) {
                if (null != exclude && exclude.contains(sub.getName())) {
                    continue;
                }
                sub.delete();
                result++;
            }
        }
        return result;
    }

    //region 从 excel 获取人像库数据 （废弃）
    // /**
    //  * 人像批量导入任务
    //  */
    // @Scheduled(fixedDelay = 500L)
    // @Transactional
    // public void impRepositoryScheduler() {
    //     if (DataQueue.isEmpty()) {
    //         LOGGER.info("没有人像导入任务");
    //         return;
    //     }
    //
    //     try {
    //         String xlsFile = DataQueue.takeFromQueue();
    //         LOGGER.info("获取人像文件 {}", xlsFile);
    //         RlEntity[] rlEntities = getRlEntityByXls(xlsFile);
    //         LOGGER.info("获取人像列表 {} ", rlEntities.length);
    //         Arrays.asList(rlEntities).forEach(rlEntity -> {
    //             String picPath = rlEntity.getBase64Pic();
    //             try {
    //                 rlEntity.setBase64Pic(FileUtils.fileToBase64(picPath));
    //                 rlEntity.setRLKID("2");
    //                 rlService.addRlData(rlEntity);
    //             } catch (Exception e) {
    //                 LOGGER.info("人像 {} 导入失败 {}", rlEntity.getXM(), e.getMessage());
    //             }
    //         });
    //         LOGGER.info("人像导入完成");
    //     } catch (Exception e) {
    //         LOGGER.info("人像导入失败 {}", e.getMessage());
    //     }
    // }

    // /**
    //  * 从 excel 获取人像库数据 （废弃）
    //  *
    //  * @param excelPath
    //  * @return
    //  * @throws IOException
    //  */
    // private RlEntity[] getRlEntityByXls(String excelPath) throws IOException {
    //     File excelFile = new File(excelPath);
    //     // 返回结果集
    //     List<RlEntity> rlEntities = new ArrayList();
    //     // 打开工作簿
    //     HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(excelFile));
    //     hssfWorkbook.getSheetAt(0).forEach(r -> {
    //         if (0 != r.getRowNum()) {
    //             RlEntity rlEntity = new RlEntity();
    //             rlEntity.setXM(r.getCell(1).getStringCellValue());
    //             rlEntity.setSFZH(r.getCell(2).getStringCellValue());
    //             rlEntity.setCSNF(r.getCell(3).getStringCellValue());
    //             switch (r.getCell(4).getStringCellValue()) {
    //                 case "男":
    //                     rlEntity.setXB(1);
    //                     break;
    //                 case "女":
    //                     rlEntity.setXB(2);
    //                     break;
    //                 default:
    //                     rlEntity.setXB(0);
    //                     break;
    //             }
    //             rlEntity.setRLSF(r.getCell(5).getStringCellValue());
    //             rlEntity.setRLCS(r.getCell(6).getStringCellValue());
    //             rlEntity.setBase64Pic(String.format("%s\\%s.jpg", excelFile.getParent(), r.getCell(7).getStringCellValue()));
    //             rlEntity.setXGSJ(CommUtils.getCurrentDate());
    //             rlEntity.setTJSJ(CommUtils.getCurrentDate());
    //             rlEntities.add(rlEntity);
    //         }
    //     });
    //     RlEntity[] results = new RlEntity[rlEntities.size()];
    //     return rlEntities.toArray(results);
    // }
    //endregion
}
