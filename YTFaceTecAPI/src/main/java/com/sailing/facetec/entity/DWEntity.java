package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by GuoWang on 2017/6/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DWEntity {
    private static final long serialVersionUID =1L;
    private long XH;
    private String DWBH;
    private String DWMC;
    private String DWDZ;
    private String LXDH;
    private String LXR;
    private String DWJB;
    private long LSGX;
    private String DWNBBM;
    private String DWMS;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
}
