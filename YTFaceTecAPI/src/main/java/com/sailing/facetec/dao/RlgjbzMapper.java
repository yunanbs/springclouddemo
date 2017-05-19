package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlgjbzEntity;
import org.apache.ibatis.annotations.Update;

/**
 * Created by yunan on 2017/5/2.
 * 告警备注
 */
public interface RlgjbzMapper {

    String updateSql = "update B_TZ_RLGJ_BZ set BZSFXT = #{BZSFXT},BZBZ=#{BZBZ} WHERE XH = #{XH}";

    /**
     * 更新报警备注
     * @param rlgjbzEntity 待更新内容
     * @return
     */
    @Update(updateSql)
    int updateRlgjBz(RlgjbzEntity rlgjbzEntity);
}
