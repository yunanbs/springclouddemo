package com.sailing.facetec.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FileUtils.class)
public class FileUtilsTest {
    @Test
    public void upZipFile() throws Exception {
        FileUtils.upZipFile("d:\\test.zip","d:\\testzip\\");
    }

}