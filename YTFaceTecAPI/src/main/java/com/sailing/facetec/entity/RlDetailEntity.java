package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yunan on 2017/5/4.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlDetailEntity implements Serializable{
    private static final long serialVersionID = 1L;

    private long XH;
    private String RLID;
    private String RLKID;
    private Date RKSJ;
    private String SFZH;
    private int XB;
    private String CSNF;
    private String XM;
    private String DTDZ;
    private String RLDZ;
    private String TJRBH;
    private String TJR;
    private String XGRBH;
    private String XGR;
    private Date TJSJ;
    private Date XGSJ;
    private String LGLX;
    private String LGYX;
    private int SFTB;
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
    private String RLKMC;

}
