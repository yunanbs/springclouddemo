package com.sailing.facetec.service;

import com.alibaba.fastjson.JSONArray;
import com.sailing.facetec.entity.RlDetailEntity;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 */
public interface RlService {
    List<RlDetailEntity> listRlDetail(JSONArray detailInfo);
}
