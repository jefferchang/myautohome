package com.autohome.resadmin.controller;

import com.autohome.resadmin.dao.EntryInfoDAO;
import com.autohome.resadmin.domain.EntryInfo;
import com.autohome.resadmin.util.StringExtensions;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Long.F on 2016/4/27.
 */
@Controller
@RequestMapping("/log")
public class TransitController {
    private static final Logger dataLogger = LoggerFactory.getLogger("dealerResClickRequestLogger");
    private static final Logger errorLogger = LoggerFactory.getLogger("resAdminErrorLogger");
    @Autowired
    String accessClickLoggerName;
    @Autowired
    String accessLoggerVersion;
    @Autowired
    public EntryInfoDAO entryInfoDAO;

    @RequestMapping(value = "/v1/click", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String serverLog(@RequestParam("url") String url, @RequestParam("access_id") String access_id, @RequestParam(value = "resid", required = false) Integer resid, @RequestParam(value = "pvid", required = false) String pvid, @RequestParam(value = "ref", required = false) String ref, @RequestParam(value = "cur", required = false) String cur, @RequestParam(value = "site", required = false) String site, @RequestParam(value = "category", required = false) String category, @RequestParam(value = "subcategory", required = false) String subcategory, @RequestParam(value = "object", required = false) String object, HttpServletRequest request) {
        try {
            String entryTypeName = "";
            if (resid !=null) {
                EntryInfo ei = entryInfoDAO.findByResIdCache(resid).orNull();
                if (ei != null) {
                    entryTypeName = ei.getEntryTypeName();
                }
            }
            String monId = UUID.randomUUID().toString();
            pvid = MoreObjects.firstNonNull(pvid, "");
            ref = MoreObjects.firstNonNull(ref, "");
            cur = MoreObjects.firstNonNull(cur, "");
            site = MoreObjects.firstNonNull(site, "0");
            category = MoreObjects.firstNonNull(category, "0");
            subcategory = MoreObjects.firstNonNull(subcategory, "0");
            object = MoreObjects.firstNonNull(object, "0");
            Map<String, String> cookieList = StringExtensions.getCookieMap(request.getHeader("Cookie"));
            String sessionId = StringExtensions.getCookie("sessionid", cookieList);
            url = java.net.URLDecoder.decode(url, "UTF-8");
            sessionId = Strings.nullToEmpty(sessionId);
            if (!url.contains("tel:")) {
                if (!url.contains("?")) {
                    url = url + "?1=1";
                }
                url = url + "&scene_type=dealer_cpw&show_id=" + access_id;
            }
            ref = java.net.URLDecoder.decode(ref, "UTF-8");
            cur = java.net.URLDecoder.decode(cur, "UTF-8");
            if (!Strings.isNullOrEmpty(sessionId) && sessionId.contains("||")) {
                sessionId = sessionId.substring(0, sessionId.indexOf("||"));
            }
            dataLogger.warn(StringExtensions.getAccessLog(monId, sessionId, site, category, subcategory, object, pvid, access_id, ref, cur, url, entryTypeName));
        } catch (Exception e) {
            errorLogger.error("Transit", e);
        }
        //TODO 不兼容汉字 跳转后会显示??
        return String.format("redirect:%s", url);
    }
}
