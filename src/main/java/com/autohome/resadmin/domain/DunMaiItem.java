package com.autohome.resadmin.domain;

/**
 * Created by hujinliang on 2016/6/15.
 */
public class DunMaiItem {
    private String ProductIdentity;
    private int DealerId;
    private int TypeId;
    private int ActivityId;
    private String ActivityName;
    private String PreTxt;
    private String DealerAddress;
    private String DealerName;
    private String SlogonTxt ;

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getDealerAddress() {
        return DealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        DealerAddress = dealerAddress;
    }

    public String getPreTxt() {
        return PreTxt;
    }

    public void setPreTxt(String preTxt) {
        PreTxt = preTxt;
    }

    public int getDealerId() {
        return DealerId;
    }

    public void setDealerId(int dealerId) {
        DealerId = dealerId;
    }

    public String getSlogonTxt() {return SlogonTxt;}

    public void setSlogonTxt(String slogonTxt) {SlogonTxt = slogonTxt;}

    public String getProductIdentity() {
        return ProductIdentity;
    }

    public void setProductIdentity(String productIdentity) {
        ProductIdentity = productIdentity;
    }

    public int getTypeId() {return TypeId;}

    public void setTypeId(int typeId) {
        TypeId = typeId;}

    public int getActivityId() {return ActivityId;}

    public void setActivityId(int activityId) {ActivityId = activityId;}

    public String getActivityName() {return ActivityName;}

    public void setActivityName(String activityName) {ActivityName = activityName;}


    public DunMaiItem() {
    }

    public DunMaiItem(String productIdentity, int dealerId, int typeId, int activityId, String activityName, String preTxt, String dealerName, String dealerAddress, String slogonTxt) {
        ProductIdentity = productIdentity;
        DealerId = dealerId;
        TypeId = typeId;
        ActivityId = activityId;
        ActivityName = activityName;
        PreTxt = preTxt;
        DealerAddress = dealerAddress;
        DealerName = dealerName;
        SlogonTxt = slogonTxt;
    }
}
