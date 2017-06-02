package com.sailing.facetec.entity;

import lombok.Data;

/**
 * Created by eagle on 2017-5-4.
 */
@Data
public class FaceRetrievalDetailEntity {
    private String name;
    private String idCard;
    private String nation;
    private int gender;
    private double similarity;
    private String date;
    private String imgUrl;
    private String bigImgUrl;
    private String imgPath;
    private String bigImgPath;
    private String repositoryId;
    private String repositoryName;

}
