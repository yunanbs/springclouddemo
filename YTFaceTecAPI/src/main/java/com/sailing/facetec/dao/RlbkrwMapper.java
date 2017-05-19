package com.sailing.facetec.dao;

import com.sailing.facetec.entity.BkrwEntity;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by yunan on 2017/5/16.
 * 布控任务信息
 */
public interface RlbkrwMapper {

    /**
     * 添加布控任务
     * @param bkrwEntity
     * @return
     */
    @Insert("insert into b_tz_rlbkrw values(B_TZ_RLBKRW_XH.nextval,'${RWBH}','${SXTID}','${SBBH}','${RLKID}',to_date('${QSSJ}','yyyy-mm-dd hh24:mi:ss'),to_date('${ZZSJ}','yyyy-mm-dd hh24:mi:ss'),'${LGLX}','${LGYY}','${BKLX}','${BKYY}','${BKDW}','${BKZL}','${BKRY}','${BKZT}','${BJFSX}','${SFKBJ}','${BKID}',to_date('${TJSJ}','yyyy-mm-dd hh24:mi:ss'),to_date('${XGSJ}','yyyy-mm-dd hh24:mi:ss'),'${TJRBH}','${TJR}','${XGRBH}','${XGR}','${RWZT}','${RWLX}','${RWJB}','${RWWCBL}','${FWIP}','${YLZD1}','${YLZD2}','${YLZD3}','${YLZD4}','${YLZD5}')")
    int addBkrw(BkrwEntity bkrwEntity);
}
