package com.autohome.resadmin.domain;

import java.util.List;

public class Result<T> {

    public final int rowcount;
    public final int pagecount;
    public final int pageindex;
    public final List<T> list;

    public Result(int rowcount, int pagecount, int pageindex, List<T> speclist) {
        this.rowcount = rowcount;
        this.pagecount = pagecount;
        this.pageindex = pageindex;
        this.list = speclist;
    }
}

