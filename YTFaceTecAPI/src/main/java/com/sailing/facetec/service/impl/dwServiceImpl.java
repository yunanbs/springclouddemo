package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.DWMapper;
import com.sailing.facetec.entity.DWEntity;
import com.sailing.facetec.service.DWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by GuoWang on 2017/6/1.
 */
@Service
public class dwServiceImpl implements DWService {
    @Autowired
    private DWMapper dwMapper;

    @Override
    public DataEntity<DWEntity> listDW() {
        DataEntity<DWEntity> result = new DataEntity<DWEntity>();
        result.setDataContent(dwMapper.listDW());

        return result;
    }
}
