package com.autohome.resadmin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * Created by zhouxiaoming on 2015/9/4.
 * 利用fastjon序列化和反序列化
 */
public final class JsonHelper {

    final static String defaultDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private JsonHelper(){
        // Utility classes should always be final and have a private constructor
    }

    public static <T> String Serialize(T t) {
        JSON.DEFFAULT_DATE_FORMAT = defaultDateFormat;
        return Serialize(t, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    public static <T> String Serialize(T t, SerializerFeature... features) {
        return JSON.toJSONString(t, features);
    }

    public static <T> T DeSerialize(String string, Class<T> tClass) {
        return JSON.parseObject(string, tClass);
    }

    public static<T> List<T> DeSerializeList(String string, Class<T> tClass) {
        return JSON.parseArray(string, tClass);
    }
}
