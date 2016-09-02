package com.autohome.resadmin.util;

import com.autohome.resadmin.util.ArrayExtensions;
import com.autohome.resadmin.util.CollectionExtensions;
import com.autohome.resadmin.util.MapExtensions;
import com.google.common.base.Joiner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhouxiaoming on 2015/9/8.
 */
public final class SolrFilterQueriesHelper {

    private SolrFilterQueriesHelper(){
        // Utility classes should always be final and have a private constructor
    }

    /**
     * 获取相同字段的Or关系查询字符串
     *
     * @param fieldName 字段名称
     * @param filedValues    Or中的值集合
     * @return
     */
    public static String getOrQueryString(String fieldName, Collection<String> filedValues) {
        String result = null;
        if (CollectionExtensions.isNotEmpty(filedValues)) {
            List<String> fieldValuesList = filedValues.stream().map(value -> getFieldQueryString(fieldName, value)).collect(Collectors.toList());
            if (fieldValuesList.size() == 1) {
                result = fieldValuesList.get(0);
            } else {
                Joiner joiner = Joiner.on(SolrConst.OPERATORS_OR).skipNulls();
                result = "(" + joiner.join(fieldValuesList) + ")";
            }
        }
        return result;
    }

    /**
     * 获取不同字段的And关系查询字符串
     *
     * @param fieldValueMap 字段及对应的值Hash表
     * @return
     */
    public static String getAndQueryString(Map<String, String> fieldValueMap) {
        String result = null;
        if (MapExtensions.isNotEmpty(fieldValueMap)) {
            List<String> fieldValuesList = new ArrayList<>();
            Iterator iter = fieldValueMap.keySet().iterator();
            while (iter.hasNext()) {
                String fieldName = (String) iter.next();
                String value = fieldValueMap.get(fieldName);
                fieldValuesList.add(getFieldQueryString(fieldName, value));
            }
            Joiner joiner = Joiner.on(SolrConst.OPERATORS_AND).skipNulls();
            result = joiner.join(fieldValuesList);
        }
        return result;
    }

    /**
     * 获取一个字段的查询字符串
     *
     * @param fieldName
     * @param value
     * @return
     */
    public static String getFieldQueryString(String fieldName, String value) {
        return fieldName + ":" + value;
    }

    /**
     * 获取不同字段的NotIn关系查询字符串
     *
     * @param fieldName 字段名称
     * @param values    Or中的值集合
     * @return
     */
    public static String getNotInQueryString(String fieldName, Collection<String> values) {
        String result = null;
        if (CollectionExtensions.isNotEmpty(values)) {
            List<String> fieldValuesList = values.stream().map(v -> getNotInQueryString(fieldName, v)).collect(Collectors.toList());
            if (fieldValuesList.size() == 1) {
                result = fieldValuesList.get(0);
            } else {
                Joiner joiner = Joiner.on(SolrConst.OPERATORS_AND).skipNulls();
                result = "(" + joiner.join(fieldValuesList) + ")";
            }
        }
        return result;
    }

    /**
     * 获取不同字段的NotIn关系查询字符串
     *
     * @param fieldNameValueMap
     * @return
     */
    public static String getNotInQueryString(Map<String, String> fieldNameValueMap) {
        String result = null;
        if (MapExtensions.isNotEmpty(fieldNameValueMap)) {
            List<String> fieldValuesList = new ArrayList<String>();
            Iterator iter = fieldNameValueMap.keySet().iterator();
            while (iter.hasNext()) {
                String fieldName = (String) iter.next();
                String value = fieldNameValueMap.get(fieldName);
                fieldValuesList.add(getNotInQueryString(fieldName, value));
            }
            Joiner joiner = Joiner.on(SolrConst.OPERATORS_AND).skipNulls();
            result = joiner.join(fieldValuesList);
        }
        return result;
    }

    /**
     * 获取一个字段的NotIn关系查询字符串
     *
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public static String getNotInQueryString(String fieldName, String fieldValue) {
        return SolrConst.OPERATORS_NOT_IN + fieldName + ":" + fieldValue;
    }

    /**
     * 用AND拼接多个fq查询字符串
     *
     * @param queryStrings
     * @return
     */
    public static String getJoinQueryStrings(String... queryStrings) {
        String queryString = null;
        if (ArrayExtensions.isNotEmpty(queryStrings)) {
            Joiner joiner = Joiner.on(SolrConst.OPERATORS_AND).skipNulls();
            queryString = joiner.join(queryStrings);
        }
        return queryString;
    }


    public static int getStartRowNum(int pageIndex, int pageSize) {
        if (pageIndex > 1) {
            return (pageIndex - 1) * pageSize;
        } else {
            return 0;
        }
    }
}
