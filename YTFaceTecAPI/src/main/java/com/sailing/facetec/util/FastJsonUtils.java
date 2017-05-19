package com.sailing.facetec.util;

import com.alibaba.fastjson.serializer.NameFilter;

/**
 * Created by yunan on 2017/5/10.
 * fastjson 自定义属性名转换
 */
public class FastJsonUtils {
    public static NameFilter nameFilter= new NameFilter() {
        @Override
        public String process(Object o, String s, Object o1) {
            // 属性名转小写
            String firstChar= s.substring(0,1);
            String result=firstChar.equals(firstChar.toUpperCase())?s.toLowerCase():s;
            return result;
        }
    };
}
