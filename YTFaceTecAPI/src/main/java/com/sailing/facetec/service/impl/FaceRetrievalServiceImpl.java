package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.*;
import com.sailing.facetec.entity.YtImportResultDetailEntity;
import com.sailing.facetec.entity.YtImportResultEntity;
import com.sailing.facetec.entity.YtRetrievalResultEntity;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.service.FaceRetrievalService;
import com.sailing.facetec.util.FaceHttpUtils;
import com.sailing.facetec.util.ImageUtils;
import com.sun.javafx.scene.layout.region.Margins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eagle on 2017-5-3.
 */
@Service
public class FaceRetrievalServiceImpl implements FaceRetrievalService {
    private final Logger logger = LoggerFactory.getLogger(FaceRetrievalServiceImpl.class);

    @Value("${ytface.api-url}")
    private String faceUrl;
    @Value("${rlapi.api-url}")
    private String rlUrl;
    @Value("${ytface.username}")
    private String ytusername;
    @Value("${ytface.password}")
    private String ytpassword;

    @Override
    public ActionResult getRetrievalDetail(String imgPath, String ids,String type) {
        Map<Integer, String> ytMap = new HashMap<>();
        JSONArray jsonArray = JSONArray.parseArray(ids);
        if (jsonArray == null) {
            return new ActionResult(ActionCodeConfig.SUCCEED_CODE, "未选择人像库", null, null);
        }
        if (type.equals("portrait")) {//人像库
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ytMap.put(Integer.parseInt(object.getString("rlkid")), object.getString("rlkmc"));
            }
        }else {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ytMap.put(Integer.parseInt(object.getString("lrkid")), object.getString("sxtmc"));
            }
        }
        if (ytMap.size() == 0) {
            return new ActionResult(ActionCodeConfig.SUCCEED_CODE, "未选择人像库", null, null);
        }

        FaceRetrievalResultEntity ytResult = getYtRetrievalDetail(imgPath, ytMap,type);
        if (type.equals("portrait")) {//人像库
            List<FaceRetrievalResultEntity> entityList = new ArrayList<>();
            DataEntity<FaceRetrievalResultEntity> dataEntity = new DataEntity<>();
            entityList.add(ytResult);
            dataEntity.setDataContent(entityList);
            return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, dataEntity, imgPath);
        }else {
            DataEntity<FaceRetrievalDetailEntity> dataEntity = new DataEntity<>();
            dataEntity.setDataContent(ytResult.getFaceRetrievalDetailEntityList());
            return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, dataEntity, imgPath);
        }
        //logger.info("上传图片Base64编码:" + ImageUtils.picToBase64(imageFolder + file.getOriginalFilename()));
    }

    public FaceRetrievalResultEntity getYtRetrievalDetail(String imgPath, Map<Integer, String> idsMap,String type) {
        String imgBase64 = ImageUtils.getURLImage(imgPath);
        int sessionId = 0;
        // 登录
        JSONObject loginJson = new JSONObject();
        loginJson.put("name", ytusername);
        loginJson.put("password", ytpassword);
        String loginString = FaceHttpUtils.postYituFaceMessage(faceUrl + "/resource_manager/user/login",
                JSON.toJSONString(loginJson),
                sessionId);
        JSONObject login = JSONObject.parseObject(loginString);
        int rtn = login.getIntValue("rtn");
        if (rtn == 0) {
            sessionId = login.getIntValue("session_id");
            logger.debug("session_id:{}", sessionId);
        } else {
            return new FaceRetrievalResultEntity("yt", ActionCodeConfig.SERVER_ERROR_CODE, "登录分析服务器失败", null);
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
            return new FaceRetrievalResultEntity("yt", ActionCodeConfig.SERVER_ERROR_CODE, "检测原始图片人脸失败", null);
        }
        YtImportResultDetailEntity[] ytImportResultDetailEntities = ytImportResultEntity.getResults();
        if (ytImportResultDetailEntities.length == 0) {
            return new FaceRetrievalResultEntity("yt", ActionCodeConfig.SERVER_ERROR_CODE, "未检测到原始图片人脸信息", null);
        }
        List<YtRetrievalResultDetailEntity> ytRetrievalResultDetailEntityList = new ArrayList<>();
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

            Integer[] repositoryIds = {};
            repositoryIds = idsMap.keySet().toArray(repositoryIds);

            retrievalJson.put("repository_ids", repositoryIds);
            analysisFaceJson.put("retrieval", retrievalJson);
            JSONObject orderJson = new JSONObject();
            orderJson.put("similarity", -1);
            analysisFaceJson.put("order", orderJson);
            analysisFaceJson.put("start", 0);
            analysisFaceJson.put("limit", 20);
            String analysisResultString = FaceHttpUtils.postYituFaceMessage(
                    faceUrl + "/face/v1/framework/face/retrieval",
                    JSON.toJSONString(analysisFaceJson), sessionId);
            YtRetrievalResultEntity ytRetrievalResultEntity = JSONObject.parseObject(analysisResultString, YtRetrievalResultEntity.class);
            if (ytRetrievalResultEntity == null || ytRetrievalResultEntity.getRtn() != 0) {
                return new FaceRetrievalResultEntity("yt", ActionCodeConfig.SERVER_ERROR_CODE, "检索失败", null);
            }
            for (YtRetrievalResultDetailEntity ytRetrievalResultDetailEntity : ytRetrievalResultEntity.getResults()) {
                ytRetrievalResultDetailEntityList.add(ytRetrievalResultDetailEntity);
            }
        }

        //获取评分
        String avg = FaceHttpUtils.getRlApiMessage(
                rlUrl + "/faceapi/SF/SFPJ/Avg",
                "sfdm=yt");
        String value = "0";
        JSONObject avgObject = JSONObject.parseObject(avg).getJSONObject("content");
        if (JSONObject.parseObject(avg).getString("responseCode").equals(ActionCodeConfig.SUCCEED_CODE)) {
            JSONArray dataContent = null;
            if (avgObject != null) {
                dataContent = avgObject.getJSONArray("dataContent");
                value = dataContent.size()>0?dataContent.getJSONObject(0).getString("fz"):"0";

            }
        }
        FaceRetrievalResultEntity retrievalResultEntity = new FaceRetrievalResultEntity("yt", value);

        JSONArray jsonArray = new JSONArray();
        if (ytRetrievalResultDetailEntityList != null && ytRetrievalResultDetailEntityList.size() > 0) {
            retrievalResultEntity.setActionCode(ActionCodeConfig.SUCCEED_CODE);
            retrievalResultEntity.setActionMsg(ActionCodeConfig.SUCCEED_MSG);

            List<FaceRetrievalDetailEntity> retrievalDetailEntityList = new ArrayList<>();

            for (YtRetrievalResultDetailEntity ytRetrievalResultDetailEntity : ytRetrievalResultDetailEntityList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sfdm", "yt");
                jsonObject.put("rlkid", String.valueOf(ytRetrievalResultDetailEntity.getRepository_id()));
                jsonObject.put("rlid", ytRetrievalResultDetailEntity.getFace_image_id_str());
                if (type.equals("stranger")) {//l路人库
                    jsonObject.put("lrid", ytRetrievalResultDetailEntity.getFace_image_id_str());
                }
                jsonArray.add(jsonObject);
            }
            String imgUrlResult = FaceHttpUtils.postRlApiMessage(
                    rlUrl + "/faceapi/RL",
                    JSON.toJSONString(jsonArray));
            JSONObject jsonObject = JSONObject.parseObject(imgUrlResult).getJSONObject("content");
            JSONArray dataContent = null;
            if (jsonObject != null) {
                dataContent = jsonObject.getJSONArray("dataContent");
            }
            for (YtRetrievalResultDetailEntity ytRetrievalResultDetailEntity : ytRetrievalResultDetailEntityList) {
                FaceRetrievalDetailEntity retrievalDetailEntity = new FaceRetrievalDetailEntity();
                if (dataContent != null) {
                    for (int i = 0; i < dataContent.size(); i++) {
                        // if (dataContent.getJSONObject(i).get("rlid").toString().equals(ytRetrievalResultDetailEntity.getFace_image_id_str())) {
                        //     if (type.equals("stranger")) {
                        //         //l路人库
                        //         retrievalDetailEntity.setImgUrl(dataContent.getJSONObject(i).get("ylzd2").toString());
                        //         retrievalDetailEntity.setBigImgUrl(dataContent.getJSONObject(i).get("ylzd1").toString());
                        //     }else {
                        //         ytRetrievalResultDetailEntity.setFace_image_url(dataContent.getJSONObject(i).get("xzgxdtdz").toString());
                        //         retrievalDetailEntity.setImgUrl(dataContent.getJSONObject(i).get("xzgxdtdz").toString());
                        //     }
                        // }

                        // 2017-06-01 替换
                        if (dataContent.getJSONObject(i).get("rlid").toString().equals(ytRetrievalResultDetailEntity.getFace_image_id_str())) {
                            if (type.equals("stranger")) {
                                //l路人库
                                retrievalDetailEntity.setImgUrl(dataContent.getJSONObject(i).get("ylzd2").toString());
                                retrievalDetailEntity.setBigImgUrl(dataContent.getJSONObject(i).get("ylzd1").toString());
                                retrievalDetailEntity.setImgPath(dataContent.getJSONObject(i).get("ylzd6").toString());
                                retrievalDetailEntity.setBigImgPath(dataContent.getJSONObject(i).get("ylzd5").toString());
                            }else {
                                ytRetrievalResultDetailEntity.setFace_image_url(dataContent.getJSONObject(i).get("xzgxdtdz").toString());
                                retrievalDetailEntity.setImgUrl(dataContent.getJSONObject(i).get("xzrldz").toString());
                                retrievalDetailEntity.setBigImgUrl(dataContent.getJSONObject(i).get("xzdtdz").toString());
                                retrievalDetailEntity.setImgPath(dataContent.getJSONObject(i).get("xzgxrldz").toString());
                                retrievalDetailEntity.setBigImgPath(dataContent.getJSONObject(i).get("xzgxdtdz").toString());
                            }
                        }

                    }
                }
                retrievalDetailEntity.setIdCard(ytRetrievalResultDetailEntity.getPerson_id());
                retrievalDetailEntity.setName(ytRetrievalResultDetailEntity.getName());
                retrievalDetailEntity.setGender(ytRetrievalResultDetailEntity.getGender());
                retrievalDetailEntity.setRepositoryName(idsMap.get(ytRetrievalResultDetailEntity.getRepository_id()));
                DecimalFormat df   = new DecimalFormat("######0.00");
                retrievalDetailEntity.setSimilarity(Double.parseDouble(df.format(ytRetrievalResultDetailEntity.getSimilarity())));
                retrievalDetailEntityList.add(retrievalDetailEntity);
            }
            retrievalResultEntity.setFaceRetrievalDetailEntityList(retrievalDetailEntityList);
        } else {
            retrievalResultEntity.setActionCode(ActionCodeConfig.SUCCEED_CODE);
            retrievalResultEntity.setActionMsg("未分析到相似人员");
        }
        return retrievalResultEntity;
    }
}
