package com.autohome.resadmin.domain;

/**
 * Created by hujinliang on 2016/4/8.
 */
public class EntryResult {
    public EntryResult(String template, String rawJson,String productIdentity,Integer seriesId,Integer specId,String activityIdentity,String accessId) {
        this.template = template;
        this.rawJson = rawJson;
        this.productIdentity=productIdentity;
        this.seriesId=seriesId;
        this.specId=specId;
        this.activityIdentity=activityIdentity;
        this.accessId=accessId;
    }

    private Integer seriesId;
    private Integer specId;
    private String accessId;

    public String getActivityIdentity() {
        return activityIdentity;
    }
    public void setActivityIdentity(String activityIdentity) {
        this.activityIdentity = activityIdentity;
    }

    public String getAccessId() {return accessId;}
    public void setAccessId(String accessId) {this.accessId = accessId;}

    private String activityIdentity;
    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    private String template;
    private String rawJson;
    private String productIdentity;
    public String getRawJson() {
        return rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getProductIdentity() {
        return productIdentity;
    }
    public void setProductIdentity(String productIdentity) {
        this.productIdentity = productIdentity;
    }

}
