package com.sailing.facetec.comm;

/**
 * Created by yunan on 2017/4/27.
 */
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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ActionResult{" +
                "responseCode='" + responseCode + '\'' +
                ", message='" + message + '\'' +
                ", content=" + content +
                ", tag=" + tag +
                '}';
    }
}
