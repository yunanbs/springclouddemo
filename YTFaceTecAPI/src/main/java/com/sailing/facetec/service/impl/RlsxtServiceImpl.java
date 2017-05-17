package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlsxtMapper;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;
import com.sailing.facetec.remoteservice.YTApi;
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

    @Autowired
    private YTApi ytService;

    @Override
    public DataEntity<SxtDetailEntity> listAllXST() {
        DataEntity<SxtDetailEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAllSXT());
        return result;
    }

    @Override
    public DataEntity<SxtdwEntity> listAllSXTDW() {
        DataEntity<SxtdwEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAllSXTDW());
        for(Iterator iterator = result.getDataContent().iterator();iterator.hasNext();){
            SxtdwEntity sxtdwEntity = (SxtdwEntity) iterator.next();
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

    @Override
    public int addSXT(SxtEntity sxtEntity) {
        JSONObject params = new JSONObject();

        // String result = ytService.addCamera(params.toJSONString());
        // params = JSONObject.parseObject(result);
        // sxtEntity.setSXTID(params.getString("sxtid"));
        // sxtEntity.setLRKID(params.getString("lrkid"));
        //
        // params.clear();
        // result = ytService.enableCamera(params.toJSONString());
        // params = JSONObject.parseObject(result);
        // sxtEntity.setYLZD1(params.getString("state"));

       // rlsxtMapper.addSXT(sxtEntity);
        return 0;
    }
}
