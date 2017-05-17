package com.sailing.facetec.service;

/**
 * Created by yunan on 2017/5/17.
 */
public interface YTService {
    String login(String userName,String passWord);
    String addCamera(String sid,String cameraName,String url,int cameraType);
    String updateCamera(String sid,int cameraId,String cameraName,String cameraUrl,int enabled);
    String setMonitorRepository(String sid,int cameraId,int repositoryId,double limit,long stSec,long endSec);
}
