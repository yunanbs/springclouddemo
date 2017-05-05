package com.sailing.facetec.entity;

/**
 * Created by eagle on 2017-5-4.
 */
public class YtImportResultEntity {
    private String message;
    private String picture_uri;
    private String picture_url;
    private YtImportResultDetailEntity[] results;
    private int rtn;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPicture_uri() {
        return picture_uri;
    }
    public void setPicture_uri(String picture_uri) {
        this.picture_uri = picture_uri;
    }
    public String getPicture_url() {
        return picture_url;
    }
    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
    public YtImportResultDetailEntity[] getResults() {
        return results;
    }
    public void setResults(YtImportResultDetailEntity[] results) {
        this.results = results;
    }
    public int getRtn() {
        return rtn;
    }
    public void setRtn(int rtn) {
        this.rtn = rtn;
    }
}
