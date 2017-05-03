package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlsxtMapper;
import com.sailing.facetec.entity.RlsxtEntity;
import com.sailing.facetec.service.RlsxtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
public class RlsxtServiceImpl implements RlsxtService {

    @Autowired
    private RlsxtMapper rlsxtMapper;

    @Override
    public DataEntity<RlsxtEntity> listAll() {
        DataEntity<RlsxtEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAll());
        return result;
    }
}
