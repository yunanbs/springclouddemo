package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.ImageUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by yunan on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class RlServiceImplTest {


    @Autowired
    private RlService rlService;

    @Test
    public void listRlDetail() throws Exception {
        JSONArray jsonArray = new JSONArray();
        DataEntity result = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("rlid","20266198323167235");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("rlid","20266198323167233");
        jsonArray.add(jsonObject);

        result = rlService.listRlDetail(jsonArray);
        Assert.assertEquals(2,result.getDataContent().size());

        jsonArray.clear();
        jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("lrid","18577348462903956");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("lrid","18577348462903960");
        jsonArray.add(jsonObject);

        result = rlService.listRlDetail(jsonArray);
        Assert.assertEquals(2,result.getDataContent().size());
    }

    @Test
    public void addRlData() throws Exception {
        String base64Str = ImageUtils.picToBase64("D:\\3.jpg").replaceAll("\r|\n", "");
        RlEntity rlEntity = new RlEntity();
        rlEntity.setBase64Pic(base64Str);

        rlEntity.setCSNF("1984-04-26");
        rlEntity.setXM("俞楠");
        rlEntity.setXB(1);
        rlEntity.setRLKID("2");
        rlEntity.setRLSF("上海");
        rlEntity.setRLCS("上海");
        rlEntity.setRLGJ("中国");
        rlEntity.setSFZH("310113198404260095");

        rlService.addRlData(rlEntity);
    }

}