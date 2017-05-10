package com.sailing.facetec.service;


import com.alibaba.fastjson.JSONArray;
import com.sailing.facetec.comm.DataEntity;

/**
 * Created by yunan on 2017/5/4.
 */
public interface RlService {
    DataEntity listRlDetail(JSONArray detailInfo);
}
