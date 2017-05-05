package com.sailing.facetec.entity;

/**
 * Created by eagle on 2017-5-4.
 */
public class YtRetrievalResultEntity {

    private String message;
    private YtRetrievalResultDetailEntity[] results;
    private int retrieval_query_id;
    private int rtn;
    private int total;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public YtRetrievalResultDetailEntity[] getResults() {
        return results;
    }

    public void setResults(YtRetrievalResultDetailEntity[] results) {
        this.results = results;
    }

    public int getRetrieval_query_id() {
        return retrieval_query_id;
    }
    public void setRetrieval_query_id(int retrieval_query_id) {
        this.retrieval_query_id = retrieval_query_id;
    }
    public int getRtn() {
        return rtn;
    }
    public void setRtn(int rtn) {
        this.rtn = rtn;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
