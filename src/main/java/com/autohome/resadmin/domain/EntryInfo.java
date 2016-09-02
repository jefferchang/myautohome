package com.autohome.resadmin.domain;


import java.util.List;
import java.util.Map;

/**
 * Created by hujinliang on 2016/3/29.
 */

public class EntryInfo {

    private String entryName;
    private int entryTypeId;
    private int resId;
    private Integer showCompetitive;


    private int id;
    private String entryTypeName;
    private boolean rendTemplate;
    private boolean useServerTemplate;
    private List<EntryIndexRel> solrIndexList;
    private List<EntryProductRel> productList;
    private Map<String,String> productMap;
    private Map<String,Boolean> solrIndexMap;
    public EntryInfo(){}
    public EntryInfo(List<EntryProductRel> productList, String entryName, int entryTypeId,int id, int pvId, String entryTypeName, boolean rendTemplate,boolean useServerTemplate, List<EntryIndexRel> solrIndexList) {
        this.productList = productList;
        this.entryName = entryName;
        this.entryTypeId = entryTypeId;
        this.resId = pvId;
        this.id=id;
        this.entryTypeName = entryTypeName;
        this.rendTemplate = rendTemplate;
        this.useServerTemplate=useServerTemplate;
        this.solrIndexList = solrIndexList;
    }


    public Map<String, Boolean> getSolrIndexMap() {
        return solrIndexMap;
    }
    public void setSolrIndexMap(Map<String, Boolean> solrIndexMap) {
        this.solrIndexMap = solrIndexMap;
    }

    public Map<String, String> getProductMap() {
        return productMap;
    }
    public void setProductMap(Map<String, String> productMap) {
        this.productMap = productMap;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public int getEntryTypeId() {
        return entryTypeId;
    }
    public void setEntryTypeId(int entryTypeId) {
        this.entryTypeId = entryTypeId;
    }

    public String getEntryTypeName() {
        return entryTypeName;
    }
    public void setEntryTypeName(String entryTypeName) {
        this.entryTypeName = entryTypeName;
    }

    public boolean getRendTemplate() {
        return rendTemplate;
    }

    public boolean isUseServerTemplate() {
        return useServerTemplate;
    }

    public void setUseServerTemplate(boolean useServerTemplate) {
        this.useServerTemplate = useServerTemplate;
    }

    public void setRendTemplate(boolean rendTemplate) {
        this.rendTemplate = rendTemplate;
    }
    public int getResId() {
        return resId;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }


    @Override
    public String toString() {
        return   "entryTypeId= "+ entryTypeId + ", entryName= "+ entryName + ", entryTypeName= "+ entryTypeName +"]";
    }

    public List<EntryIndexRel> getSolrIndexList() {
        return solrIndexList;
    }

    public void setSolrIndexList(List<EntryIndexRel> solrIndexList) {
        this.solrIndexList = solrIndexList;
    }

    public List<EntryProductRel> getProductList() {
        return productList;
    }

    public void setProductList(List<EntryProductRel> productList) {
        this.productList = productList;
    }


    public Integer getShowCompetitive() {
        return showCompetitive;
    }

    public void setShowCompetitive(Integer showCompetitive) {
        this.showCompetitive = showCompetitive;
    }

}
