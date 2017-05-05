package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlkMapper;
import com.sailing.facetec.entity.RlkEntity;
import com.sailing.facetec.service.RlkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 */
@Service
public class RlkServiceImpl implements RlkService {

    @Autowired
    private RlkMapper rlkMapper;

    @Override
    public DataEntity<RlkEntity> listAllRlk() {
        DataEntity<RlkEntity> result = new DataEntity<>();
        result.setDataContent(rlkMapper.listAllRlk());
        return result;
    }
}
