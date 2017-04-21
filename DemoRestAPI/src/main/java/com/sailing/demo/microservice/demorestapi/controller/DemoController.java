package com.sailing.demo.microservice.demorestapi.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yunan on 2017/4/21.
 */
@RestController
@CrossOrigin(origins = "*",allowedHeaders = {"*"},methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.OPTIONS})
public class DemoController {

    private final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Qualifier("discoveryClient")
    @Autowired
    private DiscoveryClient client;

    @Autowired
    private Environment environment;

    @Value("${server.port}")
    private String port;

    @RequestMapping("/helloworld")
    public String helloWorld(){
        return "hello world";
    }

    @RequestMapping("/add")
    public int add(@RequestBody String params) throws JSONException {
        logger.info("{} be called",environment.getProperty("server.port"));
        JSONObject tmp = new JSONObject(params);
        int x = tmp.getInt("x");
        int y = tmp.getInt("y");
        return  x+y;
    }
}
