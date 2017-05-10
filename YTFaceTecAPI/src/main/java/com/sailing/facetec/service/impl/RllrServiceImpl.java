package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.comm.PageEntity;
import com.sailing.facetec.dao.RllrDetailMapper;
import com.sailing.facetec.entity.RllrDetailEntity;
import com.sailing.facetec.service.RedisService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
public class RllrServiceImpl implements RllrService {

    @Value("${redis-keys.capture-data}")
    private String captureData;

    @Autowired
    private RllrDetailMapper rllrDetailMapper;

    @Autowired
    private RedisService redisService;



    @Override
    public DataEntity<RllrDetailEntity> listRllrDetail(String beginTime,String endTime,String orderBy, int page, int size,String lrkids,String sex,String age,String glass,String fringe,String uygur) {

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
        customerFilterBuilder.append(CommUtils.isNullObject(lrkids)?"":String.format(" and a.LRKID in (%s) ",lrkids));
        // 添加性别过滤
        customerFilterBuilder.append(CommUtils.isNullObject(sex)?"":String.format(" and a.RLTZ1 in (%s) ",sex));
        // 添加年龄过滤
        customerFilterBuilder.append(CommUtils.isNullObject(age)?"":String.format(" and a.RLTZ2 in (%s) ",age));
        // 添加眼镜过滤
        customerFilterBuilder.append(CommUtils.isNullObject(glass)?"":String.format(" and a.RLTZ3 in (%s) ",glass));
        // 添加刘海过滤
        customerFilterBuilder.append(CommUtils.isNullObject(fringe)?"":String.format(" and a.RLTZ4 in (%s) ",fringe));
        // 添加种族过滤
        customerFilterBuilder.append(CommUtils.isNullObject(uygur)?"":String.format(" and a.RLTZ5 in (%s) ",uygur));

        // 计算分页
        int[] pages = CommUtils.countPage(page,size);

        int counts = rllrDetailMapper.countRllrDetails(beginTime,endTime,customerFilterBuilder.toString());
        List<RllrDetailEntity> datas  = rllrDetailMapper.listRllrDetails(beginTime,endTime,orderBy,pages[0],pages[1],customerFilterBuilder.toString());

        PageEntity pageEntity = new PageEntity(counts,page,size);

        result.setDataContent(datas);
        result.setPageContent(pageEntity);

        return result;
    }

    @Override
    public String listRllrDetailReal(String lrkids) {
        JSONObject result = JSONObject.parseObject(redisService.getVal(captureData));
        JSONArray dataArray = result.getJSONArray("dataContent");
        if(!CommUtils.isNullObject(lrkids)){
            dataArray.removeIf(s->!lrkids.contains(((JSONObject)s).getString("lrkid")));
        }
        return result.toString();
    }

}
