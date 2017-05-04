package com.sailing.facetec.comm;

import lombok.Data;

/**
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

    public PageEntity(long count, int index, int size) {
        this.count = count;
        this.pages = (int)Math.ceil((double) count/(double)size);
        this.index = index;
        this.size = size;
    }


}
