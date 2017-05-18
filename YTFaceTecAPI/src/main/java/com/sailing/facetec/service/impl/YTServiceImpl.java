package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yunan on 2017/5/17.
 */
@Service
public class YTServiceImpl  implements YTService{

    private static final Logger logger = LoggerFactory.getLogger(YTServiceImpl.class);

    @Autowired
    private YTApi ytApi;

    @Override
    public String login(String userName, String passWord) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",userName);
        jsonObject.put("password",passWord);
        String result = ytApi.login(jsonObject.toJSONString());
        logger.info("yt login result:{}", result);
        return result;
    }

    @Override
    public String addCamera(String sid,String cameraName, String url, int cameraType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",cameraName);
        jsonObject.put("url",url);
        jsonObject.put("type",cameraType);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        String result = ytApi.addCamera(headerStr, jsonObject.toJSONString());
        logger.info("yt addCamera result:{}", result);
        return result;
    }

    @Override
    public String updateCamera(String sid, int cameraId, String cameraName, String cameraUrl, int enabled) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",cameraId);
        if(!CommUtils.isNullObject(cameraName)){
            jsonObject.put("name",cameraName);
        }
        if(!CommUtils.isNullObject(cameraUrl)){
            jsonObject.put("url",cameraUrl);
        }
        if(!CommUtils.isNullObject(enabled)){
            jsonObject.put("enabled",enabled);
        }
        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);

        String result = ytApi.updateCamera(headerStr, jsonObject.toJSONString());
        logger.info("yt updateCamera result:{}", result);
        return result;
    }

    @Override
    public String setMonitorRepository(String sid,int cameraId, int repositoryId, double limit, long stSec, long endSec) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("camera_id",cameraId);
        jsonObject.put("repository_id",repositoryId);
        jsonObject.put("threshold",limit);
        jsonObject.put("max_time_span",stSec);
        jsonObject.put("min_time_span",endSec);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        return ytApi.setMonitorRepository(headerStr, jsonObject.toJSONString());
    }


    @Override
    public String setMonitorByCamera(String sid,String monitorName, int cameraId, int repositoryId, double limit) {

        String headerStr = String.format("session_id=%s@DEFAULT; yt_cluster_id=DEFAULT",sid);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",monitorName);

        JSONArray jsonArray = new JSONArray();
        JSONObject monitorObject = new JSONObject();
        monitorObject.put("camera_id",String.format("%s@DEFAULT",cameraId));
        monitorObject.put("repository_id",String.format("%s@DEFAULT",repositoryId));
        monitorObject.put("threshold",limit);
        jsonArray.add(monitorObject);

        jsonObject.put("requests",jsonArray);
        String result = ytApi.setMonitorByCamera(headerStr, jsonObject.toJSONString());
        logger.info("yt setMonitorByCamera result:{}", result);
        return result;
    }
}
