package com.sailing.facetec.service;

import com.sailing.facetec.entity.RlEntity;

import java.io.IOException;

/**
 * Created by yunan on 2017/5/17.
 */
public interface YTService {
    /**
     * 登录依图平台
     * @param userName
     * @param passWord
     * @return
     */
    String login(String userName,String passWord);

    /**
     * 添加摄像头
     * @param sid
     * @param cameraName
     * @param url
     * @param cameraType
     * @return
     */
    String addCamera(String sid,String cameraName,String url,int cameraType);

    /**
     * 更新摄像头信息
     * @param sid
     * @param cameraId
     * @param cameraName
     * @param cameraUrl
     * @param enabled
     * @return
     */
    String updateCamera(String sid,int cameraId,String cameraName,String cameraUrl,int enabled);

    /**
     * 添加布控任务
     * @param sid
     * @param cameraId
     * @param repositoryId
     * @param limit
     * @param stSec
     * @param endSec
     * @return
     */
    String setMonitorRepository(String sid,int cameraId,int repositoryId,double limit,long stSec,long endSec);

    /**
     * 布控摄像头
     * @param sid
     * @param cameraId
     * @param repositoryId
     * @param limit
     * @return
     */
    String setMonitorByCamera(String sid,String monitorName,int cameraId,int repositoryId,double limit);

    /**
     * 删除摄像头
     * @param sid
     * @param cameraID
     * @return
     */
    String delCamera(String sid,String cameraID);

    /**
     * 添加人像库记录
     * @param sid
     * @param rlEntity
     * @return
     */
    String uploadPicToReopsitory(String sid, RlEntity rlEntity);

    /**
     * 下载图片
     * @param picUris
     * @param localFile
     * @return
     * @throws IOException
     */
    boolean downLoadPic(String picUris,String localFile) throws IOException;
}
