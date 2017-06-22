package com.sailing.demo.microservice.demorestapi.service.impl;

import com.sailing.demo.microservice.demorestapi.service.KafkaServer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * Created by yunan on 2017/6/21.
 */
@Service
public class KafkaServerImpl implements KafkaServer{

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        System.out.println("begin send");
        kafkaTemplate.send(topic,message).addCallback(new SuccessCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println("succeed");
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("fail"+ throwable.getMessage());
            }
        });

    }

    @KafkaListener(topics = {"test"})
    private void listen(ConsumerRecord<String,String>consumerRecord){
        System.out.println(String.format("get message %s %s",consumerRecord.key(),consumerRecord.value()));
    }
}
