package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 摄像头详细数据
 * Created by yunan on 2017/4/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SxtDetailEntity implements Serializable{

    private static final long serialVersionID=1L;

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
}
