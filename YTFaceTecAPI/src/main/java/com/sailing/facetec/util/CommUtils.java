package com.sailing.facetec.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/4/28.
 * 通用工具类
 */
public class CommUtils {
    /**
     * 分页计算
     * @param page
     * @param size
     * @return
     */
    public static int[] countPage(int page,int size){
        int min = (page-1<0)?1:(page-1)*size+1;
        int max = (page-1<0)?size:page*size;
        return  new int[] {min,max};
    }

    /**
     * 空对象判断
     * @param obj
     * @return
     */
    public static boolean isNullObject(Object obj){
        boolean result= false;
        if(null==obj||"".equals(obj)){
            result = true;
        }
        return  result;
    }

    /**
     * 获取当前时间字符串 格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDate(){
        return  dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间转字符串
     * @param date
     * @param formateStr
     * @return
     */
    public static String dateToStr(Date date,String formateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formateStr);
        return  simpleDateFormat.format(date);
    }

    /**
     * 字符串转时间
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = null;
        try {
            result = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * 获取jsonobject属性
     * @param jsonObject
     * @return
     */
    public static String[] getJsonKeys(JSONObject jsonObject){
        String[] result = new String[jsonObject.keySet().size()];
        jsonObject.keySet().toArray(result);
        return  result;
    }

    /**
     * list转数组
     * @param source
     * @return
     */
    public static <T> T[] listToArray(List<T> source){
        T[] result = (T[]) new Object[source.size()];
        result = source.toArray(result);
        return  result;
    }
}
