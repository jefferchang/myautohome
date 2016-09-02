package com.autohome.resadmin.domain;

public class SpecItem {

    private   Integer specId;
    private  String askUrl;
    private  String subTitle;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    private String accessId;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }


    public String getAskUrl() {
        return askUrl;
    }

    public void setAskUrl(String askUrl) {
        this.askUrl = askUrl;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }


    public SpecItem() {
    }

    public SpecItem(Integer specid, String askurl, String subtitle,String accessId) {
        this.specId = specid;
        this.askUrl = askurl;
        this.subTitle = subtitle;
        this.accessId=accessId;
    }
}
