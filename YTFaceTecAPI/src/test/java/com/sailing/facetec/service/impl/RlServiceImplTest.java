package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.entity.RlDetailEntity;
import com.sailing.facetec.service.RlService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class RlServiceImplTest {

    @Autowired
    private RlService rlService;

    @After
    public void tearDown() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void listRlDetail() throws Exception {
        JSONArray list = new JSONArray();

        JSONObject tmp = new JSONObject();

        tmp.put("sfdm","yt");
        tmp.put("rlkid","72");
        tmp.put("rlid","20266198323167233");
        list.add(tmp);

        tmp = new JSONObject();
        tmp.put("sfdm","hr");
        tmp.put("rlkid","72");
        tmp.put("rlid","20266198323167235");
        list.add(tmp);

        List<RlDetailEntity> result = rlService.listRlDetail(list);

        Assert.assertEquals(2,result.size());


    }

}