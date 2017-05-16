package com.sailing.facetec.dao;

import org.apache.ibatis.annotations.Insert;

/**
 * Created by yunan on 2017/5/16.
 */
public interface RlbkrwMapper {

    @Insert("insert into b_tz_rlbkrw values(seq.nextval,'${rwbh}','${sxtid}','${sbbh}','${rlkid}',to_date('${qssj}','yyyy-mm-dd hh24:mi:ss'),to_date('${zzsj}','yyyy-mm-dd hh24:mi:ss'),'${lglx}','${lgyy}','${bklx}','${bkyy}','${bkdw}','${bkzl}','${bkry}','${bkzt}','${bjfsx}','${sfkbj}','${bkid}',to_date('${tjsj}','yyyy-mm-dd hh24:mi:ss'),to_date('${xgsj}','yyyy-mm-dd hh24:mi:ss'),'${tjrbh}','${tjr}','${xgrbh}','${xgr}','${rwzt}','${rwlx}','${rwjb}','${rwwcbl}','${fwip}','${ylzd1}','${ylzd2}','${ylzd3}','${ylzd4}','${ylzd5}'")
    int addBkrw();
}
