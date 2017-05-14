package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 算法均值数据
 * Created by yunan on 2017/5/8.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SfpjAvgEntity implements Serializable {
    private static final long serialVersionId = 1L;

    private String SFDM;
    private String FZ;
}
