package com.sailing.facetec.service.impl;

import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.service.RlsxtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class RlsxtServiceImplTest {

    @Autowired
    private RlsxtService rlsxtService;

    @Test
    public void addMonitorByCamera() throws Exception {
        BkrwEntity bkrwEntity = new BkrwEntity();
        bkrwEntity.setSXTID("70");
        bkrwEntity.setRLKID("229");
        bkrwEntity.setSBBH("34020000001320000001");
        bkrwEntity.setBJFSX("85");

        System.out.println(rlsxtService.addMonitorByCamera(bkrwEntity));
    }

}