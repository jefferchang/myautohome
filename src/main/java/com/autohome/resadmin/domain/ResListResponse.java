package com.autohome.resadmin.domain;

public class ResListResponse<T> {

    public final Integer returnCode;
    public final String message;
    public final  Result<T> result;

    public ResListResponse(Integer returnCode, String message, Result<T> result) {
        this.returnCode = returnCode;
        this.message = message;
        this.result = result;
    }
}
