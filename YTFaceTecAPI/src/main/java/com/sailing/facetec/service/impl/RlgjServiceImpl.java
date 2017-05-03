package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.comm.PageEntity;
import com.sailing.facetec.dao.RlgjDetailMapper;
import com.sailing.facetec.dao.RlgjbzMapper;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.entity.RlgjbzEntity;
import com.sailing.facetec.entity.RllrDetailEntity;
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
    public DataEntity<RlgjDetailEntity> listRlgjDetail(String beginTime, String endTime, String orderBy, int page, int size, Double XSD, String BZ) {
        DataEntity<RlgjDetailEntity> result = new DataEntity<>();

        // 设置检索开始时间
        beginTime = CommUtils.isNullObject(beginTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 00:00:00") : beginTime;
        // 设置检索结束时间
        endTime = CommUtils.isNullObject(endTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 23:59:59") : endTime;
        // 设置排序字段
        orderBy = CommUtils.isNullObject(orderBy) ? "a.XH desc" : orderBy;

        // 创建自定义过滤条件
        StringBuilder customerFilterBuilder = new StringBuilder();

        // 添加阀值
        if (!CommUtils.isNullObject(XSD)) {
            customerFilterBuilder.append(String.format(" and a.XSD >= %s ", XSD));
        }

        // 添加标注
        if (!CommUtils.isNullObject(BZ)) {
            customerFilterBuilder.append(String.format(" and d.BZSFXT in (%s)", BZ));
        }

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
