package com.sailing.facetec.service;

import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/7.
 */
public interface RedisService {
    boolean setValNX(String key, String val, long expire, TimeUnit timeUnit);

    void setVal(String key,String val,long expire,TimeUnit timeUnit);

    String getVal(String key);

    void delKey(String key);

    long getKeyExpire(String key);
}
