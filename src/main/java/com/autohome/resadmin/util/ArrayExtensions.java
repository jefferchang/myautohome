package com.autohome.resadmin.util;

/**
 * Created by zhouxiaoming on 2015/9/9.
 *
 */
public final class ArrayExtensions {
    private ArrayExtensions(){
        // Utility classes should always be final and have a private constructor
    }
    public static <T> boolean isEmpty(T... array) {
        return array == null || array.length == 0;
    }
    public static <T> boolean isNotEmpty(T... array) {
        return !isEmpty(array);
    }
}
