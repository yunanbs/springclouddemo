package com.sailing.facetec.comm;

import lombok.Data;

/**
 * Created by yunan on 2017/4/27.
 */
@Data
public class ActionResult {
    private String responseCode;
    private String message;
    private Object content;
    private Object tag;


    public ActionResult(String responseCode, String message, Object content, Object tag) {
        this.responseCode = responseCode;
        this.message = message;
        this.content = content;
        this.tag = tag;
    }

}
