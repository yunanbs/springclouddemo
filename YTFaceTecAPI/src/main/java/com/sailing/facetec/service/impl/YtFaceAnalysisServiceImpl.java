package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.entity.YtImportResultDetailEntity;
import com.sailing.facetec.entity.YtImportResultEntity;
import com.sailing.facetec.entity.YtRetrievalResultEntity;
import com.sailing.facetec.service.YtFaceAnalysisService;
import com.sailing.facetec.util.FaceHttpUtils;
import com.sailing.facetec.util.ImageUtils;
import org.apache.coyote.ActionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by eagle on 2017-5-3.
 */
@Service
public class YtFaceAnalysisServiceImpl implements YtFaceAnalysisService {
    private final Logger logger = LoggerFactory.getLogger(YtFaceAnalysisServiceImpl.class);
    @Value("${ytface.api-url}")
    private String faceUrl;

    @Override
    public ActionResult getAnalysisDetail(String imgPath, String ids) {
        String imgBase64 = ImageUtils.picToBase64(imgPath);
        int sessionId = 0;
        // 登录
        JSONObject loginJson = new JSONObject();
        loginJson.put("name", "admin");
        loginJson.put("password", "21232f297a57a5a743894a0e4a801fc3");
        String loginString = FaceHttpUtils.postYituFaceMessage(faceUrl + "/resource_manager/user/login",
                JSON.toJSONString(loginJson),
                sessionId);
        JSONObject login = JSONObject.parseObject(loginString);
        int rtn = login.getIntValue("rtn");
        if (rtn == 0) {
            sessionId = login.getIntValue("session_id");
            logger.debug("session_id:{}", sessionId);
        } else {
            return new ActionResult(ActionCodeConfig.SERVER_ERROR_CODE, "登录分析服务器失败", null, null);
        }
        // 导入原始图片
        JSONObject optionsJson = new JSONObject();
        optionsJson.put("max_faces_allowed", -1);
        JSONObject importJson = new JSONObject();
        importJson.put("options", optionsJson);
        importJson.put("picture_image_content_base64", imgBase64.replaceAll("\r|\n", ""));
        String importResultString = FaceHttpUtils
                .postYituFaceMessage(faceUrl + "/face/v1/framework/face_image/repository/picture/synchronized",
                        JSON.toJSONString(importJson),
                        sessionId);
        YtImportResultEntity ytImportResultEntity = JSONObject.parseObject(importResultString, YtImportResultEntity.class);
        if (ytImportResultEntity == null || ytImportResultEntity.getRtn() != 0) {
            return new ActionResult(ActionCodeConfig.SERVER_ERROR_CODE, "检测原始图片人脸失败", null, null);
        }
        YtImportResultDetailEntity[] ytImportResultDetailEntities = ytImportResultEntity.getResults();
        if (ytImportResultDetailEntities.length == 0) {
            return new ActionResult(ActionCodeConfig.SERVER_ERROR_CODE, "未检测到原始图片人脸信息", null, null);
        }
        for (YtImportResultDetailEntity ytImportResultDetailEntity : ytImportResultDetailEntities) {
            // 获取分析结果
            JSONObject analysisFaceJson = new JSONObject();
            String[] fields = {"face_image_id", "repository_id",
                    "timestamp", "person_id", "name", "gender",
                    "nation", "camera_id", "is_hit", "born_year",
                    "similarity", "annotation", "picture_uri",
                    "face_image_uri", "face_image_url"};
            analysisFaceJson.put("fields", fields);
            JSONObject contitionJson = new JSONObject();
            analysisFaceJson.put("condition", contitionJson);
            JSONObject retrievalJson = new JSONObject();
            retrievalJson.put("threshold", 0);
            retrievalJson.put("face_image_id",
                    ytImportResultDetailEntity.getFace_image_id());
            int[] repositoryIds = { 67 };
            retrievalJson.put("repository_ids", repositoryIds);
            analysisFaceJson.put("retrieval", retrievalJson);
            JSONObject orderJson = new JSONObject();
            retrievalJson.put("similarity", -1);
            analysisFaceJson.put("order", orderJson);
            analysisFaceJson.put("start", 0);
            analysisFaceJson.put("limit", 20);
            String analysisResultString = FaceHttpUtils.postYituFaceMessage(
                    faceUrl + "/face/v1/framework/face/retrieval",
                    JSON.toJSONString(analysisFaceJson), sessionId);
            YtRetrievalResultEntity ytRetrievalResultEntity = JSONObject.parseObject(analysisResultString, YtRetrievalResultEntity.class);
            if (ytRetrievalResultEntity == null || ytRetrievalResultEntity.getRtn() != 0) {
                return new ActionResult(ActionCodeConfig.SERVER_ERROR_CODE, "检索失败", null, null);
            }
        }

        return null;
    }
}
