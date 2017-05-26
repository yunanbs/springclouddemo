package com.sailing.facetec.util;

import com.sailing.facetec.entity.PersonIDEntity;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yunan on 2017/5/26.
 */
public class PersonIDUntilsTest {
    @Test
    public void getPersonInfo() throws Exception {
        PersonIDEntity result = PersonIDUntils.getPersonInfo("俞楠","310113198403260090");
        System.out.println(result);
    }

}