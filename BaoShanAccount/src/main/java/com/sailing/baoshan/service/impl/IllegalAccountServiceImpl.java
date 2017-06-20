package com.sailing.baoshan.service.impl;


import com.sailing.baoshan.dao.AccountDao;
import com.sailing.baoshan.entity.AccountEntity;
import com.sailing.baoshan.service.IllegalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunan on 2017/6/20.
 */
@Service
public class IllegalAccountServiceImpl implements IllegalAccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 获取分类违法行为统计
     *
     * @param beginTime 统计开始时间
     * @param endTime   统计截至时间
     * @param top       获取记录数
     * @param type 0 按设备统计；1 按车牌统计；2 按违法行为统计
     * @return
     */
    @Override
    public List<AccountEntity> getIllegalAccountByTimeAndType(String beginTime, String endTime, String top, String type) {

        List<AccountEntity> result = new ArrayList<>();
        String tag = "";
        String tagTable = "";
        String label = "";
        switch (type){
            case "0":
                tag = "b.SBMC";
                tagTable= " left join B_SSSB_SBXX b on a.accountKey = b.SBBH";
                label = "BZWZDM";
                break;
            case "1":
                tag = "a.accountKey";
                label = "HPHM";
                break;
            case "2":
                tag = "b.DMXMC";
                tagTable= " left join B_QJ_DMX b on a.accountKey = b.DMXZ AND b.DMLXXH = 99";
                label = "JLLB";
                break;
        }
        result = accountDao.getIllegalAccount(label,beginTime,endTime,top,tag,tagTable);
        return result;
    }
}
