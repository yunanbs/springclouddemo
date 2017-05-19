package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/16.
 * 摄像头信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SxtEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String	XH;	//序号
    private String	SBBH;	//	设备编号
    private String	SXTID;	//	人脸摄像头id
    private String	SXTMC;	//	人脸摄像头名称
    private String	SXTLX;	//	摄像头类型 （0 表示持续识别 摄像头, 1 表示一次性识别 离线视频）
    private String	SPDZ;	//	视频的地址
    private String	LRKID;	//	路人库的id
    private String	YLZD1;	//	是否启用
    private String	YLZD2;
    private String	YLZD3;
    private String	YLZD4;
    private String	YLZD5;

}
