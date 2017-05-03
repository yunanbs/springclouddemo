package com.sailing.facetec.comm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yunan on 2017/4/27.
 */
@Configuration
@ConfigurationProperties(prefix = "actioncode")
public class ActionCode {

    public static String SUCCEED_CODE;
    public static String SUCCEED_MSG;

    public static String SERVER_ERROR_CODE;
    public static String SERVER_ERROR_MSG;

    public static String getSucceedCode() {
        return SUCCEED_CODE;
    }

    public static void setSucceedCode(String succeedCode) {
        SUCCEED_CODE = succeedCode;
    }

    public static String getSucceedMsg() {
        return SUCCEED_MSG;
    }

    public static void setSucceedMsg(String succeedMsg) {
        SUCCEED_MSG = succeedMsg;
    }

    public static String getServerErrorCode() {
        return SERVER_ERROR_CODE;
    }

    public static void setServerErrorCode(String serverErrorCode) {
        SERVER_ERROR_CODE = serverErrorCode;
    }

    public static String getServerErrorMsg() {
        return SERVER_ERROR_MSG;
    }

    public static void setServerErrorMsg(String serverErrorMsg) {
        SERVER_ERROR_MSG = serverErrorMsg;
    }
}
