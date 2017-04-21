package com.sailing.demo.microservice.consumerapi.controller;

import com.sailing.demo.microservice.consumerapi.proxy.DemoAPIProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yunan on 2017/4/21.
 */
@RestController
public class ConsumerController {

   // @Autowired
   // private RestTemplate restTemplate;

    // @RequestMapping("/add")
    // public int add(@RequestBody String params){
    //     int result = restTemplate.postForEntity("http://DEMO-API/add",params,int.class).getBody();
    //
    //     return result;
    // }
    //
    // @RequestMapping("/helloworld")
    // public String hello(){
    //     return restTemplate.getForEntity("http://DEMO-API/helloworld",String.class).getBody();
    // }

    @Autowired
    private DemoAPIProxy demoAPIProxy;

    @RequestMapping("/add")
    public int add(@RequestBody String params){
        return  demoAPIProxy.add(params);
    }

}
