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

    /**
     * 添加算法评价
     *
     * @param tableName 算法评价表名
     * @param seqName   评价seq名称
     * @param cxdm      查询代码 标识单次查询
     * @param sfdm      算法代码
     * @param fz        分值
     * @param bz        备注
     * @return
     */
    @Insert(insertSql)
    int insertPJ(@Param("tableName") String tableName, @Param("seqName") String seqName, @Param("cxdm") String cxdm, @Param("sfdm") String sfdm, @Param("fz") double fz, @Param("bz") String bz);

    /**
     * 计算评价平均值
     *
     * @param tableName      算法评价表名
     * @param customerFilter 自定义条件
     * @return
     */
    @Select(avgSql)
    List<SfpjAvgEntity> getAvg(@Param("tableName") String tableName, @Param("customerFilter") String customerFilter);

}
