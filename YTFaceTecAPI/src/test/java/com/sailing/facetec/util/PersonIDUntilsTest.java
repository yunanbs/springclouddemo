package com.sailing.facetec.util;

import com.sailing.facetec.config.PlantConfig;
import com.sailing.facetec.entity.PersonIDEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class PersonIDUntilsTest {
    @Autowired
    private PlantConfig plantConfig;
    @Test
    public void getPersonInfo() throws Exception {
        PersonIDEntity result = PersonIDUntils.getPersonInfo("俞楠","310113198403260090");
        System.out.println(result);
    }

}