package com.sailing.facetec.service;

import java.util.concurrent.TimeUnit;

/**
 * redis数据库访问
 * Created by yunan on 2017/5/7.
 */
public interface RedisService {

    /**
     * 设置值 nx
     * @param key key名称
     * @param val value
     * @param expire 失效时间 值
     * @param timeUnit 失效时间 单位
     * @return
     */
    boolean setValNX(String key, String val, long expire, TimeUnit timeUnit);

    /**
     * 设置值
     * @param key key名称
     * @param val value
     * @param expire 失效时间 值
     * @param timeUnit 失效时间 单位
     * @return
     */
    void setVal(String key,String val,long expire,TimeUnit timeUnit);

    /**
     * 读取值
     * @param key key
     * @return
     */
    String getVal(String key);

    /**
     * 删除key
     * @param key
     */
    void delKey(String key);

    /**
     * 获取key的有效时间
     * @param key
     * @return
     */
    long getKeyExpire(String key);
}
