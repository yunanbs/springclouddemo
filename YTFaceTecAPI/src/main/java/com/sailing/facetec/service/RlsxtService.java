package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtdwEntity;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlsxtService {
    DataEntity<SxtDetailEntity> listAllXST();
    DataEntity<SxtdwEntity> listAllSXTDW();
}
