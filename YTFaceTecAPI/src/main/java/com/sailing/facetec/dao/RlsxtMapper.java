package com.sailing.facetec.dao;

import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.entity.SxtdwEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 * 路人摄像头
 */
public interface RlsxtMapper {

    /**
     * 获取摄像头信息
     *
     * @return
     */
    @Select("select a.*,b.yhz,b.sbmc from B_TZ_RLSXT a left join b_sssb_sbxx b on a.sbbh = b.sbbh where a.ylzd1='1' and a.sxtmc like '%${name}%'")
    List<SxtDetailEntity> listAllSXT(@Param("name") String name);

    /**
     * 获取包含摄像头的单位信息
     *
     * @return
     */
    @Select("select distinct a.dwbh,a.dwmc,a.dwnbbm from b_rs_dw a ,(select c.dwnbbm from b_tz_rlsxt a left join b_sssb_sbxx b on a.sbbh = b.sbbh left join b_rs_dw c  on b.yhz = c.dwbh where a.ylzd1='1')  b where instr(b.dwnbbm,a.dwbh)>0 order by dwbh")
    List<SxtdwEntity> listAllSXTDW();

    /**
     * 添加摄像头
     * @param sxtEntity
     * YLZD2表示摄像头启停标志位 1表示启动 0表示停止
     * @return
     */
    @Insert("insert into b_tz_rlsxt values(SEQ_TZ_ZJRL.nextval,'${SBBH}','${SXTID}','${SXTMC}','${SXTLX}','${SPDZ}','${LRKID}','${YLZD1}','${YLZD2}','${YLZD3}','${YLZD4}','${YLZD4}')")
    int addSXT(SxtEntity sxtEntity);

    /**
     * 删除摄像头
     * @param sxtEntity YLZD1是标志位，1代表正常，0代表被删除
     * @return
     */
    @Update("update b_tz_rlsxt set ylzd1='${YLZD1}' where SXTID = '${SXTID}'")
    int delSXT(SxtEntity sxtEntity);

    @Update("update b_tz_rlsxt set ylzd2='${enable}' where sxtid = '${sxtid}'")
    int enableSXT(@Param("sxtid") String sxtid, @Param("enable") String enable);
}
