package com.sailing.facetec.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/4/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RllrDetailEntity implements Serializable {

    private static final long serialVersionID = 1L;

    private Long RN;
    @JSONField(name = "XH")
    private Long XH;
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
    private String YLDZ1;
    private String YLDZ2;
    private String YLDZ3;
    private String YLDZ4;
    private String YLDZ5;
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
    private String SXTMC;
}
