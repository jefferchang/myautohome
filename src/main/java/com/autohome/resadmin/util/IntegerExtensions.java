package com.autohome.resadmin.util;

/**
 * Created by zhouxiaoming on 2015/9/14.
 */
public final class IntegerExtensions {

    private IntegerExtensions(){
        // Utility classes should always be final and have a private constructor
    }

    public static boolean isMoreThanZero(Integer integer) {
        return integer != null && integer.intValue() > 0 ? true : false;
    }

    public static int getIntValue(Integer integer, int defaultValue) {
        if (integer == null) {
            return defaultValue;
        } else {
            return integer.intValue();
        }
    }
}
