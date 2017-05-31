package com.sailing.facetec.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.entity.FaceRetrievalResultEntity;
import com.sailing.facetec.service.FaceRetrievalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by eagle on 2017-5-3.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
public class ImageAnalysisController {
    private final Logger logger = LoggerFactory.getLogger(ImageAnalysisController.class);
    @Autowired
    private FaceRetrievalService faceRetrievalService;

    @Value("${upload.image-folder}")
    private String imageFolder;
    @Value("${upload.image-url}")
    private String imageUrl;


    @RequestMapping(value = "/Analysis/Compare", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ActionResult listRealDetails(@RequestParam(value = "image", required = false) MultipartFile file, @RequestParam(name = "dataContent", defaultValue = "") String ids, @RequestParam(name = "originalUrl", defaultValue = "") String originalUrl, @RequestParam(name = "type", defaultValue = "portrait") String type) {
        //type : stranger路人库  portrait人像库
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'), file.getOriginalFilename().length());
        String imagePath = imageFolder  + fileName;
        String url = "";
        try {
            if (originalUrl.trim().isEmpty()) {
                File localFile = new File(imagePath);
                if (localFile.exists()) {
                    localFile.delete();
                }
                Files.copy(file.getInputStream(), Paths.get(imagePath));
                url = imageUrl + fileName;
            } else {
                url = originalUrl;
            }
            /*if (type.equals("portrait")) {
                ids = "[{\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \",\"rlkmc\":\"在逃人员库3\",\"rlkid\":\"238\",\"ylzd1\":\" \",\"ylzd2\":\" \",\"xh\":952794,\"rlklx\":1,\"cclx\":0,\"tjsj\":1494402329000,\"xgsj\":1494402329000,\"tjrbh\":\" \",\"tjr\":\" \",\"xgrbh\":\" \",\"xgr\":\" \",\"wcbl\":0,\"zt\":0,\"rlsl\":0,\"tpsl\":0,\"jzlx\":0,\"bz\":\" \",\"rlkid1\":null,\"rlkid2\":null,\"rlkid3\":null,\"rlkid4\":null,\"rlkid5\":null,\"rlkid6\":null,\"rlkid7\":null,\"rlkid8\":null,\"rlkid9\":null,\"rlkid10\":null},{\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \",\"rlkmc\":\"在逃人员库5\",\"rlkid\":\"240\",\"ylzd1\":\" \",\"ylzd2\":\" \",\"xh\":952803,\"rlklx\":1,\"cclx\":0,\"tjsj\":1494402445000,\"xgsj\":1494402445000,\"tjrbh\":\" \",\"tjr\":\" \",\"xgrbh\":\" \",\"xgr\":\" \",\"wcbl\":0,\"zt\":0,\"rlsl\":0,\"tpsl\":0,\"jzlx\":0,\"bz\":\" \",\"rlkid1\":null,\"rlkid2\":null,\"rlkid3\":null,\"rlkid4\":null,\"rlkid5\":null,\"rlkid6\":null,\"rlkid7\":null,\"rlkid8\":null,\"rlkid9\":null,\"rlkid10\":null},{\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \",\"rlkmc\":\"在逃人员库4\",\"rlkid\":\"239\",\"ylzd1\":\" \",\"ylzd2\":\" \",\"xh\":952797,\"rlklx\":1,\"cclx\":0,\"tjsj\":1494402352000,\"xgsj\":1494402352000,\"tjrbh\":\" \",\"tjr\":\" \",\"xgrbh\":\" \",\"xgr\":\" \",\"wcbl\":0,\"zt\":0,\"rlsl\":0,\"tpsl\":0,\"jzlx\":0,\"bz\":\" \",\"rlkid1\":null,\"rlkid2\":null,\"rlkid3\":null,\"rlkid4\":null,\"rlkid5\":null,\"rlkid6\":null,\"rlkid7\":null,\"rlkid8\":null,\"rlkid9\":null,\"rlkid10\":null}]";
            }
            if (type.equals("stranger")) {
                ids = "[{\"sxtid\":\"74\",\"sxtmc\":\"456\",\"yhz\":\"31011318\",\"spdz\":\"654\",\"sbbh\":\"22018124\",\"sxtlx\":0,\"xh\":952725,\"lrkid\":\"234\",\"ylzd1\":\"1\",\"ylzd2\":\" \",\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \"},{\"sxtid\":\"72\",\"sxtmc\":\"234\",\"yhz\":\"31011318\",\"spdz\":\"432\",\"sbbh\":\"22018146\",\"sxtlx\":0,\"xh\":952723,\"lrkid\":\"232\",\"ylzd1\":\"1\",\"ylzd2\":\" \",\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \"},{\"sxtid\":\"73\",\"sxtmc\":\"345\",\"yhz\":\"31011318\",\"spdz\":\"543\",\"sbbh\":\"22018028\",\"sxtlx\":0,\"xh\":952724,\"lrkid\":\"233\",\"ylzd1\":\"1\",\"ylzd2\":\" \",\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \"},{\"sxtid\":\"71\",\"sxtmc\":\"123\",\"yhz\":\"31011318\",\"spdz\":\"312\",\"sbbh\":\"22018055\",\"sxtlx\":0,\"xh\":952722,\"lrkid\":\"231\",\"ylzd1\":\"1\",\"ylzd2\":\" \",\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \"},{\"sxtid\":\"75\",\"sxtmc\":\"736565\",\"yhz\":\"31011324\",\"spdz\":\"76765\",\"sbbh\":\"33014026\",\"sxtlx\":0,\"xh\":952726,\"lrkid\":\"235\",\"ylzd1\":\"1\",\"ylzd2\":\" \",\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \"},{\"sxtid\":\"70\",\"sxtmc\":\"sailing\",\"yhz\":\"31011327\",\"spdz\":\"rtsp://admin:admin@172.20.25.17:554\",\"sbbh\":\"34020000001320000001\",\"sxtlx\":0,\"xh\":952670,\"lrkid\":\"229\",\"ylzd1\":\"1\",\"ylzd2\":\" \",\"ylzd3\":\" \",\"ylzd4\":\" \",\"ylzd5\":\" \"}]";
            }*/
            logger.debug("库记录:" + ids);
            ActionResult resutl = faceRetrievalService.getRetrievalDetail(url, ids, type);
            return resutl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ActionResult(ActionCodeConfig.getServerErrorCode(), ActionCodeConfig.getServerErrorMsg(), null, url);
    }
}
