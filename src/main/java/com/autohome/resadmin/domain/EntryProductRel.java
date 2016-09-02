package com.autohome.resadmin.domain;

/**
 * Created by hujinliang on 2016/4/7.
 */
public class EntryProductRel {
    private String productIdentity;
    private int productOrder;
    private Integer showCompetitive;

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

    public int getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(int productOrder) {
        this.productOrder = productOrder;
    }

    private String template;
    public EntryProductRel(){}
    public EntryProductRel(String productIdentity, int productOrder, String template) {
        this.productIdentity = productIdentity;
        this.productOrder = productOrder;
        this.template = template;
    }

    public Integer getShowCompetitive() {
        return showCompetitive;
    }

    public void setShowCompetitive(Integer showCompetitive) {
        this.showCompetitive = showCompetitive;
    }
}
