package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 */
public interface RlDetailMapper {
    String baseSelSql = "select rl.*,rlk.rlkmc from b_tz_rl rl left join b_tz_rlk rlk on rl.rlkid = rlk.rlkid where ${customerFilter}";

    /**
     * 获取人脸详细信息
     * @param customerFilter 自定义过滤条件
     * @return
     */
    @Select(baseSelSql)
    List<RlDetailEntity> listRlDetail(@Param("customerFilter")String customerFilter);
}
