package com.sailing.facetec.service;

import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;

/**
 * Created by eagle on 2017-5-3.
 */
public interface YtFaceAnalysisService {
    ActionResult getAnalysisDetail(String imgPath, String ids);
}
