package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.service.RlService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

}