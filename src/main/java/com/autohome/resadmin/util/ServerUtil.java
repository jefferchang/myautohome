package com.autohome.resadmin.util;

import com.alibaba.fastjson.JSON;
import com.autohome.resadmin.domain.EntryInfo;
import com.autohome.resadmin.domain.ProductActivityInfo;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * 业务类型转化
 */
public class ServerUtil {



    /**
     *     JSON转化
     * @param seriesId
     * @param specId
     * @param cityId
     * @param ei
     * @param productActivityInfo
     * @param resultJson
     * @return
     * @throws IOException
     */
    public static String parseServerJson(int seriesId, int specId, Integer cityId, EntryInfo ei, ProductActivityInfo productActivityInfo, String resultJson) throws IOException {
        Map<String, Object> resmap = (Map<String, Object>) JSON.parse(resultJson);
        resmap.put("cityId", cityId);
        resmap.put("seriesId", seriesId);
        resmap.put("specId", specId);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustacheTemplate = mf.compile(new StringReader(ei.getProductMap().get(productActivityInfo.getProductIdentity())), "template");
        StringWriter writer = new StringWriter();
        mustacheTemplate.execute(writer, resmap).flush();
        resultJson = writer.toString();
        return resultJson;
    }
}
