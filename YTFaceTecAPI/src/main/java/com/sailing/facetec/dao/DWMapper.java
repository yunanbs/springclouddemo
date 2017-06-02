package com.sailing.facetec.dao;

import com.sailing.facetec.entity.DWEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by GuoWang on 2017/6/1.
 */
public interface DWMapper {
    @Select("select * from b_rs_dw")
    List<DWEntity> listDW();
}
