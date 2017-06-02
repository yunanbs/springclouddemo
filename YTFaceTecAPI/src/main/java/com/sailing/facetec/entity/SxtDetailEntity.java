package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/16.
 * 摄像头详细信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SxtDetailEntity implements Serializable{
    private static final long serialVersionUID=1L;

    private Long XH;
    private String SBBH;
    private String SXTID;
    private String SXTMC;
    private Integer SXTLX;
    private String SPDZ;
    private String LRKID;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;
    private String YHZ;
    private String SBMC;
}
