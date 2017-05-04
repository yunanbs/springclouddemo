package com.sailing.facetec.comm;

import lombok.Data;

import java.io.PipedReader;
import java.util.List;

/**
 * Created by yunan on 2017/5/2.
 */
@Data
public class DataEntity<T> {
    private List<T> dataContent;
    private PageEntity pageContent;
}
