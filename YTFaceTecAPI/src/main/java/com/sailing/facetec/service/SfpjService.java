package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * Created by yunan on 2017/5/8.
 * 算法评价Service
 */
public interface SfpjService {
    DataEntity insertSfpj(int pjFlag,String cxdm,String sfdm,double fz,String bz);
    DataEntity getSfAvg(int pjFlag, String sfdm);
}
