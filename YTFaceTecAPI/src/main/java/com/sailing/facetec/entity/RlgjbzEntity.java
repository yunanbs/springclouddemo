package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by yunan on 2017/5/2.
 * 报警标注数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RlgjbzEntity {
    private static final long serialVersionUID = 1L;

    private Long XH;
    private String RLKRLID;
    private String LRKRLID;
    private String BZSFXT;
    private String BZBZ;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;
}
