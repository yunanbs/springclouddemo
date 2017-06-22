package com.sailing.demo.microservice.demorestapi.service;

/**
 * Created by yunan on 2017/6/21.
 */
public interface KafkaServer {
    void sendMessage(String topic, String message);
}
