package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sailing.facetec.service.FileService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashMap;

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
        System.out.println(fileService.createZip("D:\\test\\sbu","d:\\sbu.zip"));
    }

    @Test
    public void expDataWithPic() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id","序号");
        jsonObject.put("datacol1","测试属性1");
        jsonObject.put("datacol2","测试属性2");
        jsonObject.put("link-L","大图地址");
        jsonObject.put("link-X","小图地址");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id","1");
        jsonObject.put("datacol1","11");
        jsonObject.put("datacol2","22");
        jsonObject.put("link-L","\\\\172.20.22.10\\tomcat5.0.28\\webapps\\main\\images\\ytlr\\2017051016\\952932ytlrdt.jpg");
        jsonObject.put("link-X","\\\\172.20.22.10\\tomcat5.0.28\\webapps\\main\\images\\ytlr\\2017051016\\952932ytlrxt.jpg");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id","2");
        jsonObject.put("datacol1","33");
        jsonObject.put("datacol2","44");
        jsonObject.put("link-L","\\\\172.20.22.10\\tomcat5.0.28\\webapps\\main\\images\\ytlr\\2017051211\\953081ytlrdt.jpg");
        jsonObject.put("link-X","\\\\172.20.22.10\\tomcat5.0.28\\webapps\\main\\images\\ytlr\\2017051211\\953081ytlrxt.jpg");
        jsonArray.add(jsonObject);


        System.out.println(fileService.expDataWithPic(jsonArray));
    }

}