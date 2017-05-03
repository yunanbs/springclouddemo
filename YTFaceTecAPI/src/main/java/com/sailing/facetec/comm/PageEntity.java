package com.sailing.facetec.comm;

/**
 * Created by yunan on 2017/5/2.
 */
public class PageEntity {
    private long count;
    private int pages;
    private int index;
    private int size;

    public PageEntity() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageEntity{" +
                "count=" + count +
                ", pages=" + pages +
                ", index=" + index +
                ", siza=" + size +
                '}';
    }
}
