package com.sailing.facetec.dao;

import com.sailing.facetec.entity.RlEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * 人脸记录
 * Created by yunan on 2017/5/24.
 */
public interface RlMapper {
    String insertSql = "insert into B_TZ_RL values(SEQ_TZ_ZJRL.nextval,'${RLID}','${RLKID}',to_date('${RKSJ}','yyyy-mm-dd hh24:mi:ss'),'${SFZH}','${XB}','${CSNF}','${XM}','${DTDZ}','${RLDZ}','${TJRBH}','${TJR}','${XGRBH}','${XGR}',to_date('${TJSJ}','yyyy-mm-dd hh24:mi:ss'),to_date('${XGSJ}','yyyy-mm-dd hh24:mi:ss'),'${LGLX}','${LGYX}','${SFTB}','${ZJLB}','${ZJHM}','${RLSF}','${RLCS}','${RLGJ}','${YLZD1}','${YLZD2}','${YLZD3}','${YLZD4}','${YLZD5}','${RLKID1}','${RLKID2}','${RLKID3}','${RLKID4}','${RLKID5}','${RLKID6}','${RLKID7}','${RLKID8}','${RLKID9}','${RLKID10}','${RLID1}','${RLID2}','${RLID3}','${RLID4}','${RLID5}','${RLID6}','${RLID7}','${RLID8}','${RLID9}','${RLID10}','${XZDTDZ}','${XZRLDZ}','${XZGXDTDZ}','${XZGXRLDZ}')";
    @Insert(insertSql)
    int insertRl(RlEntity rlEntity);
}
