package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.entity.RlkEntity;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.ImageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class YTServiceImplTest {



    @Autowired
    private YTService ytService;

    @Value("${ytface.username}")
    private String ytUsername;

    @Value("${ytface.password}")
    private String ytPassword;

    @Test
    public void setMonitorByCamera() throws Exception {

        String sid = ytService.login();

        System.out.println(ytService.setMonitorByCamera(sid,"testByYu",70,229,85));
    }

    @Test
    public void delCamera() throws Exception {
        String sid = ytService.login();
        ytService.delCamera(sid,"71");
    }

    @Test
    public void uploadPicToReopsitory() throws Exception {

        String sid = ytService.login();

        RlEntity rlEntity = new RlEntity();

        String base64Str = ImageUtils.picToBase64("D:\\2.jpg").replaceAll("\r|\n", "");

        rlEntity.setBase64Pic(base64Str);

        rlEntity.setCSNF("1981-01-01");
        rlEntity.setXB(0);
        rlEntity.setRKSJ(CommUtils.getCurrentDate());
        rlEntity.setRLKID("2");
        rlEntity.setSFZH("31011111111111");

        ytService.uploadPicToReopsitory(sid,rlEntity);
    }

    @Test
    public void queryFacesByID() throws Exception {
        String picpath = "C:/Users/yunan/Desktop/TIM图片20170607164937.jpg";
        String picStr = FileUtils.fileToBase64(picpath);

        String result = ytService.uploadFaceToQuery(picStr);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String faceid = jsonObject.getJSONArray("results").getJSONObject(0).getString("face_image_id");

        String fields="face_image_id,repository_id,timestamp,person_id,name,gender,born_year,camera_id,timestamp_end,is_hit,rec_gender,similarity";
        String[] repositorys = new String[]{"57"};

        result = ytService.queryFacesByID(ytService.login(),Long.parseLong(faceid),repositorys,40,fields.split(","),null,null,0,1000);

        long timestamp = Long.parseLong(JSONObject.parseObject(result).getJSONArray("results").getJSONObject(0).getString("timestamp"));

        System.out.println(new Date(timestamp*1000));

        System.out.println(result);

    }
}