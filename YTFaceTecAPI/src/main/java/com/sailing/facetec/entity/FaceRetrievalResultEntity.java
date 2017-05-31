package com.sailing.facetec.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by eagle on 2017-5-4.
 */
@Data
public class FaceRetrievalResultEntity {
    private String sfdm;
    private String avgScore;
    private String actionCode;
    private String actionMsg;
    private List<FaceRetrievalDetailEntity> faceRetrievalDetailEntityList;

    public FaceRetrievalResultEntity(String sfdm) {
        this.sfdm = sfdm;
    }

    public FaceRetrievalResultEntity(String sfdm, String avgScore) {
        this.sfdm = sfdm;
        this.avgScore = avgScore;
    }

    public FaceRetrievalResultEntity(String sfdm, String actionCode, String actionMsg, List<FaceRetrievalDetailEntity> faceRetrievalDetailEntityList) {
        this.sfdm = sfdm;
        this.actionCode = actionCode;
        this.actionMsg = actionMsg;
        this.faceRetrievalDetailEntityList = faceRetrievalDetailEntityList;
    }
}
