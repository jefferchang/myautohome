package com.autohome.resadmin.domain;

/**
 * 固定参数
 */
public class BaseAttribute {

    private Integer resId;
    private String pvId;
    private String requestId;
    private String sessionId;
    private String sessionVid;
    private String referUrl;
    private boolean needlog;

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getPvId() {
        return pvId;
    }

    public void setPvId(String pvId) {
        this.pvId = pvId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionVid() {
        return sessionVid;
    }

    public void setSessionVid(String sessionVid) {
        this.sessionVid = sessionVid;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    public boolean isNeedlog() {
        return needlog;
    }

    public void setNeedlog(boolean needlog) {
        this.needlog = needlog;
    }
}
