package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlShowDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by GuoWang on 2017/5/29.
 */
public interface RlShowDetailMapper {

    String baseSelSql ="select * from(select  row_number() over (order by a.xh desc) as rn,a.xh,b.rlkid, a.xm ,case when xb=1 then '男' when xb=2 then '女' else '未知'  end as xb, a.csnf, (a.rlsf || a.rlsf) as szqy, a.sfzh from b_tz_rl a left join  b_tz_rlk b on a.rlkid = b.rlkid where ${customerFilter}) where rn>=${min} and rn<=${max}";

    String countSql ="select count(*) as count from b_tz_rl a left join  b_tz_rlk b on a.rlkid = b.rlkid where ${customerFilter}";


    @Select(baseSelSql)
    List<RlShowDetailEntity> listRlShowDetail(@Param("customerFilter") String customerFilter, @Param("min") int min,@Param("max") int max);

    @Select(countSql)
    int RlShowDetailCount(@Param("customerFilter") String customerFilter);
}
