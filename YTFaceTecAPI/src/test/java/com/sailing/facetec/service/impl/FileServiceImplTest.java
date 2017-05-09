package com.sailing.facetec.service.impl;

import com.sailing.facetec.service.FileService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class FileServiceImplTest {


    @Autowired
    private FileService fileService;

    @Test
    public void createExcel() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("col1","r1c1");
        jsonObject.put("col2","r1c2");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("col1","r2c1");
        jsonObject.put("col2","r2c2");
        jsonArray.add(jsonObject);


        jsonObject = new JSONObject();
        jsonObject.put("col1","r3c1");
        jsonObject.put("col2","r3c2");
        jsonArray.add(jsonObject);

        System.out.println(fileService.createExcel(jsonArray,"D:\\test1.xls","testSheet",true));
        System.out.println(fileService.createExcel(jsonArray,"D:\\test2.xls","testSheet",false));
    }

    @Test
    public void createZip() throws Exception {
        System.out.println(fileService.createZip("D:\\test","d:\\test\\1.zip"));
    }

}