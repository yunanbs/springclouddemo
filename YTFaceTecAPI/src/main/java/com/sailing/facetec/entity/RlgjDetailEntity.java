package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 报警信息详细数据
 * Created by yunan on 2017/4/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlgjDetailEntity implements Serializable{
    private static final long serialVersionUID = 1L;

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
    private String LRTZ1;
    private String LRTZ2;
    private String LRTZ3;
    private String LRTZ4;
    private String LRTZ5;
    private String LRTZ6;
    private String LRTZ7;
    private String LRTZ8;
    private String LRTZ9;
    private String LRTZ10;

    private String RLXZDT;
    private String RLXZXT;
    private String LRXZDT;
    private String LRXZXT;

    private String GXRLXZDT;
    private String GXRLXZXT;
    private String GXLRXZDT;
    private String GXLRXZXT;

    private String LRWZ;
    private String LRTPKG;

    private String SXSD;
    private String LRKMC;
    private String RLKMC;
    private String BZXH;
    private String BZSFXT;
    private String BZBZ;
}
