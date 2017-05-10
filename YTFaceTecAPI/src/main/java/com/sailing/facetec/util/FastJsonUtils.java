package com.sailing.facetec.util;

import com.alibaba.fastjson.serializer.NameFilter;

/**
 * Created by yunan on 2017/5/10.
 */
public class FastJsonUtils {
    public static NameFilter nameFilter= new NameFilter() {
        @Override
        public String process(Object o, String s, Object o1) {
            return s.toLowerCase();
        }
    };
}
