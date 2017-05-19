package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RllrDetailEntity;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 * 路人库Service
 */
public interface RllrService {
    // 获取路人详细信息
    DataEntity<RllrDetailEntity> listRllrDetail(String beginTime,String endTime,String orderBy, int page, int size,String lrkids,String sex,String age,String glass,String fringe,String uygur);

    // 获取路人详细信息 实时
    String listRllrDetailReal(String lrkids);
}
