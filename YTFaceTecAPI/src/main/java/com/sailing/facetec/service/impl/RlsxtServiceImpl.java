package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlbkrwMapper;
import com.sailing.facetec.dao.RlsxtMapper;
import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.service.RlsxtService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
@Transactional
public class RlsxtServiceImpl implements RlsxtService {

    @Autowired
    private RlsxtMapper rlsxtMapper;

    @Autowired
    private RlbkrwMapper rlbkrwMapper;

    @Value("${ytface.username}")
    private String ytUsername;

    @Value("${ytface.password}")
    private String ytPassword;

    @Autowired
    private YTService ytService;

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
        JSONObject jsonObject = new JSONObject();
        // 获取sid
        String sid = loginToYT();

        String cameraId;
        String repositoryId;

        // 添加摄像头
        jsonObject = JSONObject.parseObject(ytService.addCamera(sid,sxtEntity.getSXTMC(),sxtEntity.getSPDZ(),0));
        cameraId = jsonObject.getString("id");
        repositoryId = jsonObject.getString("history_repository_id");

        // 更新摄像头状态
        jsonObject = JSONObject.parseObject(ytService.updateCamera(sid,Integer.parseInt(cameraId),"","",1));

        sxtEntity.setSXTID(cameraId);
        sxtEntity.setLRKID(repositoryId);

        return rlsxtMapper.addSXT(sxtEntity);
    }

    @Override
    public int addMonitorReposity(BkrwEntity bkrwEntity) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        // 登录 获取sid
        String sid = loginToYT();

        // 计算布控时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date stTime = simpleDateFormat.parse(bkrwEntity.getQSSJ());
        Date endTime = simpleDateFormat.parse(bkrwEntity.getZZSJ());
        long sec = (endTime.getTime()-stTime.getTime())/1000;

        // 设置布控
        jsonObject =JSONObject.parseObject(ytService.setMonitorRepository(sid,Integer.parseInt(bkrwEntity.getSXTID()),Integer.parseInt(bkrwEntity.getRLKID()),Double.parseDouble(bkrwEntity.getBJFSX()),0,sec));
        return rlbkrwMapper.addBkrw(bkrwEntity);
    }

    private String loginToYT(){
        JSONObject jsonObject = new JSONObject();
        jsonObject = JSONObject.parseObject(ytService.login(ytUsername,ytPassword));
        return jsonObject.getString("session_id");
    }

}
