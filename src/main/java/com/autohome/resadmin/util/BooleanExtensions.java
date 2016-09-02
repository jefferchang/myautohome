package com.autohome.resadmin.util;

/**
 * Created by zhouxiaoming on 2015/9/14.
 */
public final class BooleanExtensions {

    private BooleanExtensions(){
        // Utility classes should always be final and have a private constructor
    }

    public static boolean getBoolean(Boolean aBoolean, boolean defaultValue) {
        if(aBoolean == null){
            return defaultValue;
        }else{
            return aBoolean;
        }
    }
}
