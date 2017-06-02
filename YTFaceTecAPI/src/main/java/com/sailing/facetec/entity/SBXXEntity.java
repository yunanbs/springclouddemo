package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by GuoWang on 2017/6/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SBXXEntity {
    private static final long serialVersionUID = 1L;
    private long XH;
    private String SBBH;
    private String SBMC;
    private String SBLX;
    private String SBDL;
    private String SBMS;
    private String BZWZDM;
    private Double JD;
    private Double WD;
    private String XWJXH;
    private String SBXINGH;
    private String AZRQ;
    private long BXZQ;
    private long BYZQ;
    private String ZJBYRQ;
    private String JDHGRQ;
    private String CSMC;
    private String LXFS;
    private String SBTXFS;
    private String RJBB;
    private String IPDZ;
    private int TCXH;
    private String DYCVSBMC;
    private String SBSMZT;
    private String YHZ;
    private String DFSBDM;
    private String GLFWBH;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String SJQX;
    private String TXFWBH;
    private String CCFWBH;
    private String YBH;
    private String LJFS;
    private int BZLX;
}
