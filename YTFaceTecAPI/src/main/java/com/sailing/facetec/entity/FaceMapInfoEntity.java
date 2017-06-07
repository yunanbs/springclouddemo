package com.sailing.facetec.entity;

import lombok.Data;

/**
 * Created by yunan on 2017/6/7.
 * 地图用人脸信息
 */
@Data
public class FaceMapInfoEntity {
    private String rlid; // 人像id
    private String jd; // 摄像头经度
    private String wd; // 摄像头纬度
    private String sxtmc; // 摄像头名称
    private String sxtid;
    private String ylzd1; // 抓拍照片路径
    private String ylzd2; // 人像照片路径

}
