package com.autohome.resadmin.util;


/**
 * Created by zhouxiaoming on 2015/9/8.
 * Solr常量类
 */
public class SolrConst {
    /**
     * Solr查询最大行数
     */
    public final static int MAX_QUERY_ROWS = Integer.MAX_VALUE;

    /**
     * Solr冗余查询字段名称
     */
    public final static String REBUND_QUERY_FIELD_NAME = "1";

    /**
     * Solr默认查询字段q=*:*
     */
    public final static String Q_DEFALUT_VALUE = "*:*";

    public final static String OPERATORS_AND = " AND ";

    public final static String OPERATORS_OR = " OR ";

    public final static String OPERATORS_NOT_IN = "-";

}
