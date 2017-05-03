package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlsxtEntity;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlsxtMapper {

    @Select("select * from B_TZ_RLSXT")
    @Results
    List<RlsxtEntity> listAll();
}
