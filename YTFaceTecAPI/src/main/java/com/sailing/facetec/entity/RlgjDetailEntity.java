package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/4/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlgjDetailEntity implements Serializable{
    private static final long serialVersionID = 1L;

    private Long RN;
    private Long XH;
    private String RKSJ;
    private String GJSJ;
    private String RLKID;
    private String RLKRLID;
    private String LRKID;
    private String LRKRLID;
    private String XSD;
    private String MZLX;
    private String SFXT;
    private String SFZSFXT;
    private String LRRLDZ;
    private String LRRKSJ;
    private String RLSCNF;
    private String RLXB;
    private String RLXM;
    private String RLSFZ;
    private String RLRLDZ;
    private String RLRKSJ;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;
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
    private String SXSD;
    private String LRKMC;
    private String RLKMC;
    private String BZXH;
    private String BZSFXT;
    private String BZBZ;
}
