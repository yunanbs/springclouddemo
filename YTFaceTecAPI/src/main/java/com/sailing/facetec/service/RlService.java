package com.sailing.facetec.service;


import com.sailing.facetec.comm.DataEntity;
import net.sf.json.JSONArray;

/**
 * Created by yunan on 2017/5/4.
 */
public interface RlService {
    DataEntity listRlDetail(JSONArray detailInfo);
}
