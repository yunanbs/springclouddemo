package com.sailing.baoshan.service.impl;

import com.sailing.baoshan.entity.AccountEntity;
import com.sailing.baoshan.service.IllegalAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.baoshan.app.class)
public class IllegalAccountServiceImplTest {
    @Autowired
    private IllegalAccountService illegalAccountService;
    @Test
    public void getIllegalAccountByTimeAndType() throws Exception {
        List<AccountEntity> results = illegalAccountService.getIllegalAccountByTimeAndType(
                "2017-06-01 00:00:00",
                "2017-06-20 00:00:00",
                "10",
                "1"
                );
        assertEquals(10,results.size());
    }

}