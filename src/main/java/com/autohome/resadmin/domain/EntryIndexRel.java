package com.autohome.resadmin.domain;

/**
 * Created by hujinliang on 2016/4/7.
 */
public class EntryIndexRel {
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public boolean getIsSel() {
        return isSel;
    }

    public void setIsSel(boolean isSel) {
        this.isSel = isSel;
    }

    private String indexName;
    private boolean isSel;
    public EntryIndexRel(){}
    public EntryIndexRel(String indexName, boolean isSel) {
        this.indexName = indexName;
        this.isSel = isSel;
    }
}
