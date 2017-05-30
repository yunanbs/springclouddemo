package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by GuoWang on 2017/5/29.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlShowDetailEntity {
    private static final long serialVersionUID = 1L;

    private  Long RN;
    private  Long XH;
    private String RLKID;
    private String XM;
    private String XB;
    private String CSNF;
    private String SZQY;
    private String SFZH;
}
