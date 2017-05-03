package com.sailing.facetec.util;

import com.sailing.facetec.comm.PageEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yunan on 2017/4/28.
 */
public class CommUtils {
    public static int[] countPage(int page,int size){
        int min = (page-1<0)?1:(page-1)*size+1;
        int max = (page-1<0)?size:page*size;
        return  new int[] {min,max};
    }

    public static boolean isNullObject(Object obj){
        boolean result= false;
        if(null==obj||"".equals(obj)){
            result = true;
        }
        return  result;
    }

    public static String getCurrentDate(){
        return  dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToStr(Date date,String formateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formateStr);
        return  simpleDateFormat.format(date);
    }

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
}
