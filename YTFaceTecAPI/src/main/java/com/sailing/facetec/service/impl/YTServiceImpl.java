package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yunan on 2017/5/17.
 */
@Service
public class YTServiceImpl  implements YTService{

    @Autowired
    private YTApi ytApi;

    @Override
    public String login(String userName, String passWord) {
        userName = "admin";
        passWord = "21232f297a57a5a743894a0e4a801fc3";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",userName);
        jsonObject.put("password",passWord);
        return ytApi.login(jsonObject.toJSONString());
    }

    @Override
    public String addCamera(String sid,String cameraName, String url, int cameraType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",cameraName);
        jsonObject.put("url",url);
        jsonObject.put("type",cameraType);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        return ytApi.addCamera(headerStr, jsonObject.toJSONString());
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
        return ytApi.updateCamera(headerStr, jsonObject.toJSONString());
    }

    @Override
    public String setMonitorRepository(String sid,int cameraId, int repositoryId, double limit, int stSec, int endSec) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("camera_id",cameraId);
        jsonObject.put("repository_id",repositoryId);
        jsonObject.put("threshold",limit);
        jsonObject.put("max_time_span",stSec);
        jsonObject.put("min_time_span",endSec);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        return ytApi.setMonitorRepository(headerStr, jsonObject.toJSONString());
    }
}
