package com.autohome.resadmin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autohome.resadmin.domain.CompeteSpecs;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public final class StringExtensions {
    private StringExtensions() {
        // Utility classes should always be final and have a private constructor
    }

    public static String getAccessLog(String... values) {
        Joiner joiner = Joiner.on("\\t").skipNulls();
       // return loggerName + "|" + joiner.join(values) + "\n";
        return  joiner.join(values) + "\n";

    }

    public static String getIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-PROXY-CLIENT-IP");
        }
        if (ip == null || ip.length() == 0 || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.contains(ip, ",")) { //TODO: 如果包含多个,返回第一个ip ，ngxin情况应该取最后一个
            String[] iplist = ip.split(",");
            ip = iplist[iplist.length - 1];
        }
        return ip;
    }
    public static Map<String, String> getCookieMap(String cookiestr) {
        Map<String, String> cookielist = new HashMap<String, String>();
        if (!Strings.isNullOrEmpty(cookiestr)) {
            String[] rawCookieParams = cookiestr.split(";");
            for (String rawCookieNameAndValue : rawCookieParams) {
                String[] rawCookieNameAndValuePair = rawCookieNameAndValue.split("=");
                if (rawCookieNameAndValuePair.length == 2) {
                    cookielist.put(rawCookieNameAndValuePair[0].trim(), rawCookieNameAndValuePair[1].trim());
                }
            }
        }
        return cookielist;
    }
    public static String getCookie(String name, Map<String, String> cookieList) throws UnsupportedEncodingException {
        String cookieVal="";
        if (cookieList.containsKey(name)) {
            cookieVal = URLDecoder.decode(cookieList.get(name), "UTF-8");
            if (!Strings.isNullOrEmpty(cookieVal) && cookieVal.contains("||")) {
                cookieVal = cookieVal.substring(0, cookieVal.indexOf("||"));
            }
        }
        return cookieVal;
    }

}

