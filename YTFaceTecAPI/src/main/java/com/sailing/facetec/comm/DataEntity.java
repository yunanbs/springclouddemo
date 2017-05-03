package com.sailing.facetec.comm;

import java.io.PipedReader;
import java.util.List;

/**
 * Created by yunan on 2017/5/2.
 */
public class DataEntity<T> {
    private List<T> dataContent;
    private PageEntity pageContent;



    public List<T> getDataContent() {
        return dataContent;
    }

    public void setDataContent(List<T> dataContent) {
        this.dataContent = dataContent;
    }

    public PageEntity getPageContent() {
        return pageContent;
    }

    public void setPageContent(PageEntity pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "dataContent=" + dataContent +
                ", pageContent=" + pageContent +
                '}';
    }
}
