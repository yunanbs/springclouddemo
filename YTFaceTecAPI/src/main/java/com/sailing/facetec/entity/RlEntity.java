package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/24.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlEntity implements Serializable{
    private static final long serialVersionUID =1L;

    private int  XH;
    private String RLID;
    private String RLKID;
    private String RKSJ;
    private String SFZH;
    private int  XB;
    private String CSNF;
    private String XM;
    private String DTDZ;
    private String RLDZ;
    private String TJRBH;
    private String TJR;
    private String XGRBH;
    private String XGR;
    private String TJSJ;
    private String XGSJ;
    private String LGLX;
    private String LGYX;
    private int  SFTB;
    private String ZJLB;
    private String ZJHM;
    private String RLSF;
    private String RLCS;
    private String RLGJ;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;
    private String RLKID1;
    private String RLKID2;
    private String RLKID3;
    private String RLKID4;
    private String RLKID5;
    private String RLKID6;
    private String RLKID7;
    private String RLKID8;
    private String RLKID9;
    private String RLKID10;
    private String RLID1;
    private String RLID2;
    private String RLID3;
    private String RLID4;
    private String RLID5;
    private String RLID6;
    private String RLID7;
    private String RLID8;
    private String RLID9;
    private String RLID10;
    private String XZDTDZ;
    private String XZRLDZ;
    private String XZGXDTDZ;
    private String XZGXRLDZ;
    private String base64Pic;
}
