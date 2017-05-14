package com.sailing.facetec.comm;

import lombok.Data;

import java.io.PipedReader;
import java.util.List;

/**
 * 查询数据实体类
 * Created by yunan on 2017/5/2.
 */
@Data
public class DataEntity<T> {
    // 数据实体类集合
    private List<T> dataContent;
    // 分页信息实体类
    private PageEntity pageContent;
}
