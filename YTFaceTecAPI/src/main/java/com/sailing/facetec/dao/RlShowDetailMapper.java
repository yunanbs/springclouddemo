package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlShowDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by GuoWang on 2017/5/29.
 */
public interface RlShowDetailMapper {

    String baseSelSql ="select * from(select  row_number() over (order by a.xh desc) as rn,a.xh,b.rlkid,a.faceID,a.xm ,case when xb=1 then '男' when xb=2 then '女' else '未知'  end as xb, a.csnf, (a.rlsf || a.rlsf) as szqy, a.sfzh,a.xzdtdz as dtdz, a.xzrldz as rldz, a.xzgxdtdz as gxdtdz,a.xzgxrldz as gxrldz,a.lglx,a.zjlb,a.ylzd2 as mz,a.ylzd3 as qybh from b_tz_rl a left join  b_tz_rlk b on a.rlkid = b.rlkid where a.ylzd1='1' ${customerFilter}) where rn>=${min} and rn<=${max}";

    String countSql ="select count(*) as count from b_tz_rl a left join  b_tz_rlk b on a.rlkid = b.rlkid where a.ylzd1='1' ${customerFilter}";

    /**
     * 模糊查询人脸
     * @param customerFilter 查询条件
     * @param min 起始位置
     * @param max 结束位置
     * @return
     */
    @Select(baseSelSql)
    List<RlShowDetailEntity> listRlShowDetail(@Param("customerFilter") String customerFilter, @Param("min") int min,@Param("max") int max);

    /**
     * 获取人脸数
     * @param customerFilter
     * @return
     */
    @Select(countSql)
    int RlShowDetailCount(@Param("customerFilter") String customerFilter);
}
