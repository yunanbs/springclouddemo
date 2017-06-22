package com.sailing.demo.microservice.demorestapi.controller;


import com.alibaba.fastjson.JSONObject;
import com.sailing.demo.microservice.demorestapi.service.KafkaServer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PrinterAbortException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunan on 2017/4/21.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = {"*"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class DemoController {

    private final Logger logger = LoggerFactory.getLogger(DemoController.class);

    // @Qualifier("discoveryClient")
    // @Autowired
    // private DiscoveryClient client;

    @Autowired
    private Environment environment;

    @Autowired
    private KafkaServer kafkaServer;

    @Value("${server.port}")
    private String port;

    // @Value("${testkey}")
    // private String testkey;

    @RequestMapping("/helloworld")
    public String helloWorld() {
        return "hello world";
    }

    @RequestMapping("/add")
    public int add(@RequestBody String params){
        logger.info("{} be called", environment.getProperty("server.port"));
        JSONObject tmp = JSONObject.parseObject(params);
        int x = tmp.getInteger("x");
        int y = tmp.getInteger("y");
        return x + y;
    }

    // @RequestMapping("/testkey")
    // public String test() {
    //     return testkey;
    // }

    @RequestMapping("/jwtToken")
    public String jwtToken() {
        String result = "";
        Map map = new HashMap();
        map.put("test", "123");
        map.put("testjwt", "234");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 30);
        result = Jwts
                .builder()
                .setClaims(map)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS512, "com.sailing")
                .compact();
        return result;
    }

    @RequestMapping("/unjwt")
    public Claims unJwtToken(@RequestParam("token") String token){
        return Jwts.parser().setSigningKey("com.sailing").parseClaimsJws(token).getBody();


    }

    @RequestMapping("/login")
    public String login(@RequestBody String params){
        return  "";
    }

    @RequestMapping("/kafka")
    public String send(@RequestParam("message")String message){
        kafkaServer.sendMessage("test","123");
        kafkaServer.sendMessage("test","456");
        return "123";
    }
}
