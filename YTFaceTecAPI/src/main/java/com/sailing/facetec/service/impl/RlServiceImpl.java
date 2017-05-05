package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.SupplyConfig;
import com.sailing.facetec.dao.RlDetailMapper;
import com.sailing.facetec.dao.RlkMapper;
import com.sailing.facetec.entity.RlDetailEntity;
import com.sailing.facetec.service.RlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 */
@Service
public class RlServiceImpl implements RlService {

    @Autowired
    private RlDetailMapper rlDetailMapper;

    @Autowired
    private SupplyConfig supplyConfig;

    @Override
    public DataEntity<RlDetailEntity> listRlDetail(JSONArray detailInfo) {
        StringBuilder customerFilterBuilder = new StringBuilder();
        for(Iterator iterator = detailInfo.iterator();iterator.hasNext();){
            JSONObject tmp = (JSONObject) iterator.next();
            String supply = tmp.getString("sfdm");
            customerFilterBuilder.append(" ( ");
            customerFilterBuilder.append(
                    String.format(
                            " %s='%s' and %s = '%s' ",
                            supplyConfig.getSupplyMap().get(supply+"_rl_rlkid"),
                            tmp.getString("rlkid"),
                            supplyConfig.getSupplyMap().get(supply+"_rl_rlid"),
                            tmp.getString("rlid")
                            )
            );
            customerFilterBuilder.append(" ) or ");
        }
        customerFilterBuilder.append("1=0");
        DataEntity<RlDetailEntity> result = new DataEntity<>();
        result.setDataContent(rlDetailMapper.listRlDetail(customerFilterBuilder.toString()));
        return result;
    }
}
