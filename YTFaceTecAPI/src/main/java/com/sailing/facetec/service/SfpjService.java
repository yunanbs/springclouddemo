package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * Created by yunan on 2017/5/8.
 */
public interface SfpjService {
    int insertSfpj(int pjFlag,String cxdm,String sfdm,double fz,String bz);
    DataEntity getSfAvg(int pjFlag, String sfdm);
}