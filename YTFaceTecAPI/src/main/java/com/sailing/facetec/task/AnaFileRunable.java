package com.sailing.facetec.task;

import com.sailing.facetec.entity.PersonIDEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.PersonIDUntils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/30.
 */
@Data
public class AnaFileRunable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnaFileRunable.class);

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    // 扫描目录
    private String scanPath;
    // 线程文件处理限制
    private long faceScanLimit;
    // 人脸库根目录
    private String faceRepository;

    private RedisService redisService;

    private RlService rlService;



    public AnaFileRunable(String path,long limit,String root,RedisService redis,RlService rl){
        scanPath = path;
        faceScanLimit = limit;
        faceRepository = root;
        redisService = redis;
        rlService = rl;
    }

    @Override
    public void run() {

         if(!redisService.setValNX(scanPath,"ok",1, TimeUnit.MINUTES)){
            return;
        }

        try {
            long use = System.currentTimeMillis();
            LOGGER.info("start deal path {}",scanPath);
            RlEntity[] rlEntities = getRlEntitys();
            LOGGER.info("deal path used {} ms get {} faces", System.currentTimeMillis() - use, rlEntities.length);
        } finally {
            redisService.delKey(scanPath);
        }
    }

    /**
     * 获取人脸信息
     *
     * @return
     */
    private RlEntity[] getRlEntitys() {
        List<RlEntity> result = new ArrayList<>();
        File repository = new File(scanPath);
        File[] faces = repository.listFiles();
        if(!CommUtils.isNullObject(faces))
        {
            for (File face : faces) {
                try {
                    RlEntity tmp = getRlEntityByFile(face);
                    tmp.setRLKID(face.getParentFile().getName().split("-")[0]);
                    if (!CommUtils.isNullObject(tmp) && rlService.addRlData(tmp) > 0) {
                        result.add(tmp);
                        // moveFile(face.getPath(), true, false);
                        moveFile(face.getPath(), String.format("%s\\#fail\\%s",faceRepository,face.getParentFile().getName()),true, false);

                        if (0 != faceScanLimit && faceScanLimit == result.size()) {
                            break;
                        }
                    } else {
                        moveFile(face.getPath(), String.format("%s\\#fail\\%s",faceRepository,face.getParentFile().getName()),false, true);
                    }
                } catch (Exception e) {
                    moveFile(face.getPath(), String.format("%s\\#fail\\%s",faceRepository,face.getParentFile().getName()),false, true);
                    // moveFile(face.getPath(), false, true);
                }
            }
        }

        if(0==repository.listFiles().length){
            LOGGER.info("delete empty dir {}",repository.getName());
            repository.delete();
        }
        RlEntity[] tmp = new RlEntity[result.size()];
        tmp = result.toArray(tmp);
        return tmp;
    }

    /**
     * 移动文件
     * @param filePath
     * @param desPath
     * @param succeed
     * @param keep
     */
    private void moveFile(String filePath, String desPath,boolean succeed, boolean keep) {
        try {
            if (succeed) {
                if (keep) {
                    LOGGER.info("succeed file: {} will be move to {}", filePath, Paths.get(faceRepository, "#succeed"));
                    Files.copy(Paths.get(filePath), Paths.get(desPath,Paths.get(filePath).getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    LOGGER.info("succeed file: {} will be delete", filePath);
                }
            } else {
                LOGGER.error("fail file: {} will be move to  {}", filePath, Paths.get(faceRepository, "#fail"));
                Files.copy(Paths.get(filePath), Paths.get(desPath,Paths.get(filePath).getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                Files.delete(Paths.get(filePath));
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * 提取人像文件对象
     * @param faceFile
     * @return
     * @throws IOException
     */
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
}
