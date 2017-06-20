package com.sailing.baoshan.dao;

import com.sailing.baoshan.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/6/20.
 */
public interface AccountDao {
    String baseIllegalAccountSql = "select * from (SELECT row_number() OVER (ORDER BY a.accountValue DESC ) AS id,a.*,${tag} AS accountTag FROM(SELECT ${label} AS accountKey,count(*) AS accountValue FROM B_KKQP_WFTC WHERE JGSJ >= to_date('${beginTime}', 'yyyy-mm-dd hh24:mi:ss') AND JGSJ <= to_date('${endTime}', 'yyyy-mm-dd hh24:mi:ss') GROUP BY ${label}) a ${tagTable}) where id<=${top}";

    @Select(baseIllegalAccountSql)
    List<AccountEntity> getIllegalAccount(
            @Param("label") String label,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("top") String top,
            @Param("tag") String tag,
            @Param("tagTable") String tagTable
    );
}
