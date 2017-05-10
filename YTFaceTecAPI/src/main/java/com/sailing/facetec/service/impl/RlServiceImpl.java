package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.SupplyConfig;
import com.sailing.facetec.dao.RlDetailMapper;
import com.sailing.facetec.dao.RllrDetailMapper;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private RllrDetailMapper rllrDetailMapper;

    @Autowired
    private SupplyConfig supplyConfig;

    @Override
    public DataEntity listRlDetail(JSONArray detailInfo) {
        StringBuilder customerFilterBuilder = new StringBuilder();
        DataEntity result = new DataEntity();
        if(CommUtils.isNullObject(detailInfo.getJSONObject(0).get("lrid")))
        {
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
            result.setDataContent(rlDetailMapper.listRlDetail(customerFilterBuilder.toString()));
        }else{
            List<String> idList = new ArrayList<>();
            detailInfo.forEach(s->{
                idList.add(String.format("'%s'",((JSONObject)s).getString("lrid")));
            });
            result.setDataContent(rllrDetailMapper.listRllrDetailsByRLIDs(String.join(",",idList)));
        }
        return result;
    }
}
