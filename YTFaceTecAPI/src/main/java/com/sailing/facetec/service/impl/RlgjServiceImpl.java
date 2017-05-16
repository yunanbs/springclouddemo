package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.comm.PageEntity;
import com.sailing.facetec.dao.RlgjDetailMapper;
import com.sailing.facetec.dao.RlgjbzMapper;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.entity.RlgjbzEntity;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 */
@Service
public class RlgjServiceImpl implements RlgjService {

    @Autowired
    private RlgjDetailMapper rlgjDetailMapper;

    @Autowired
    private RlgjbzMapper rlgjbzMapper;

    @Override
    public DataEntity<RlgjDetailEntity> listRlgjDetail(String beginTime, String endTime, String orderBy, int page, int size, Double xsd, String bz, String rlid, String lrkids, String sex, String age, String glass, String fringe, String uygur) {
        DataEntity<RlgjDetailEntity> result = new DataEntity<>();

        // 设置检索开始时间
        beginTime = CommUtils.isNullObject(beginTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 00:00:00") : beginTime;
        // 设置检索结束时间
        endTime = CommUtils.isNullObject(endTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 23:59:59") : endTime;

        // 设置排序字段
        orderBy = CommUtils.isNullObject(orderBy) ? "a.XH desc" : orderBy;

        // 创建自定义过滤条件
        StringBuilder customerFilterBuilder = new StringBuilder();
        // 添加阀值条件
        customerFilterBuilder.append(CommUtils.isNullObject(xsd)?"":String.format(" and a.xsd >= %s ", xsd));
        // 添加标注条件
        customerFilterBuilder.append(CommUtils.isNullObject(bz)?"":String.format(" and d.BZSFXT in (%s)", bz));
        // 添加路人人脸编号条件
        customerFilterBuilder.append(CommUtils.isNullObject(rlid)?"":String.format(" and a.LRKRLID = '%s'", rlid));
        // 添加摄像头过滤
        customerFilterBuilder.append(CommUtils.isNullObject(lrkids)?"":String.format(" and a.LRKID in (%s) ",lrkids));
        // 添加性别过滤
        customerFilterBuilder.append(CommUtils.isNullObject(sex)?"":String.format(" and a.LRTZ1 in (%s) ",sex));
        // 添加年龄过滤
        customerFilterBuilder.append(CommUtils.isNullObject(age)?"":String.format(" and a.LRTZ2 in (%s) ",age));
        // 添加眼镜过滤
        customerFilterBuilder.append(CommUtils.isNullObject(glass)?"":String.format(" and a.LRTZ3 in (%s) ",glass));
        // 添加刘海过滤
        customerFilterBuilder.append(CommUtils.isNullObject(fringe)?"":String.format(" and a.LRTZ4 in (%s) ",fringe));
        // 添加种族过滤
        customerFilterBuilder.append(CommUtils.isNullObject(uygur)?"":String.format(" and a.LRTZ5 in (%s) ",uygur));

        // 计算分页
        int[] pages = CommUtils.countPage(page, size);

        int counts = rlgjDetailMapper.countRlgjDetails(beginTime, endTime, customerFilterBuilder.toString());
        List<RlgjDetailEntity> datas = rlgjDetailMapper.listRlgjDetails(beginTime, endTime, orderBy, pages[0], pages[1], customerFilterBuilder.toString());

        PageEntity pageEntity = new PageEntity(counts,page,size);

        result.setDataContent(datas);
        result.setPageContent(pageEntity);

        return result;
    }

    @Override
    public int updateRlgjBZ(Long xh, String bzsfxt, String bzbz) {
        RlgjbzEntity rlgjbzEntity = new RlgjbzEntity();
        rlgjbzEntity.setXH(xh);
        rlgjbzEntity.setBZSFXT(bzsfxt);
        rlgjbzEntity.setBZBZ(bzbz);
        return rlgjbzMapper.updateRlgjBz(rlgjbzEntity);
    }

}
