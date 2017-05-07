package com.sailing.facetec.service.impl;

import com.netflix.discovery.converters.Auto;
import com.sailing.facetec.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by yunan on 2017/5/7.
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean setValNX(String key, String val, long expire, TimeUnit timeUnit) {
        boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key,val);
        stringRedisTemplate.expire(key,expire,timeUnit);
        return result;
    }

    @Override
    public void setVal(String key, String val, long expire, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key,val,expire,timeUnit);
    }

    @Override
    public String getVal(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void delKey(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public long getKeyExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }
}
