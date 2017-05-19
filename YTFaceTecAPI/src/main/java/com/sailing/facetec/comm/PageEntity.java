package com.sailing.facetec.comm;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/2.
 * 分页信息
 */
@Data
public class PageEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    private long count;
    private int pages;
    private int index;
    private int size;

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
