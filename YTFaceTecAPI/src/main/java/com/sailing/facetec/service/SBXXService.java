package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.SBXXEntity;

/**
 * Created by GuoWang on 2017/6/1.
 */
public interface SBXXService {
    DataEntity<SBXXEntity> listSBXX(String yhz,String key);
}
