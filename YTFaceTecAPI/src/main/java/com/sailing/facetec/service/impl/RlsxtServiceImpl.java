package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.dao.RlbkrwMapper;
import com.sailing.facetec.dao.RlsxtMapper;
import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;
import com.sailing.facetec.service.RlsxtService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by yunan on 2017/4/28.
 * 摄像头相关服务
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
    public DataEntity<SxtDetailEntity> listAllXST(String name) {
        DataEntity<SxtDetailEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAllSXT(name));
        return result;
    }

    @Override
    public DataEntity<SxtdwEntity> listAllSXTDW() {
        // 获取摄像头单位信息
        DataEntity<SxtdwEntity> result = new DataEntity<>();
        result.setDataContent(rlsxtMapper.listAllSXTDW());

        // 重构 计算每个单位的级别并设定父节点编号
        result.getDataContent().forEach(s -> {
            // 分级获取各级单位的编号
            String[] dwIDs = s.getDWNBBM().split("\\.");
            // 设定级别
            s.setLevel(dwIDs.length);
            // 设定父节点编号
            int parentIDIndex = dwIDs.length - 2;
            if (parentIDIndex >= 0) {
                s.setParentID(dwIDs[dwIDs.length - 2]);
            }
        });

        return result;
    }

    @Override
    public int addSXT(SxtEntity sxtEntity) {
        int result = 0;
        JSONObject jsonObject;

        // 登录 获取sid
        String sid = loginToYT();

        String cameraId;
        String repositoryId;

        // 添加摄像头
        jsonObject = JSONObject.parseObject(ytService.addCamera(sid, sxtEntity.getSXTMC(), sxtEntity.getSPDZ(), 0));
        // 获取摄像头Id
        cameraId = jsonObject.getString("id");
        // 获取路人库Id
        repositoryId = jsonObject.getString("history_repository_id");

        if (!"0".equals(jsonObject.getString("rtn"))) {
            return result;
        }

        // 更新摄像头状态 启用摄像头
        jsonObject = JSONObject.parseObject(ytService.updateCamera(sid, Integer.parseInt(cameraId), "", "", 1));

        if (!"0".equals(jsonObject.getString("rtn"))) {
            sxtEntity.setYLZD2("0");
        }else{
            sxtEntity.setYLZD2("1");
        }
        sxtEntity.setYLZD1("1");
        sxtEntity.setSXTID(cameraId);
        sxtEntity.setLRKID(repositoryId);

        // 添加摄像头记录
        return rlsxtMapper.addSXT(sxtEntity);

    }


    @Override
    public int addMonitorByCamera(BkrwEntity bkrwEntity) {
        JSONObject jsonObject = new JSONObject();
        // 登录 获取sid
        String sid = loginToYT();
        // 获取摄像头id
        String cameraId = bkrwEntity.getSXTID();
        // 获取布控库id
        String repositoryId = bkrwEntity.getRLKID();
        // 获取报警分数线
        String limit = bkrwEntity.getBJFSX();
        // 生成时间字符串作为布控任务
        String monitorName = CommUtils.dateToStr(new Date(), "yyyyMMddHHmmss");

        // 创建依图布控任务
        String bkId = JSONObject.parseObject(ytService.setMonitorByCamera(sid, monitorName, Integer.parseInt(cameraId), Integer.parseInt(repositoryId), Double.parseDouble(limit))).getString("surveillanceId");

        // 设置布控id
        bkrwEntity.setBKID(bkId);
        // 设置布控任务编号
        bkrwEntity.setRWBH(monitorName);
        // 设置起始时间
        bkrwEntity.setQSSJ(CommUtils.getCurrentDate());
        // 设置终止时间
        bkrwEntity.setZZSJ(CommUtils.getCurrentDate());
        // 设置添加时间
        bkrwEntity.setTJSJ(CommUtils.getCurrentDate());
        // 设置修改时间
        bkrwEntity.setXGSJ(CommUtils.getCurrentDate());
        // 设置任务状态
        bkrwEntity.setRWZT("30");

        return rlbkrwMapper.addBkrw(bkrwEntity);
    }

    /**
     * 删除摄像头
     *
     * @param cameraID
     * @return
     */
    @Override
    public int removeCamera(String cameraID) {
        int result = 0;
        JSONObject jsonObject = new JSONObject();
        // 登录 获取sid
        String sid = loginToYT();
        jsonObject = JSONObject.parseObject(ytService.delCamera(sid, cameraID));
        if ("0".equals(jsonObject.getString("rtn"))) {
            SxtEntity sxtEntity = new SxtEntity();
            sxtEntity.setSXTID(cameraID);
            sxtEntity.setYLZD1("0");
            result = rlsxtMapper.delSXT(sxtEntity);
        }
        return result;
    }

    /**
     * 启停摄像头
     *
     * @param cameraID
     * @param enable 1表示启动，0表示停止
     * @return
     */
    @Override
    public int enableCamera(String cameraID, String enable) {
        int result=0;
        JSONObject jsonObject = new JSONObject();
        // 登录 获取sid
        String sid = loginToYT();
        jsonObject = JSONObject.parseObject(ytService.updateCamera(sid, Integer.parseInt(cameraID), "", "", Integer.parseInt(enable)));
        if ("0".equals(jsonObject.getString("rtn"))) {
            result = rlsxtMapper.enableSXT(cameraID,enable);
        }
        return result;
    }

    /**
     * 登录依图平台
     *
     * @return
     */
    private String loginToYT() {
        JSONObject jsonObject;
        jsonObject = JSONObject.parseObject(ytService.login(ytUsername, ytPassword));

        return jsonObject.getString("session_id");
    }

}
