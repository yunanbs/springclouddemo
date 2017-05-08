package com.sailing.facetec.dao;

import com.sailing.facetec.entity.SfpjAvgEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/5/8.
 */
public interface SfpjMapper {
    String insertSql = "insert into ${tableName} values(${seqName}.nextval,sysdate,'${cxdm}','${sfdm}','${fz}','${bz}')";

    String avgSql = "select SFDM,round(avg(fz),2) as FZ from ${tableName} ${customerFilter} group by SFDM";

    @Insert(insertSql)
    int insertPJ(
            @Param("tableName") String tableName,
            @Param("seqName") String seqName,
            @Param("cxdm") String cxdm,
            @Param("sfdm") String sfdm,
            @Param("fz") double fz,
            @Param("bz") String bz
    );

    @Select(avgSql)
    List<SfpjAvgEntity> getAvg(
            @Param("tableName") String tableName,
            @Param("customerFilter") String customerFilter
    );

}
