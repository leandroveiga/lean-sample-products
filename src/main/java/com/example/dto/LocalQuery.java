package com.example.dto;

/**
 * Created by pedroxs on 12/11/15.
 */
public class LocalQuery {
    private String attribute;
    private String queryString;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
