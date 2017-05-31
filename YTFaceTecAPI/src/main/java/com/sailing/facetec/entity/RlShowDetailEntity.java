package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**人脸查询信息
 * Created by GuoWang on 2017/5/29.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlShowDetailEntity {
    private static final long serialVersionUID = 1L;

    private  Long RN;
    private  Long XH;
    private String RLKID;
    private String RLID;
    private String XM;
    private String XB;
    private String CSNF;
    private String SZQY;
    private String SFZH;

    private String DTDZ;
    private String RLDZ;
    private String GXDTDZ;
    private String GXRLDZ;
    private String LGLX;
    private String ZJLB;
    private String MZ;
    private String QYBH;
}
