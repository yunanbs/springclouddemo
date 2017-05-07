package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlgjDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlgjDetailMapper {
    // String baseLrgjDetailSelSql = "select * from(select  row_number() over (order by ${orderColumn}) as rn,a.XH,a.RLKID,a.RLKRLID,a.LRKID,a.LRKRLID,round(a.XSD,2) as XSD,a.MZLX,a.SFXT,a.SFZSFXT,a.LRRLDZ,a.LRRKSJ,a.RLSCNF,a.RLXB,a.RLXM,a.RLSFZ,a.RLRLDZ,a.RLRKSJ,a.YLZD1,a.YLZD2,a.YLZD3,a.YLZD4,a.YLZD5,b.RLKMC,c.SXTMC,d.XH as BZXH,d.BZSFXT,d.BZBZ from  b_tz_rlgj a left join b_tz_rlk b on a. rlkid = b.rlkid left join b_tz_rlsxt c on a.lrkid = c.lrkid left join b_tz_rlgj_bz d on a.rlkrlid = d.rlkrlid and a.lrkrlid = d.lrkrlid  where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') ${customerFilter}) a where a.rn between ${min} and ${max}";
    String baseLrgjDetailSelSql = "select * from(select  row_number() over (order by ${orderColumn}) as rn,a.*,round(a.XSD,2) as SXSD,b.RLKMC,c.SXTMC,d.XH as BZXH,d.BZSFXT,d.BZBZ from  b_tz_rlgj a left join b_tz_rlk b on a. rlkid = b.rlkid left join b_tz_rlsxt c on a.lrkid = c.lrkid left join b_tz_rlgj_bz d on a.rlkrlid = d.rlkrlid and a.lrkrlid = d.lrkrlid  where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') ${customerFilter}) a where a.rn between ${min} and ${max}";

    String countSql = "select count(*) from b_tz_rlgj a left join b_tz_rlgj_bz d on a.rlkrlid = d.rlkrlid and a.lrkrlid = d.lrkrlid where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') ${customerFilter}";

    @Select(baseLrgjDetailSelSql)
    List<RlgjDetailEntity> listRlgjDetails(
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("orderColumn") String orderColumn,
            @Param("min") int min,
            @Param("max") int max,
            @Param("customerFilter") String customerFilter
    );


    @Select(countSql)
    int countRlgjDetails(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("customerFilter") String customerFilter);


}
