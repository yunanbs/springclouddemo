package com.sailing.facetec.comm;

import lombok.Data;

/**
 * 通用返回结果
 * Created by yunan on 2017/4/27.
 */
@Data
public class ActionResult {
    private String responseCode;
    private String message;
    private Object content;
    private Object tag;


    /**
     * 构造函数
     * @param responseCode 返回值代码
     * @param message 返回信息
     * @param content 返回内容
     * @param tag 附加内容
     */
    public ActionResult(String responseCode, String message, Object content, Object tag) {
        this.responseCode = responseCode;
        this.message = message;
        this.content = content;
        this.tag = tag;
    }

}
