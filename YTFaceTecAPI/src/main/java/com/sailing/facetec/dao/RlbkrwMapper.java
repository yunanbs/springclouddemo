package com.sailing.facetec.dao;

import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.entity.SxtEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/5/16.
 * 布控任务信息
 */
public interface RlbkrwMapper {

    /**
     * 添加布控任务
     * @param bkrwEntity
     * @return
     */
    @Insert("insert into b_tz_rlbkrw values(SEQ_TZ_ZJRL.nextval,'${RWBH}','${SXTID}','${SBBH}','${RLKID}',to_date('${QSSJ}','yyyy-mm-dd hh24:mi:ss'),to_date('${ZZSJ}','yyyy-mm-dd hh24:mi:ss'),'${LGLX}','${LGYY}','${BKLX}','${BKYY}','${BKDW}','${BKZL}','${BKRY}','${BKZT}','${BJFSX}','${SFKBJ}','${BKID}',to_date('${TJSJ}','yyyy-mm-dd hh24:mi:ss'),to_date('${XGSJ}','yyyy-mm-dd hh24:mi:ss'),'${TJRBH}','${TJR}','${XGRBH}','${XGR}','${RWZT}','${RWLX}','${RWJB}','${RWWCBL}','${FWIP}','${YLZD1}','${YLZD2}','${YLZD3}','${YLZD4}','${YLZD5}')")
    int addBkrw(BkrwEntity bkrwEntity);


    /**
     * 删除布控任务
     * @param bkid
     * @return
     */
    @Delete("delete from b_tz_rlbkrw where bkid='${bkid}' ")
    int delBkrw(@Param("bkid") String bkid);

    @Select("select a.*,b.sxtmc as sxtmc,c.rlkmc as rlkmc from b_tz_rlbkrw a left join b_tz_rlsxt b on a.sxtid =b.sxtid left join b_tz_rlk c on a.rlkid = c.rlkid where 1=1 ${customerFilter}")
    List<BkrwEntity> queryMonitorReposity(@Param("customerFilter") String customerFilter);


//    @Select("select b.* from b_tz_rlbkrw a left join b_tz_rlsxt b on a.sxtid =b.sxtid left join b_tz_rlk c on a.rlkid = c.rlkid where b.")
//    List<SxtEntity> queryMonitorReposityRelations(@Param("rlkid") String rlkid);
}
