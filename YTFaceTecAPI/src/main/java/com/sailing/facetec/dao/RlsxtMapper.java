package com.sailing.facetec.dao;

import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
public interface RlsxtMapper {

    /**
     * 获取摄像头信息
     *
     * @return
     */
    @Select("select a.*,b.yhz from B_TZ_RLSXT a left join b_sssb_sbxx b on a.sbbh = b.sbbh")
    List<SxtDetailEntity> listAllSXT();

    /**
     * 获取包含摄像头的单位信息
     *
     * @return
     */
    @Select("select distinct a.dwbh,a.dwmc,a.dwnbbm from b_rs_dw a ,(select c.dwnbbm from b_tz_rlsxt a left join b_sssb_sbxx b on a.sbbh = b.sbbh left join b_rs_dw c  on b.yhz = c.dwbh)  b where instr(b.dwnbbm,a.dwbh)>0 order by dwbh")
    List<SxtdwEntity> listAllSXTDW();


    //@Insert("insert into b_tz_rlsxt values(seqs.nextval,'${sbbh}','${sxtid}','${sxtmc}','${sxtlx}','${spdz}','${lrkid}','${ylzd1}','${ylzd2}','${ylzd3}','${ylzd4}','${ylzd5}')")
    //int addCamera(@Param("sbbh") String sbbh, @Param("sxtid") String sxtid, @Param("sxtmc") String sxtmc, @Param("sxtlx") String sxtlx, @Param("spdz") String spdz, @Param("lrkid") String lrkid, @Param("ylzd1") String ylzd1, @Param("ylzd2") String ylzd2, @Param("ylzd3") String ylzd3, @Param("ylzd4") String ylzd4, @Param("ylzd5") String ylzd5);

     @Insert("insert into b_tz_rlsxt values(seqs.nextval,#{SBBH},#SXTID,#SXTMC,#SXTLX,#SPDZ,#LRKID,#YLZD1,#YLZD2,#YLZD3,#YLZD4,#YLZD5)")
    int addSXT(SxtEntity sxtEntity);
}
