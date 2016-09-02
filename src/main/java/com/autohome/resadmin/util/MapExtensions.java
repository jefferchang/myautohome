package com.autohome.resadmin.util;

import java.util.Map;

/**
 * Created by zhouxiaoming on 2015/9/14.
 * map 扩展类
 */
public final class MapExtensions {

    private MapExtensions(){
        // Utility classes should always be final and have a private constructor
    }
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }
}
