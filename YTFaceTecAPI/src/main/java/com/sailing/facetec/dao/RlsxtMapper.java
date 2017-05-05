package com.sailing.facetec.dao;

import com.sailing.facetec.entity.SXTDetailEntity;
import com.sailing.facetec.entity.SXTDWEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlsxtMapper {

    @Select("select a.*,b.yhz from B_TZ_RLSXT a left join b_sssb_sbxx b on a.sbbh = b.sbbh")
    List<SXTDetailEntity> listAllSXT();

    @Select("select distinct a.dwbh,a.dwmc,a.dwnbbm from b_rs_dw a ,(select c.dwnbbm from b_tz_rlsxt a left join b_sssb_sbxx b on a.sbbh = b.sbbh left join b_rs_dw c  on b.yhz = c.dwbh)  b where instr(b.dwnbbm,a.dwbh)>0 order by dwbh")
    List<SXTDWEntity> listAllSXTDW();
}
