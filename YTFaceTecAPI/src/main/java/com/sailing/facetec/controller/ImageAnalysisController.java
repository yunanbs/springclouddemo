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
import java.nio.file.StandardCopyOption;
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
        String url = "";
        if (file == null) {
            url = originalUrl;
        } else {
            //type : stranger路人库  portrait人像库
            String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'), file.getOriginalFilename().length());
            String imagePath = imageFolder + fileName;

            try {

                File localFile = new File(imagePath);
                if (localFile.exists()) {
                    localFile.delete();
                }
                Files.copy(file.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
                url = imageUrl + fileName;

                logger.debug("库记录:" + ids);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ActionResult resutl = faceRetrievalService.getRetrievalDetail(url, ids, type);
        return resutl;
        // //type : stranger路人库  portrait人像库
        // String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'), file.getOriginalFilename().length());
        // String imagePath = imageFolder  + fileName;
        // String url = "";
        // try {
        //     if (originalUrl.trim().isEmpty()) {
        //         File localFile = new File(imagePath);
        //         if (localFile.exists()) {
        //             localFile.delete();
        //         }
        //         Files.copy(file.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
        //         url = imageUrl + fileName;
        //     } else {
        //         url = originalUrl;
        //     }
        //
        //     logger.debug("库记录:" + ids);
        //     ActionResult resutl = faceRetrievalService.getRetrievalDetail(url, ids, type);
        //     return resutl;
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        // return new ActionResult(ActionCodeConfig.getServerErrorCode(), ActionCodeConfig.getServerErrorMsg(), null, url);
    }
}
