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
public class DWServiceImpl implements DWService {
    @Autowired
    private DWMapper dwMapper;

    @Override
    public DataEntity<DWEntity> listDW(String key) {
        DataEntity<DWEntity> result = new DataEntity<DWEntity>();
        StringBuilder customerFilterBuilder = new StringBuilder();
        if("".equals(key))
        {
            customerFilterBuilder.append(String.format("%s",key));
        }
        else
        {
            customerFilterBuilder.append(String.format("and dwmc like '%%%s%%'",key));
        }
        result.setDataContent(dwMapper.listDW(customerFilterBuilder.toString()));

        return result;
    }
}
