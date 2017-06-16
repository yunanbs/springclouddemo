package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.comm.PageEntity;
import com.sailing.facetec.dao.RllrDetailMapper;
import com.sailing.facetec.entity.FaceMapInfoEntity;
import com.sailing.facetec.entity.RllrDetailEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
public class RllrServiceImpl implements RllrService {

    @Value("${redis-keys.capture-data}")
    private String captureData;

    @Autowired
    private RllrDetailMapper rllrDetailMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private YTService ytService;


    @Override
    public DataEntity<RllrDetailEntity> listRllrDetail(String beginTime, String endTime, String orderBy, int page, int size, String lrkids, String sex, String age, String glass, String fringe, String uygur) {

        DataEntity<RllrDetailEntity> result = new DataEntity<>();

        // 设置检索开始时间
        beginTime = CommUtils.isNullObject(beginTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 00:00:00") : beginTime;
        // 设置检索结束时间
        endTime = CommUtils.isNullObject(endTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 23:59:59") : endTime;
        // 设置排序字段
        orderBy = CommUtils.isNullObject(orderBy) ? "a.XH desc" : orderBy;

        // 创建自定义过滤条件
        StringBuilder customerFilterBuilder = new StringBuilder();
        // 添加摄像头过滤
        customerFilterBuilder.append(CommUtils.isNullObject(lrkids) ? "" : String.format(" and a.LRKID in (%s) ", lrkids));
        // 添加性别过滤
        customerFilterBuilder.append(CommUtils.isNullObject(sex) ? "" : String.format(" and a.RLTZ1 in (%s) ", sex));
        // 添加年龄过滤
        customerFilterBuilder.append(CommUtils.isNullObject(age) ? "" : String.format(" and a.RLTZ2 in (%s) ", age));
        // 添加眼镜过滤
        customerFilterBuilder.append(CommUtils.isNullObject(glass) ? "" : String.format(" and a.RLTZ3 in (%s) ", glass));
        // 添加刘海过滤
        customerFilterBuilder.append(CommUtils.isNullObject(fringe) ? "" : String.format(" and a.RLTZ4 in (%s) ", fringe));
        // 添加种族过滤
        customerFilterBuilder.append(CommUtils.isNullObject(uygur) ? "" : String.format(" and a.RLTZ5 in (%s) ", uygur));

        // 计算分页
        int[] pages = CommUtils.countPage(page, size);

        int counts = rllrDetailMapper.countRllrDetails(beginTime, endTime, customerFilterBuilder.toString());
        List<RllrDetailEntity> datas = rllrDetailMapper.listRllrDetails(beginTime, endTime, orderBy, pages[0], pages[1], customerFilterBuilder.toString());

        PageEntity pageEntity = new PageEntity(counts, page, size);

        result.setDataContent(datas);
        result.setPageContent(pageEntity);

        return result;
    }

    @Override
    public String listRllrDetailReal(String lrkids) {
        JSONObject result = JSONObject.parseObject(redisService.getVal(captureData));
        JSONArray dataArray = result.getJSONArray("dataContent");
        // 如果传递了lrkid 则根据id进行过滤
        if (!CommUtils.isNullObject(lrkids)) {
            dataArray.removeIf(s -> !lrkids.contains(((JSONObject) s).getString("lrkid")));
        }
        return result.toString();
    }

    @Override
    public JSONArray listFaceQueryInfo(String picStr, String[] repositorys, double threshold, String beginTime, String endTime) {

        String faceid = uploadFace(picStr);

        // 定义返回的属性
        String fields = "face_image_id,repository_id,timestamp,camera_id,timestamp_end,is_hit,rec_gender,similarity";
        // 请求检索

        // 设定时间条件
        JSONObject timeCondition = new JSONObject();
        timeCondition.put("$gte", CommUtils.stringToDate(beginTime).getTime() / 1000);
        timeCondition.put("$lte", CommUtils.stringToDate(endTime).getTime() / 1000);

        JSONObject conditionObj = new JSONObject();
        conditionObj.put("timestamp", timeCondition);

        String sid = ytService.login();

        // 获取检索结果
        String queryResult = ytService.queryFacesByID(sid, Long.parseLong(faceid), repositorys, threshold, fields.split(","), conditionObj, null, 0, 1000);
        JSONArray faceArray = JSONObject.parseObject(queryResult).getJSONArray("results");
        if(0<faceArray.size()){
            getFaceMapInfo(faceArray);
        }
        return faceArray;
    }

    /**
     * 上传需要检索的人像
     *
     * @param picStr
     * @return
     */
    private String uploadFace(String picStr) {
        // 上传检索图片
        String result = ytService.uploadFaceToQuery(picStr);
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 获取上传人像的id
        String faceid = jsonObject.getJSONArray("results").getJSONObject(0).getString("face_image_id");
        return faceid;
    }

    /**
     * 人像相关业务数据
     *
     * @param faces
     * @return
     */
    private void getFaceMapInfo(JSONArray faces) {
        // 获取检索到的人像编号
        StringBuilder faceIDs = new StringBuilder();
        faces.forEach(face -> {
            faceIDs.append(String.format("%s,", ((JSONObject) face).getString("face_image_id")));
        });
        faceIDs.deleteCharAt(faceIDs.length() - 1);
        List<FaceMapInfoEntity> faceMapInfoEntities = rllrDetailMapper.listFaceMapInfoByFaceIDs(faceIDs.toString());
        for (Object face : faces) {
            JSONObject faceObj = (JSONObject) face;
            for (FaceMapInfoEntity faceMapInfoEntity : faceMapInfoEntities) {
                if (faceMapInfoEntity.getFaceID().equals(faceObj.getString("face_image_id"))) {
                    ((JSONObject) face).put("picurl", faceMapInfoEntity.getPicUrl());
                    ((JSONObject) face).put("faceurl", faceMapInfoEntity.getFaceUrl());
                    ((JSONObject) face).put("longitude", faceMapInfoEntity.getLongitude());
                    ((JSONObject) face).put("latitude", faceMapInfoEntity.getLatitude());
                    ((JSONObject) face).put("cameraname", faceMapInfoEntity.getCameraName());
                    ((JSONObject) face).put("cameraid", faceMapInfoEntity.getCameraID());
                    switch (faceObj.getString("rec_gender")) {
                        case "1":
                            ((JSONObject) face).put("rec_gender", "男");
                            break;
                        case "2":
                            ((JSONObject) face).put("rec_gender", "女");
                            break;
                        default:
                            break;
                    }
                    double similarity = ((JSONObject) face).getDouble("similarity");

                    ((JSONObject) face).put("similarity",String.format("%.2f",similarity));
                    continue;
                }
            }
        }
    }

    /**
     * 地图多人脸检索
     *
     * @param picStrs     人脸图片base64字符串
     * @param repositorys 检索人脸库
     * @param threshold   最低相似度
     * @param beginTime   检索开始时间
     * @param endTime     检索结束时间
     * @param mergeFlag   多结果集合并标记位 1 交集 2并集
     * @param offset      并集情况下的时间偏移量
     * @return
     */
    @Override
    public JSONObject mapMutilFaceQuery(String[] picStrs, String[] repositorys, double threshold, String beginTime, String endTime, int mergeFlag, long offset) {

        JSONObject result = new JSONObject();
        List<JSONArray> faceResults = new ArrayList<>();
        JSONArray src = null;
        JSONArray des = null;

        for (String pic : picStrs) {
            faceResults.add(listFaceQueryInfo(pic, repositorys, threshold, beginTime, endTime));
        }

        for (JSONArray faceResult : faceResults) {
            if (CommUtils.isNullObject(src)) {
                src = faceResult;
                continue;
            }

            des = faceResult;

            if (CommUtils.isNullObject(src) || CommUtils.isNullObject(des)) {
                return null;
            }

            src = mergeList(src, des, mergeFlag, offset);
        }

        result.put("faces",src);
        result.put("cameras",getCameraInfoByFaces(src));

        return result;
    }

    private JSONArray mergeList(JSONArray src, JSONArray des, int mergeFlag, long offset) {
        JSONArray result = new JSONArray();
        if (1 == mergeFlag) {
            result = joinArray(src, des, "face_image_id_str");
        } else {
            result = uninArray(src, des, "timestamp_end", offset);
        }
        return result;
    }

    /**
     * 使用key进行jsonobject的比较 取交集
     *
     * @param src
     * @param des
     * @param key
     * @return
     */
    private JSONArray joinArray(JSONArray src, JSONArray des, String key) {
        JSONArray result = new JSONArray();
        for (Object srcObj : src) {
            JSONObject srcTmp = (JSONObject) srcObj;
            String srcFaceID = srcTmp.getString(key);
            for (Object desObj : des) {
                JSONObject desTmp = (JSONObject) desObj;
                String desFaceID = desTmp.getString(key);
                if (srcFaceID.equals(desFaceID)) {
                    result.add(srcTmp);
                    continue;
                }
            }
        }
        return result;
    }

    /**
     * 使用key 和 偏移量进行jsonobject比较 取并集
     *
     * @param src
     * @param des
     * @param key
     * @param offset
     * @return
     */
    private JSONArray uninArray(JSONArray src, JSONArray des, String key, long offset) {
        JSONArray result = new JSONArray();
        for (Object srcObj : src) {
            JSONObject srcTmp = (JSONObject) srcObj;
            long srcTimestamp = srcTmp.getLong(key);
            for (Object desObj : des) {
                JSONObject desTmp = (JSONObject) desObj;
                long desTimestamp = desTmp.getLong(key);
                if (Math.abs(srcTimestamp - desTimestamp) < offset) {
                    JSONObject subResult = (JSONObject) srcTmp.clone();
                    subResult.put("unin_face_image_id_str", desTmp.getString("face_image_id_str"));
                    subResult.put("unin_rec_gender", desTmp.getString("rec_gender"));
                    subResult.put("unin_picurl", desTmp.getString("picurl"));
                    subResult.put("unin_faceurl", desTmp.getString("faceurl"));
                    subResult.put("unin_similarity", desTmp.getString("similarity"));
                    subResult.put("unin_timestamp_end", desTmp.getString("timestamp_end"));
                    subResult.put("unit_is_hit", desTmp.getString("is_hit"));
                    subResult.put("unin_offset", srcTimestamp - desTimestamp);
                    result.add(subResult);
                    //continue;
                }
            }
        }
        return result;
    }

    /**
     * 提取人脸记录中的摄像头信息
     * @param faces
     * @return
     */
    private JSONArray getCameraInfoByFaces (JSONArray faces){
        JSONArray result = new JSONArray();
        List<String> findCameras=new ArrayList<>();
        for(Object face:faces){
            JSONObject faceObj = (JSONObject) face;
            String cameraID = faceObj.getString("cameraid");
            if(findCameras.contains(cameraID)){
                continue;
            }
            JSONObject cameraObj = new JSONObject();
            cameraObj.put("cameraid",faceObj.getString("cameraid"));
            cameraObj.put("cameraname",faceObj.getString("cameraname"));
            cameraObj.put("longitude",faceObj.getString("longitude"));
            cameraObj.put("latitude",faceObj.getString("latitude"));
            cameraObj.put("repository_id",faceObj.getString("repository_id"));
            result.add(cameraObj);
            findCameras.add(faceObj.getString("cameraid"));
        }
        return  result;
    }
}
