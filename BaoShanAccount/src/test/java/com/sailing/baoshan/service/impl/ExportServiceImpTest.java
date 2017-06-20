package com.sailing.baoshan.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.baoshan.service.ExportService;
import com.sailing.baoshan.service.IllegalAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.baoshan.app.class)
public class ExportServiceImpTest {
    @Autowired
    private IllegalAccountService illegalAccountService;

    @Autowired
    private ExportService exportService;
    @Test
    public void expIllegalAccountData() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("camera-data",illegalAccountService.getIllegalAccountByTimeAndType("2017-06-01 00:00:00","2017-06-20 00:00:00","10","0"));
        jsonObject.put("licence-data",illegalAccountService.getIllegalAccountByTimeAndType("2017-06-01 00:00:00","2017-06-20 00:00:00","10","1"));
        jsonObject.put("illegal-data",illegalAccountService.getIllegalAccountByTimeAndType("2017-06-01 00:00:00","2017-06-20 00:00:00","10","2"));

        String uri = exportService.expIllegalAccountData(jsonObject,"test");
        assertEquals("test",uri);
    }

}