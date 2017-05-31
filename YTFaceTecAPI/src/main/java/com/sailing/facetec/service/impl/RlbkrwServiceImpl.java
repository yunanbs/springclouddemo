package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.dao.RlbkrwMapper;
import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.service.RlbkrwService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by GuoWang on 2017/5/29.
 */
@Service
public class RlbkrwServiceImpl implements RlbkrwService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${ytface.username}")
    private String ytUsername;

    @Value("${ytface.password}")
    private String ytPassword;

    @Autowired
    private YTService ytService;

    @Autowired
    private RlbkrwMapper rlbkrwMapper;

    /**
     * 摄像头布控人脸库
     *
     * @param bkrwEntity
     * @return
     * @throws ParseException
     */
    @Override
    public int addMonitorReposity(BkrwEntity bkrwEntity) throws ParseException {
        JSONObject jsonObject;
        // 登录 获取sid
        String sid = loginToYT();

        // 计算布控时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date stTime = simpleDateFormat.parse(bkrwEntity.getQSSJ());
        Date endTime = simpleDateFormat.parse(bkrwEntity.getZZSJ());
        long stSec = (stTime.getTime()-System.currentTimeMillis())<0?0:(stTime.getTime()-System.currentTimeMillis());
        stSec = stSec/1000;
        long edSec = (endTime.getTime() -System.currentTimeMillis()) / 1000;

        // 设置布控
        jsonObject = JSONObject.parseObject(ytService.setMonitorRepository(sid, Integer.parseInt(bkrwEntity.getSXTID()), Integer.parseInt(bkrwEntity.getRLKID()), Double.parseDouble(bkrwEntity.getBJFSX()), stSec, edSec));

        if(!"0".equals(jsonObject.getString("rtn"))){
            return 0;
        }

        bkrwEntity.setBKID(jsonObject.getString("id"));
        bkrwEntity.setRWBH(UUID.randomUUID().toString().replace("-",""));
        bkrwEntity.setTJSJ(CommUtils.getCurrentDate());
        bkrwEntity.setXGSJ(CommUtils.getCurrentDate());
        bkrwEntity.setRWZT("30");

        return rlbkrwMapper.addBkrw(bkrwEntity);
    }


    /**
     * 删除布控任务
     *
     * @param bkid
     * @return
     */
    @Override
    public int delMonitorReposity(String bkid) {
        int result=0;
        String sid = loginToYT();
        JSONObject jsonObject=new JSONObject();
        jsonObject= JSONObject.parseObject(ytService.delMonitorReposity(sid,Long.parseLong(bkid)));
        if("0".equals(jsonObject.getString("rtn")))
        {
            result=rlbkrwMapper.delBkrw(bkid);
        }
        return  result;
    }

    /**
     * 登录依图平台
     * @return
     */
    private String loginToYT() {
        JSONObject jsonObject;
        jsonObject = JSONObject.parseObject(ytService.login(ytUsername, ytPassword));
        return jsonObject.getString("session_id");
    }
}
