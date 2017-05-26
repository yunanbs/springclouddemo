package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/26.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonIDEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String province;
    private String city;
    private String district;
    private String birthDay;
    private int age;
    private String gender;
}
