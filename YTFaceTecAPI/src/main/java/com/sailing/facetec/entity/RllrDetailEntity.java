package com.sailing.facetec.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/4/28.
 * 抓拍人脸详细数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RllrDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long RN;
    private Long XH;
    private String RLID;
    private String SXTID;
    private String LRKID;
    private String RKSJ;
    private String SFZH;
    private String XB;
    private String CSNF;
    private String XM;
    private String DTDZ;
    private String RLDZ;
    private String RKKSSJ;
    private String RKJSSJ;
    private String SFMZ;
    private String YSDTID;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;
    private String LRRKSJ;
    private String RLTZ1;
    private String RLTZ2;
    private String RLTZ3;
    private String RLTZ4;
    private String RLTZ5;
    private String RLTZ6;
    private String RLTZ7;
    private String RLTZ8;
    private String RLTZ9;
    private String RLTZ10;

    private String YLZD7;
    private String YLZD6;
    private String YLZD8;
    private String YLZD9;
    private String YLZD10;
    private String RLWZ;
    private String RLTPKG;

    private String SXTMC;
}
