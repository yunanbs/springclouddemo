package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.SfpjMapper;
import com.sailing.facetec.entity.SfpjAvgEntity;
import com.sailing.facetec.service.SfpjService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yunan on 2017/5/8.
 */
@Service
@Transactional
public class SfpjServiceImpl implements SfpjService {

    @Autowired
    private SfpjMapper sfpjMapper;

    private String tableName;
    private String seqName;

    @Override
    public int insertSfpj(int pjFlag, String cxdm, String sfdm, double fz, String bz) {
        tableName = pjFlag==1?"B_TZ_RLZDSFPJ":"B_TZ_RLSDSFPJ";
        seqName = pjFlag==1?"B_TZ_RLZDSFPJ_XH":"B_TZ_RLSDSFPJ_XH";
        return sfpjMapper.insertPJ(tableName,seqName,cxdm,sfdm,fz,bz);
    }

    @Override
    public DataEntity getSfAvg(int pjFlag, String sfdm) {
        tableName = pjFlag==1?"B_TZ_RLZDSFPJ":"B_TZ_RLSDSFPJ";
        StringBuilder customerFilter = new StringBuilder();
        if(!CommUtils.isNullObject(sfdm)){
            sfdm = String.join("','",sfdm.split(","));
            customerFilter.append(String.format(" where sfdm in ('%s')",sfdm));
        }
        DataEntity<SfpjAvgEntity> sfpjAvgEntityDataEntity = new DataEntity<>();
        sfpjAvgEntityDataEntity.setDataContent(sfpjMapper.getAvg(tableName,customerFilter.toString()));
        return sfpjAvgEntityDataEntity;
    }
}
