package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlkEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by yunan on 2017/5/4.
 * 人脸库
 */
public interface RlkMapper {
    String baseSelSql = "select * from B_TZ_RLK";

    /**
     * 获取人脸库数据库信息
     * @return
     */
    @Select(baseSelSql)
    List<RlkEntity> listAllRlk();

    /**
     * 删除人脸库
     * @param rlkEntity YLZD2表示标志位，1代表正常，0代表被删除
     * @return
     */
    @Update("update b_tz_rlk set ylzd2='${YLZD2}' where rlkid='${RLKID}'")
    int delFaceLib(RlkEntity rlkEntity);

    /**
     * 新增人脸库
     * @param rlkEntity YLZD2表示标志位，1代表正常，0代表被删除
     * @return
     */
    @Insert("insert into b_tz_rlk values(SEQ_TZ_ZJRL.nextval,'${RLKID}','${RLKMC}','${RLKLX}','${CCLX}',to_date('${TJSJ}','yyyy-mm-dd hh24:mi:ss'),to_date('${XGSJ}','yyyy-mm-dd hh24:mi:ss'),'${TJRBH}','${TJR}','${XGRBH}','${XGR}','${WCBL}','${ZT}','${RLSL}','${TPSL}','${JZLX}','${BZ}','${YLZD1}','${YLZD2}','${YLZD3}','${YLZD4}','${YLZD5}','${RLKID1}','${RLKID2}','${RLKID3}','${RLKID4}','${RLKID5}','${RLKID6}','${RLKID7}','${RLKID8}','${RLKID9}','${RLKID10}')")
    int addRLK(RlkEntity rlkEntity);

}
