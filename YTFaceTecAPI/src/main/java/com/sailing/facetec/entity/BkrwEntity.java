package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/16.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BkrwEntity implements Serializable{
    private static final long serialVersionUID = 1L;

}
