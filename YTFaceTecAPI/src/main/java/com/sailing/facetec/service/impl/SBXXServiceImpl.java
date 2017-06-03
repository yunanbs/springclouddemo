package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.SBXXMapper;
import com.sailing.facetec.entity.SBXXEntity;
import com.sailing.facetec.service.SBXXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by GuoWang on 2017/6/1.
 */
@Service
public class SBXXServiceImpl implements SBXXService{
    @Autowired
    private SBXXMapper sbxxMapper;

    @Override
    public DataEntity<SBXXEntity> listSBXX(String yhz, String key) {
        StringBuilder customerFilterBuilder = new StringBuilder();
        DataEntity<SBXXEntity> result = new DataEntity<SBXXEntity>();
        if("".equals(key))
        {
            customerFilterBuilder.append(String.format(" yhz='%s'", yhz));
        }
        else {
            customerFilterBuilder.append(String.format(" (sbmc like '%%%s%%' or sbbh like '%%%s%%') and yhz='%s'", key,key,yhz));
        }

        result.setDataContent(sbxxMapper.listSBXX(customerFilterBuilder.toString()));
        return result;
    }
}
