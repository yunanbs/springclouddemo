package com.sailing.facetec.comm;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by yunan on 2017/4/27.
 */
public class ActionResult {
    private String actioncode;
    private String actionmsg;
    private Object data;
    private Object tag;


    public ActionResult(String actioncode, String actionmsg, Object data, Object tag) {
        this.actioncode = actioncode;
        this.actionmsg = actionmsg;
        this.data = data;
        this.tag = tag;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public String getActionmsg() {
        return actionmsg;
    }

    public void setActionmsg(String actionmsg) {
        this.actionmsg = actionmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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
                "actioncode='" + actioncode + '\'' +
                ", actionmsg='" + actionmsg + '\'' +
                ", data=" + data +
                ", tag=" + tag +
                '}';
    }
}
