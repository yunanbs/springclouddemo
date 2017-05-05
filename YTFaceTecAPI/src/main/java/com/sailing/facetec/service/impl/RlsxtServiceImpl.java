package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlsxtMapper;
import com.sailing.facetec.entity.SXTDetailEntity;
import com.sailing.facetec.entity.SXTDWEntity;
import com.sailing.facetec.service.RlsxtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
public class RlsxtServiceImpl implements RlsxtService {

    @Autowired
    private RlsxtMapper rlsxtMapper;

    @Override
    public DataEntity<SXTDetailEntity> listAllXST() {
        DataEntity<SXTDetailEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAllSXT());
        return result;
    }

    @Override
    public DataEntity<SXTDWEntity> listAllSXTDW() {
        DataEntity<SXTDWEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAllSXTDW());
        for(Iterator iterator = result.getDataContent().iterator();iterator.hasNext();){
            SXTDWEntity sxtdwEntity = (SXTDWEntity) iterator.next();
            String[] dwIDs = sxtdwEntity.getDWNBBM().split("\\.");
            sxtdwEntity.setLevel(dwIDs.length);
            int parentIDIndex = dwIDs.length-2;
            if(parentIDIndex>=0)
            {
                sxtdwEntity.setParentID(dwIDs[dwIDs.length-2]);
            }

        }
        return result;
    }
}
