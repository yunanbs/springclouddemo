package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlgjDetailEntity;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlgjService {
    DataEntity<RlgjDetailEntity> listRlgjDetail(String beginTime, String endTime, String orderBy, int page, int size,Double XSD, String BZ,String RLID,String lrkids,String sex,String age,String glass,String fringe,String uygur);

    int updateRlgjBZ(Long xh,String bzsfxt,String bzbz);


}
