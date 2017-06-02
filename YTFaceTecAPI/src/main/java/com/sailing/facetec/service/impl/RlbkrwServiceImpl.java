package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
     * @param bkrwEntities
     * @return
     * @throws ParseException
     */
    @Override
    @Transactional
    public int addMonitorReposity(BkrwEntity[] bkrwEntities) throws ParseException {
        JSONObject jsonObject;
        int result=0;
        // 登录 获取sid
        String sid = loginToYT();

        // 计算布控时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(BkrwEntity bkrwEntity :bkrwEntities) {
            Date stTime = simpleDateFormat.parse(bkrwEntity.getQSSJ());
            Date endTime = simpleDateFormat.parse(bkrwEntity.getZZSJ());
            long stSec = (stTime.getTime() - System.currentTimeMillis()) < 0 ? 0 : (stTime.getTime() - System.currentTimeMillis());
            stSec = stSec / 1000;
            long edSec = (endTime.getTime() - System.currentTimeMillis()) / 1000;

            // 设置布控
            jsonObject = JSONObject.parseObject(ytService.setMonitorRepository(sid, Integer.parseInt(bkrwEntity.getSXTID()), Integer.parseInt(bkrwEntity.getRLKID()), Double.parseDouble(bkrwEntity.getBJFSX()), stSec, edSec));

            if (!"0".equals(jsonObject.getString("rtn"))) {
                continue;
            }

            bkrwEntity.setBKID(jsonObject.getString("id"));
            bkrwEntity.setRWBH(UUID.randomUUID().toString().replace("-", ""));
            bkrwEntity.setTJSJ(CommUtils.getCurrentDate());
            bkrwEntity.setXGSJ(CommUtils.getCurrentDate());
            bkrwEntity.setRWZT("30");

            int isSuccess=rlbkrwMapper.addBkrw(bkrwEntity);
            result++;
        }
        return result;
    }


    /**
     * 删除布控任务
     *
     * @param bkids
     * @return
     */
    @Override
    @Transactional
    public int delMonitorReposity(String bkids) {
        int result=0;
        String sid = loginToYT();
        String[] bkidArray=null;
        bkidArray=bkids.split(",");
        for(String bkid : bkidArray) {
            JSONObject jsonObject = new JSONObject();
            jsonObject = JSONObject.parseObject(ytService.delMonitorReposity(sid, Long.parseLong(bkid)));
            if ("0".equals(jsonObject.getString("rtn"))) {
                rlbkrwMapper.delBkrw(bkid);
                result++;
            }
        }
        return  result;
    }

    /**
     * 查询布控任务
     *
     * @return
     */
    @Override
    public DataEntity<BkrwEntity> queryMonitorReposity(String rlkid) {
        StringBuilder customerFilterBuilder = new StringBuilder();
        StringBuilder customerFilterCountBuilder = new StringBuilder();

        DataEntity<BkrwEntity> result = new DataEntity<BkrwEntity>();
        if("".equals(rlkid))
        {
            customerFilterBuilder.append(String.format("%s", rlkid));
        }
        else {
            customerFilterBuilder.append(String.format("and a.rlkid='%s'", rlkid));
        }
        List<BkrwEntity> listBkrwEntity=rlbkrwMapper.queryMonitorReposity(customerFilterBuilder.toString());
        result.setDataContent(listBkrwEntity);
        return result;

//        BkrwEntity bkrwEntity
//        return List<> listRlShowDetail.queryMonitorReposity();
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
