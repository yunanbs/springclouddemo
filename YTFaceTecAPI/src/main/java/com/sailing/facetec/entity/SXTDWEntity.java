package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/5.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SXTDWEntity implements Serializable{

    private static final long serialVersionID =1L;

    private String DWBH;
    private String DWMC;
    private String DWNBBM;
    private int level;
    private String parentID;
}
