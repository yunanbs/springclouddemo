package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.comm.PageEntity;
import com.sailing.facetec.dao.RllrDetailMapper;
import com.sailing.facetec.entity.RllrDetailEntity;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
public class RllrServiceImpl implements RllrService {

    @Autowired
    private RllrDetailMapper rllrDetailMapper;

    @Override
    public DataEntity listRllrDetail(String beginTime, String endTime, String orderBy, int page, int size, String xb, String cameras) {

        DataEntity<RllrDetailEntity> result = new DataEntity<>();

        // 设置检索开始时间
        beginTime = CommUtils.isNullObject(beginTime)?CommUtils.dateToStr(new Date(),"yyyy-MM-dd 00:00:00"):beginTime;
        // 设置检索结束时间
        endTime = CommUtils.isNullObject(endTime)?CommUtils.dateToStr(new Date(),"yyyy-MM-dd 23:59:59"):endTime;
        // 设置排序字段
        orderBy = CommUtils.isNullObject(orderBy)?"a.XH desc":orderBy;

        // 创建自定义过滤条件
        StringBuilder customerFilterBuilder = new StringBuilder();

        // 添加摄像头过滤
        if(!CommUtils.isNullObject(cameras))
        {
            customerFilterBuilder.append(String.format(" and a.SXTID in (%s) ",cameras));
        }

        // 添加性别过滤
        if(!CommUtils.isNullObject(xb)){
            customerFilterBuilder.append(String.format(" and a.XB in (%s) ",xb));
        }

        // 计算分页
        int[] pages = CommUtils.countPage(page,size);

        int counts = rllrDetailMapper.countRllrDetails(beginTime,endTime,customerFilterBuilder.toString());
        List<RllrDetailEntity> datas  = rllrDetailMapper.listRllrDetails(beginTime,endTime,orderBy,pages[0],pages[1],customerFilterBuilder.toString());

        PageEntity pageEntity = new PageEntity(counts,page,size);

        result.setDataContent(datas);
        result.setPageContent(pageEntity);

        return result;
    }

}
