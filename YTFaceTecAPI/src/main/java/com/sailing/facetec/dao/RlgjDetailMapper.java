package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlgjDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 * 人脸告警
 */
public interface RlgjDetailMapper {
    String baseLrgjDetailSelSql = "select * from(select  row_number() over (order by ${orderColumn}) as rn,a.*,round(a.XSD,2) as SXSD,b.RLKMC,c.SXTMC as LRKMC,d.XH as BZXH,d.BZSFXT,d.BZBZ,e.JD,e.WD,e.SBBH from  b_tz_rlgj a left join b_tz_rlk b on a. rlkid = b.rlkid left join b_tz_rlsxt c on a.lrkid = c.lrkid left join b_tz_rlgj_bz d on a.rlkrlid = d.rlkrlid and a.lrkrlid = d.lrkrlid left join b_sssb_sbxx e on c.sbbh = e.sbbh  where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') and a.YLZD3='0' ${customerFilter}) a where a.rn between ${min} and ${max}";

    String countSql = "select count(*) from b_tz_rlgj a left join b_tz_rlgj_bz d on a.rlkrlid = d.rlkrlid and a.lrkrlid = d.lrkrlid where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') and a.YLZD3='0' ${customerFilter}";

    /**
     * 获取报警详细信息
     *
     * @param beginTime      查询开始时间
     * @param endTime        查询截止时间
     * @param orderColumn    排序字段
     * @param min            分页开始
     * @param max            分页截止
     * @param customerFilter 自定义条件
     * @return
     */
    @Select(baseLrgjDetailSelSql)
    List<RlgjDetailEntity> listRlgjDetails(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("orderColumn") String orderColumn, @Param("min") int min, @Param("max") int max, @Param("customerFilter") String customerFilter);


    /**
     * 获取查询总记录数
     *
     * @param beginTime      查询开始时间
     * @param endTime        查询截止时间
     * @param customerFilter 自定义条件
     * @return
     */
    @Select(countSql)
    int countRlgjDetails(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("customerFilter") String customerFilter);

    @Update("update b_tz_rlgj set ylzd4='1' where xh in ('${ids}')")
    int setAlertSendFlag(@Param("ids")String ids);



}
