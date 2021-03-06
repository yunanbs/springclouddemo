package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yunan on 2017/5/8.
 * 手动算法评价数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SdsfpjEntiry implements Serializable{
    private static final long serialVersionUID = 1L;

    private long XH;
    private Date RKSJ;
    private String CXDM;
    private String SFDM;
    private double FZ;
    private String BZ;
}
