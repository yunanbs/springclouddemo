package com.sailing.facetec.comm;

import lombok.Data;

/**
 * 分页实体类
 * Created by yunan on 2017/5/2.
 */
@Data
public class PageEntity {
    private long count;
    private int pages;
    private int index;
    private int size;

    public PageEntity() {
    }

    /**
     * 构造函数
     * @param count 总数据量
     * @param index 当前页码
     * @param size 分页大小
     */
    public PageEntity(long count, int index, int size) {
        this.count = count;
        // 总页码 根据数据量及分页大小计算获得
        this.pages = (int)Math.ceil((double) count/(double)size);
        this.index = index;
        this.size = size;
    }


}
