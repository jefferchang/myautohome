package com.autohome.resadmin.common;

import com.google.common.base.Strings;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Created by zhouxiaoming on 2015/8/28.
 */
public class ParamsValid {
    private int returnCode;

    private String message;

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

    public ParamsValid ValidAppId(String appId) {
        if (returnCode != 0) {
            return this;
        }
        if (Strings.isNullOrEmpty(appId)) {
            returnCode = ParamErrorType.Miss_Appid.getValue();
            message = ParamErrorType.Miss_Appid.getName();
        }
        return this;
    }

    public ParamsValid ValidNotNull(String paramName, Object paramValue) {
        if (returnCode != 0) {
            return this;
        }
        if (paramValue == null) {
            returnCode = ParamErrorType.Miss_Require.getValue();
            message = ParamErrorType.Miss_Require.getName() + paramName;
        }
        return this;
    }

    public ParamsValid ValidNotNullOrEmpty(String paramName, String paramValue) {
        if (returnCode != 0) {
            return this;
        }
        if (Strings.isNullOrEmpty(paramValue)) {
            returnCode = ParamErrorType.Miss_Require.getValue();
            message = ParamErrorType.Miss_Require.getName() + paramName;
        }
        return this;
    }

    public ParamsValid ValidMoreThenZero(String paramName, Integer paramValue) {
        if (returnCode != 0) {
            return this;
        }
        if (paramValue == null || paramValue <= 0) {
            returnCode = ParamErrorType.Miss_Require.getValue();
            message = ParamErrorType.Miss_Require.getName() + paramName;
        }
        return this;
    }

    public ParamsValid Valid(String paramName, Supplier<Boolean> booleanSupplier) {
        if (returnCode != 0) {
            return this;
        }
        if (!booleanSupplier.get()) {
            returnCode = ParamErrorType.Miss_Require.getValue();
            message = ParamErrorType.Miss_Require.getName() + paramName;
        }
        return this;
    }

    public ParamsValid Valid(String paramName, Boolean aBoolean) {
        if (returnCode != 0) {
            return this;
        }
        if (!aBoolean) {
            returnCode = ParamErrorType.Miss_Require.getValue();
            message = ParamErrorType.Miss_Require.getName() + paramName;
        }
        return this;
    }

    public ParamsValid ValidTelNumber(String TelNumber) {
        if (returnCode != 0) {
            return this;
        }
        final String regexMobile = "^1\\d{10}$";
        final String regexPhone = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";
        if (TelNumber == null || !(Pattern.matches(regexMobile, TelNumber) || Pattern.matches(regexPhone, TelNumber))) {
            returnCode = ParamErrorType.Wrong_Format.getValue();
            message = ParamsValid.GetErrorMessageWhenWrongFormat("电话号码格式不正确");
        }
        return this;
    }




    public boolean IsValid() {
        return returnCode == 0 && Strings.isNullOrEmpty(message);
    }

    private static String GetErrorMessageWhenWrongFormat(String errorTipMsg) {
        return ParamErrorType.Wrong_Format.getName() + errorTipMsg;
    }
}