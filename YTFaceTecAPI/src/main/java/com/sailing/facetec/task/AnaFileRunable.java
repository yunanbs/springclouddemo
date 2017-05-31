package com.sailing.facetec.task;

import com.sailing.facetec.entity.PersonIDEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.PersonIDUntils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunan on 2017/5/30.
 */
@Data
public class AnaFileRunable implements Runnable {

    // TODO: 2017/5/31  需要测试 
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
    private int faceScanLimit;
    // 人脸服务
    private RlService rlService;
    // 人脸库根目录
    @Value("${facepic.repository}")
    private String faceRepository;
    @Override
    public void run() {

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
}
