package com.autohome.resadmin.common;

/**
 * Created by zhouxiaoming on 2015/9/7.
 */
public enum ParamErrorType {


    Miss_Require(101, "缺少必要的请求参数:"),
    Wrong_Format(102, "请求参数格式错误:"),
    Miss_Appid(103, "缺少参数_appId");

    private final String name;

    private final int value;

    ParamErrorType(int value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
