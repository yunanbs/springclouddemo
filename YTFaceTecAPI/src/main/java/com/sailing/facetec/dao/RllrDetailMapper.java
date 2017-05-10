package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RllrDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RllrDetailMapper {

    String baseRllrDetailSelSql = "select * from (select  row_number() over (order by ${orderColumn}) as rn, a.*,b.SXTMC from B_TZ_RLLR a left join B_TZ_RLSXT b on a.SXTID = b.SXTID where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') and (a.YLZD3='0' or (a.YLZD4='0' and a.SFMZ=0)) ${customerFilter}) a where a.rn between ${min} and ${max}";

    String countSql = "select  count(*) from B_TZ_RLLR a where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') and (a.YLZD3='0' or (a.YLZD4='0' and a.SFMZ=0)) ${customerFilter}";

    String selRllrDetailByRLID = "select * from B_TZ_RLLR where RLID in (${rlids})";

    @Select(baseRllrDetailSelSql)
    List<RllrDetailEntity> listRllrDetails(
            @Param("beginTime")String beginTime,
            @Param("endTime")String endTime,
            @Param("orderColumn") String orderColumn,
            @Param("min") int min,
            @Param("max") int max,
            @Param("customerFilter") String customerFilter
    );

    @Select(countSql)
    int countRllrDetails(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("customerFilter") String customerFilter);

    @Select(selRllrDetailByRLID)
    List<RllrDetailEntity> listRllrDetailsByRLIDs(@Param("rlids")String rlids);
}
