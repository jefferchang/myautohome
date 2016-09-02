package com.autohome.resadmin.controller;


import com.alibaba.fastjson.JSON;
import com.autohome.resadmin.util.SolrFilterQueriesHelper;
import com.autohome.resadmin.util.StringExtensions;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JmustacheTest {

    @Test
    public void GetQueryString(){
      String str=  SolrFilterQueriesHelper.getOrQueryString("SpecId", Splitter.on(',').trimResults().omitEmptyStrings().splitToList(""));
        String fieldQueryString = SolrFilterQueriesHelper.getJoinQueryStrings(str);
        System.out.println(fieldQueryString);
    }
    @Test
    public void GetJson_With_Template() {
        String expected="{\"车系ID\": 3185,\"车型ID\": 23036,\"车型名称\": \"2015款 1.4T 手动两驱尊贵型\",\"优惠\": 9000.0,\"最高价\": 136800,\"最低价\": 136800,\"经销商ID\": 2021630,\"车系名称\": \"锋驭\",\"公司简称\": \"汉中市瑞铃\",\"公司\": \"汉中瑞铃汽车销售服务有限公司\",\"图片\": \"~/car/upload/2015/6/10/20150610135643552264110.jpg\",\"订单地址\": \"http://hjk.m.autohome.com.cn/dealer/order/2021630/3185/23036?cityid=610700\",\"城市ID\": 610700,\"任务ID\": 3725}";
        String rawJson="{\"SeriesId\":3185,\"SpecId\":23036,\"SpecName\":\"2015款 1.4T 手动两驱尊贵型\",\"DiscountPrice\":9000.0,\"MaxPrice\":136800,\"MinPrice\":136800,\"DealerId\":2021630,\"SeriesName\":\"锋驭\",\"CompanySimple\":\"汉中市瑞铃\",\"Company\":\"汉中瑞铃汽车销售服务有限公司\",\"ImgUrl\":\"~/car/upload/2015/6/10/20150610135643552264110.jpg\",\"OrderUrl\":\"http://hjk.m.autohome.com.cn/dealer/order/2021630/3185/23036?cityid=610700\",\"CityId\":610700,\"CollectorId\":3725}";
        String template="{\"车系ID\": {{SeriesId}},\"车型ID\": {{SpecId}},\"车型名称\": \"{{SpecName}}\",\"优惠\": {{DiscountPrice}},\"最高价\": {{MaxPrice}},\"最低价\": {{MinPrice}},\"经销商ID\": {{DealerId}},\"车系名称\": \"{{SeriesName}}\",\"公司简称\": \"{{CompanySimple}}\",\"公司\": \"{{Company}}\",\"图片\": \"{{ImgUrl}}\",\"订单地址\": \"{{OrderUrl}}\",\"城市ID\": {{CityId}},\"任务ID\": {{CollectorId}}}";

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustacheTemplate = mf.compile(new StringReader(template), "template");
        Map<String, Object> resmap = (Map<String, Object>) JSON.parse(rawJson);
        StringWriter writer = new StringWriter();
        try {
            mustacheTemplate.execute(writer, resmap).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rawJson = writer.toString();
        System.out.println(rawJson);
        assertEquals(expected, rawJson);
    }
}
