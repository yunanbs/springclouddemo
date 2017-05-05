package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.SXTDetailEntity;
import com.sailing.facetec.entity.SXTDWEntity;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlsxtService {
    DataEntity<SXTDetailEntity> listAllXST();
    DataEntity<SXTDWEntity> listAllSXTDW();
}
