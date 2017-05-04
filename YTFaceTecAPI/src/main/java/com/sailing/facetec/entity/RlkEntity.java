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
public class RlkEntity implements Serializable {

    private static final long serialVersionID = 1L;

    private long XH;
    private String RLKID;
    private String RLKMC;
    private int RLKLX;
    private int CCLX;
    private Date TJSJ;
    private Date XGSJ;
    private String TJRBH;
    private String TJR;
    private String XGRBH;
    private String XGR;
    private int WCBL;
    private int ZT;
    private int RLSL;
    private int TPSL;
    private int JZLX;
    private String BZ;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;

}
