package com.sailing.facetec.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.service.YtFaceAnalysisService;
import com.sailing.facetec.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by eagle on 2017-5-3.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
public class ImageAnalysisController {
    private final Logger logger = LoggerFactory.getLogger(ImageAnalysisController.class);

    @Autowired
    private YtFaceAnalysisService ytFaceAnalysisService;

    @Value("${upload.image-folder}")
    private String imageFolder;

    @RequestMapping(value = "/Analysis/Compare", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ActionResult listRealDetails(@RequestParam("image") MultipartFile file, @RequestParam(name = "ids", defaultValue = "") String ids) {
        try {
            Files.copy(file.getInputStream(), Paths.get(imageFolder, file.getOriginalFilename()));
            JSONArray jsonArray = JSONArray.parseArray(ids);

            ytFaceAnalysisService.getAnalysisDetail(imageFolder + file.getOriginalFilename(), "");
            //logger.info("上传图片Base64编码:" + ImageUtils.picToBase64(imageFolder + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
