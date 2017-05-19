package com.sailing.facetec.comm;

import lombok.Data;

import java.util.List;

/**
 * 查询数据实体类
 * Created by yunan on 2017/5/2.
 */
@Data
public class DataEntity<T> {
    // 数据对象集合
    private List<T> dataContent;
    // 分页信息
    private PageEntity pageContent;
}
