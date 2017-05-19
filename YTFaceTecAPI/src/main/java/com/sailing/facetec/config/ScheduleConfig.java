package com.sailing.facetec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yunan on 2017/5/7.
 * 定时任务配置项 启用线程池 支持多scheduler并发
 * 默认的scheduler是串行的 多个scheduler串行执行
 * 使用线程池后 多个线程是并发 提高scheduler的执行效率
 * 如果使用分布式部署，需要考虑多个同样的scheduler之间的同步 在这里用了分布式锁来实现
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor()
    {
        // 获取线程池 设定线程池大小
        return Executors.newScheduledThreadPool(100);
    }
}
