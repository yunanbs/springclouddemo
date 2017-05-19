package com.sailing.facetec.service;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlgjDetailEntity;

/**
 * Created by yunan on 2017/4/28.
 * 告警Service
 */
public interface RlgjService {
    /**
     * 查询报警数据
     *
     * @param beginTime 查询开始时间 默认为当天00:00:00
     * @param endTime   查询截止时间 默认为当天23:59:59
     * @param orderBy   排序字段 默认按照报警记录xh字段降序排列
     * @param page      分页-页码
     * @param size      分页-分页大小
     * @param xsd       最小相似度 默认为0
     * @param bz        标注编号，多个编号使用 , 分割 默认为空
     * @param rlid      人脸编号 默认为空
     * @param lrkids    路人库编号，多个编号使用 , 分割 默认为空
     * @param sex       性别编号 多个编号使用 , 分割 默认为空
     * @param age       年龄段编号 多个编号使用 , 分割 默认为空
     * @param glass     眼镜特征编号 多个编号使用 , 分割 默认为空
     * @param fringe    刘海特征编号 多个编号使用 , 分割 默认为空
     * @param uygur     种族特征编号 多个编号使用 , 分割 默认为空
     * @return
     */
    DataEntity<RlgjDetailEntity> listRlgjDetail(String beginTime, String endTime, String orderBy, int page, int size, Double xsd, String bz, String rlid, String lrkids, String sex, String age, String glass, String fringe, String uygur);

    /**
     * 更新报警标注数据
     *
     * @param xh     待更新记录序号
     * @param bzsfxt 标准编码
     * @param bzbz   备注信息
     * @return
     */
    int updateRlgjBZ(Long xh, String bzsfxt, String bzbz);


}
