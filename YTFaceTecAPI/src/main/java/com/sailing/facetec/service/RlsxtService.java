package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;

import java.text.ParseException;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlsxtService {
    DataEntity<SxtDetailEntity> listAllXST();
    DataEntity<SxtdwEntity> listAllSXTDW();
    int addSXT(SxtEntity sxtEntity);
    int addMonitorReposity(BkrwEntity bkrwEntity) throws ParseException;
}
