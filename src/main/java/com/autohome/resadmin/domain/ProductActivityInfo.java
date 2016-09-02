package com.autohome.resadmin.domain;

import com.google.common.base.Objects;
import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by zhouxiaoming on 2015/9/5.
 */
public class ProductActivityInfo implements Comparable<ProductActivityInfo> {

    private int ProductOrder;
    @Field
    private Integer SeriesId;
    @Field
    private Integer SpecId;
    @Field
    private String RawJson;
    @Field
    private String ActivityIdentity;
    @Field
    private String ProductIdentity;
    public ProductActivityInfo(){}
    public ProductActivityInfo(String rawJson, int productOrder,String productIdentity,String activityIdentity,int seriesId,int specId) {
        this.setProductIdentity(productIdentity);
        this.setProductOrder(productOrder);
        this.setRawJson(rawJson);
        this.setActivityIdentity(activityIdentity);
        this.setSeriesId(seriesId);
        this.setSpecId(specId);
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ProductActivityInfo other = (ProductActivityInfo) obj;
        return Objects.equal(this.getProductIdentity(), other.getProductIdentity()) && Objects.equal(this.getRawJson(), other.getRawJson())&& Objects.equal(this.getActivityIdentity(), other.getActivityIdentity());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getProductIdentity(), this.getRawJson(),this.getActivityIdentity());

    }
    public String getActivityIdentity() {
        return ActivityIdentity;
    }
    public void setActivityIdentity(String activityIdentity) {
        ActivityIdentity = activityIdentity;
    }
    public String getProductIdentity() {
        return ProductIdentity;
    }

    public void setProductIdentity(String productIdentity) {
        ProductIdentity = productIdentity;
    }

    public Integer getSpecId() {return SpecId;}

    public void setSpecId(Integer specId) {SpecId = specId;}

    public Integer getSeriesId() {return SeriesId;}

    public void setSeriesId(Integer seriesId) {SeriesId = seriesId;}

    public String getRawJson() {
        return RawJson;
    }

    public void setRawJson(String rawJson) {
        RawJson = rawJson;
    }

    public int getProductOrder() {
        return ProductOrder;
    }

    public void setProductOrder(int productOrder) {
        ProductOrder = productOrder;
    }

    @Override
    public int compareTo(ProductActivityInfo o) {
        return getProductOrder()-o.getProductOrder();
    }
}