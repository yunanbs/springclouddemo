package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlkEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 */
public interface RlkMapper {
    String baseSelSql = "select * from B_TZ_RLK";

    @Select(baseSelSql)
    List<RlkEntity> listAllRlk();

}
