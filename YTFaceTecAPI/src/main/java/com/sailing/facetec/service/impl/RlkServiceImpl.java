package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlkMapper;
import com.sailing.facetec.entity.RlkEntity;
import com.sailing.facetec.service.RlkService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by yunan on 2017/5/4.
 */
@Service
public class RlkServiceImpl implements RlkService {

    @Autowired
    private RlkMapper rlkMapper;

    @Autowired
    private YTService ytService;

    @Value("${ytface.username}")
    private String ytUsername;

    @Value("${ytface.password}")
    private String ytPassword;

    @Override
    public DataEntity<RlkEntity> listAllRlk() {
        DataEntity<RlkEntity> result = new DataEntity<>();
        result.setDataContent(rlkMapper.listAllRlk());
        return result;
    }

    /**
     * 新增人脸库
     * @param rlkEntity
     * @return
     */
    @Override
    public String addFaceLib(RlkEntity rlkEntity) {
        String result="";
        String sid=loginToYT();
        JSONObject jsonObject=JSONObject.parseObject(ytService.addFaceLib(sid, rlkEntity.getRLKMC(),rlkEntity.getBZ()));
        if("0".equals(jsonObject.getString("rtn"))){
            rlkEntity.setRLKID(jsonObject.getString("id"));
            rlkEntity.setRLKID1(jsonObject.getString("id"));
            rlkEntity.setRLKLX(1);
            rlkEntity.setYLZD2("1");
            rlkEntity.setTJSJ(CommUtils.getCurrentDate());
            result = rlkMapper.addRLK(rlkEntity)==1?rlkEntity.getRLKID():"";
        }
        return result;
    }

    /**
     * 删除人脸库
     * @param rlkID
     * @return 返回不为 0 成功
     */
    @Override
    public int delFaceLib(String rlkID) {
        int result =0;
        // 登录 获取sid
        String sid = loginToYT();
        JSONObject jsonObject = JSONObject.parseObject(ytService.delFaceLib(sid,rlkID));
        if("0".equals(jsonObject.getString("rtn"))){
            RlkEntity rlkEntity = new RlkEntity();
            rlkEntity.setRLKID(rlkID);
            rlkEntity.setXGSJ(CommUtils.getCurrentDate());
            rlkEntity.setYLZD2("0");
            result = rlkMapper.delFaceLib(rlkEntity);
        }
        return result;
    }

    private String loginToYT() {
        JSONObject jsonObject;
        jsonObject = JSONObject.parseObject(ytService.login(ytUsername, ytPassword));
        return jsonObject.getString("session_id");
    }
}
