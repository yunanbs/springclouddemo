package com.sailing.facetec.entity;

import lombok.Data;

/**
 * Created by yunan on 2017/6/7.
 * 地图用人脸信息
 */
@Data
public class FaceMapInfoEntity {
    private String faceID; // 人像id
    private String longtitude; // 摄像头经度
    private String latitude; // 摄像头纬度
    private String cameraName; // 摄像头名称
    private String cameraID; // 摄像头编号
    private String pirUrl; // 抓拍照片路径
    private String faceUrl; // 人像照片路径

}
