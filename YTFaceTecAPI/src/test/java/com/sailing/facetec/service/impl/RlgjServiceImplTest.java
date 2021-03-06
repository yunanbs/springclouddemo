package com.sailing.facetec.service.impl;

import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.service.RlgjService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class RlgjServiceImplTest {


    @Autowired
    private RlgjService rlgjService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void updateRlgjBZ() throws Exception {
        int result = rlgjService.updateRlgjBZ(922599L,"-1","test");
        Assert.assertEquals(1,result);
    }

}