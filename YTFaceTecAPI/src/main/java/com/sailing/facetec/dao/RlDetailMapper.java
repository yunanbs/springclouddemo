package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 * 人脸详细信息
 */
public interface RlDetailMapper {
    String baseSelSql = "select rl.*,rlk.rlkmc from b_tz_rl rl left join b_tz_rlk rlk on rl.rlkid = rlk.rlkid where ${customerFilter}";

    String pageSql = "select * from(select  row_number() over (order by ${orderColumn}) as rn,rl.*,rlk.rlkmc from b_tz_rl rl left join b_tz_rlk rlk on rl.rlkid = rlk.rlkid where ${customerFilter}) a where a.rn between ${min} and ${max}";

    String countSql = "select count(*) from rl.*,rlk.rlkmc from b_tz_rl rl left join b_tz_rlk rlk on rl.rlkid = rlk.rlkid where ${customerFilter}";

    /**
     * 获取人脸详细信息
     *
     * @param customerFilter 自定义过滤条件
     * @return
     */
    @Select(baseSelSql)
    List<RlDetailEntity> listRlDetail(@Param("customerFilter") String customerFilter);




    /**
     * 修改人员信息
     * @param customerFilter
     * @return
     */
    @Update("update b_tz_rl set ${customerFilter}")
    int altPersonalInfo(@Param("customerFilter") String customerFilter);


    /**
     * 删除人员
     *
     * @param rlkEntity YLZD1表示标志位，1代表正常，0代表被删除
     * @return
     */
    @Update("update b_tz_rl set ylzd1='${YLZD1}' where faceID='${RLID}'")
    int delProsonal(RlDetailEntity rlkEntity);
}
