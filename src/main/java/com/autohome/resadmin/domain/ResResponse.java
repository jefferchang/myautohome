package com.autohome.resadmin.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujinliang on 2016/4/8.
 */
public class ResResponse {
    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EntryResult getResult() {
        return result;
    }

    public void setResult(EntryResult result) {
        this.result = result;
    }

    public ResResponse(int returnCode, String message, EntryResult result) {
        this.returnCode = returnCode;
        this.message = message;
        this.result = result;
    }

    private int returnCode;
    private String message;
    private EntryResult result;

    }



