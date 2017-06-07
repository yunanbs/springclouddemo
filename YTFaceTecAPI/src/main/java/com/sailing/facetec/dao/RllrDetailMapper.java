package com.sailing.facetec.dao;

import com.sailing.facetec.entity.FaceMapInfoEntity;
import com.sailing.facetec.entity.RllrDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 * 路人抓拍详细信息
 */
public interface RllrDetailMapper {

    String baseRllrDetailSelSql = "select * from (select  row_number() over (order by ${orderColumn}) as rn, a.*,b.SXTMC from B_TZ_RLLR a left join B_TZ_RLSXT b on a.SXTID = b.SXTID where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') and (a.YLZD3='0' or (a.YLZD4='0' and a.SFMZ=0)) ${customerFilter}) a where a.rn between ${min} and ${max}";

    String countSql = "select  count(*) from B_TZ_RLLR a where a.LRRKSJ>=to_date('${beginTime}','yyyy-mm-dd hh24:mi:ss') and  a.LRRKSJ<=to_date('${endTime}','yyyy-mm-dd hh24:mi:ss') and (a.YLZD3='0' or (a.YLZD4='0' and a.SFMZ=0)) ${customerFilter}";

    // String selRllrDetailByRLID = "select a.*,b.SXTMC from B_TZ_RLLR a left join B_TZ_RLSXT b on a.SXTID = b.SXTID where a.RLID in (${rlids})";
    String selRllrDetailByRLID = "select a.*,b.SXTMC,c.JD,c.WD from B_TZ_RLLR a left join B_TZ_RLSXT b on a.SXTID = b.SXTID left join B_SSSB_SBXX c on b.SBBH= c.SBBH where a.RLID in (${rlids})";

    /**
     * 获取人脸抓拍数据
     *
     * @param beginTime      查询开始时间
     * @param endTime        查询截止时间
     * @param orderColumn    排序字段
     * @param min            分页开始
     * @param max            分页截止
     * @param customerFilter 自定义条件
     * @return
     */
    @Select(baseRllrDetailSelSql)
    List<RllrDetailEntity> listRllrDetails(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("orderColumn") String orderColumn, @Param("min") int min, @Param("max") int max, @Param("customerFilter") String customerFilter);

    /**
     * 获取查询总记录数
     *
     * @param beginTime      查询开始时间
     * @param endTime        查询截止时间
     * @param customerFilter 自定义条件
     * @return
     */
    @Select(countSql)
    int countRllrDetails(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("customerFilter") String customerFilter);

    /**
     * 查询符合指定人脸id的人脸记录
     *
     * @param rlids
     * @return
     */
    @Select(selRllrDetailByRLID)
    List<RllrDetailEntity> listRllrDetailsByRLIDs(@Param("rlids") String rlids);

    /**
     * 获取人脸相关信息 地图展示用
     * @param faceIDs
     * @return
     */
    @Select(selRllrDetailByRLID)
    List<FaceMapInfoEntity> listFaceMapInfoByFaceIDs(@Param("faceids") String faceIDs);
}
