package com.sailing.facetec.service;

import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.FaceRetrievalResultEntity;

/**
 * Created by eagle on 2017-5-3.
 */
public interface FaceRetrievalService {
    ActionResult getRetrievalDetail(String imgPath, String ids,String type);
}
