package com.sailing.facetec.service.impl;

import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.util.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/6/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class RllrServiceImplTest {

    @Autowired
    private RllrService rllrService;

    @Test
    public void listFaceQueryInfo() throws Exception {
        String picpath = "C:/Users/yunan/Desktop/TIM图片20170607164937.jpg";
        String picStr = FileUtils.fileToBase64(picpath);

        rllrService.listFaceQueryInfo(picStr,new String[]{"57"},40,"2017-06-01 00:00:00","2017-06-08 00:00:00");
    }

    @Test
    public void mapMutilFaceQuery() throws Exception {
        String picpath = "C:/Users/yunan/Desktop/TIM图片20170607164937.jpg";
        String picStr = FileUtils.fileToBase64(picpath);

        String[] faces = new String[2];
        faces[0] = picStr;
        faces[1] = picStr;

        rllrService.mapMutilFaceQuery(faces,new String[]{"57"},40,"2017-06-01 00:00:00","2017-06-08 00:00:00",1,4000L);

    }


}