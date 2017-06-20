package com.sailing.baoshan.service;

import com.sailing.baoshan.entity.AccountEntity;

import java.util.List;

/**
 * Created by yunan on 2017/6/20.
 * 违法统计接口
 */
public interface IllegalAccountService {
    /**
     * 获取分类违法行为统计
     * @param beginTime 统计开始时间
     * @param endTime 统计截至时间
     * @param top 获取记录数
     * @param type 0 按设备统计；1 按车牌统计=；2 按违法行为统计
     * @return
     */
    List<AccountEntity> getIllegalAccountByTimeAndType(String beginTime, String endTime, String top, String type);
}
