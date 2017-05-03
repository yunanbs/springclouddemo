package com.sailing.facetec.comm;

/**
 * Created by yunan on 2017/4/27.
 */
public class ActionResult {
    private String responseCode;
    private String actionmsg;
    private Object content;
    private Object tag;


    public ActionResult(String responseCode, String message, Object content, Object tag) {
        this.responseCode = responseCode;
        this.actionmsg = message;
        this.content = content;
        this.tag = tag;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getActionmsg() {
        return actionmsg;
    }

    public void setActionmsg(String actionmsg) {
        this.actionmsg = actionmsg;
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
                ", actionmsg='" + actionmsg + '\'' +
                ", content=" + content +
                ", tag=" + tag +
                '}';
    }
}
