package com.sailing.facetec.dao;

import com.sailing.facetec.entity.SBXXEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by GuoWang on 2017/6/1.
 */
public interface SBXXMapper {
    @Select("select * from b_sssb_sbxx where ${customerFilter}")
    List<SBXXEntity> listSBXX(@Param("customerFilter") String customerFilter);
}
