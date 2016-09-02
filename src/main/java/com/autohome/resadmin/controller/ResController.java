package com.autohome.resadmin.controller;

import com.alibaba.fastjson.JSON;
import com.autohome.resadmin.dao.EntryInfoDAO;
import com.autohome.resadmin.domain.*;
import com.autohome.resadmin.service.CompeteService;
import com.autohome.resadmin.service.DealerInfoService;
import com.autohome.resadmin.service.ResService;
import com.autohome.resadmin.util.StringExtensions;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.timgroup.statsd.StatsDClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Res")
public class ResController {
    private static final Logger dataLogger = LoggerFactory.getLogger("dealerResRequestLogger");
    private static final Logger datadunmaiLogger = LoggerFactory.getLogger("dealerResDunMaiRequestLogger");

    @Autowired
    String accessClickLoggerName;
    @Autowired
    String accessLoggerName;
    @Autowired
    String accessLoggerVersion;
    @Autowired
    StatsDClient statsdClient;
    @Autowired
    public EntryInfoDAO entryInfoDAO;
    @Autowired
    ResService resService;
    @Autowired
    DealerInfoService dealerInfoService;
    @Autowired
    private CompeteService competeService;

    @RequestMapping(value = "/v1/Get", method = RequestMethod.GET)
    public ResResponse GetRes(@RequestParam(value = "pvId", required = false) String pvId, @RequestParam("resId") int resId, @RequestParam("cityId") String cityIdStr, @RequestParam("seriesId") int seriesId, @RequestParam("specId") int specId, @RequestParam(value = "requestId",required = false) String requestId, @RequestParam(value = "sessionId", required = false) String sessionIdApp, @RequestParam(value = "sessionVid", required = false) String sessionVidApp, @RequestParam(value = "needLog", required = false) Boolean needLog, HttpServletRequest request) throws SolrServerException, IOException {
        ResListResponse<EntryResult> resListResponse = GetResMulti(pvId, resId, cityIdStr, String.valueOf(seriesId), String.valueOf(specId), requestId, sessionIdApp, sessionVidApp, needLog, request);
        return new ResResponse(resListResponse.returnCode, resListResponse.message, resListResponse.returnCode == 0 ? resListResponse.result.list.get(0) : new EntryResult("", "{}", "", 0, 0, "", ""));
    }

    @RequestMapping(value = "/v1/GetDunMaiRes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResListResponse<DunMaiItem> GetDunMaiRes(@RequestParam("cityId") int cityId, @RequestParam("seriesId") int seriesId, @RequestParam("specId") int specId, @RequestParam("requestId") String requestId, @RequestParam(value = "sessionId", required = false) String sessionIdApp, @RequestParam(value = "sessionVid", required = false) String sessionVidApp, HttpServletRequest request) throws SolrServerException, IOException {
        Map<String, String> cookieList = StringExtensions.getCookieMap(request.getHeader("Cookie"));
        String sessionId = MoreObjects.firstNonNull(sessionIdApp, StringExtensions.getCookie("sessionid", cookieList));
        String sessionvId = MoreObjects.firstNonNull(sessionVidApp, StringExtensions.getCookie("sessionvid", cookieList));
        String showid = UUID.randomUUID().toString();
        List<DunMaiItem> dunMaiItemList = Lists.newArrayList();
        DunMaiItem firstmodel = GetDunMaiItem(10014, cityId, seriesId, specId, request);
        DunMaiItem secondmodel = GetDunMaiItem(10015, cityId, seriesId, specId, request);
        if (firstmodel != null) {
            dunMaiItemList.add(firstmodel);
        }
        if (secondmodel != null) {
            dunMaiItemList.add(secondmodel);
        }
        ResListResponse<DealerItem> dealerItems = dealerInfoService.getDealersByCityAndSpec(cityId, specId, "resadmin");
        if (secondmodel != null) {
            dealerItems.result.list.removeIf(p -> p.getDealerId() == secondmodel.getDealerId());
        }
        if (Strings.isNullOrEmpty(dealerItems.message)) {
            for (int i = 0; i < dealerItems.result.list.size(); i++) {
                DealerItem dealerItem = dealerItems.result.list.get(i);
                dunMaiItemList.add(new DunMaiItem("DEALER", dealerItem.getDealerId(), 0, dealerItem.getDealerId(), "", dealerItem.getKindId() == 1 ? "4S店" : "综合", dealerItem.getDealerName(), "地址:" + dealerItem.getDealerAddress(), ""));
            }
        }
        dunMaiItemList = dunMaiItemList.stream().limit(6).collect(Collectors.toList());
        Integer row = 1;
        for (DunMaiItem item : dunMaiItemList) {
            datadunmaiLogger.warn(StringExtensions.getAccessLog(accessLoggerVersion, requestId, Long.toString(Instant.now().toEpochMilli()), showid, "M", sessionId, sessionvId, item.getProductIdentity(), String.format("%d,1", row++), Integer.toString(item.getActivityId()), "v1"));
        }
        return new ResListResponse<>(0, "", new Result<>(dunMaiItemList.size(), 1, 1, dunMaiItemList));
    }

    private DunMaiItem GetDunMaiItem(int resId, int cityId, int seriesId, int specId, HttpServletRequest request) throws IOException, SolrServerException {
        DunMaiItem dunMaiItem = null;
        ResResponse jsonResult = GetRes("dunmai", resId, String.valueOf(cityId), seriesId, specId, "", "", "", false, request);
        if (jsonResult.getReturnCode() == 0 && !Strings.isNullOrEmpty(jsonResult.getResult().getRawJson())) {
            dunMaiItem = JSON.parseObject(jsonResult.getResult().getRawJson(), DunMaiItem.class);
        }
        return dunMaiItem;
    }


    @RequestMapping(value = "/v1/GetBySpecList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResListResponse<SpecItem> GetResBySpecList(@RequestParam("resId") int resId, @RequestParam("cityId") int cityId, @RequestParam("seriesId") String seriesId, @RequestParam("specIdList") String specIdList, @RequestParam(value = "requestId",required = false) String requestId, @RequestParam(value = "sessionId") String sessionIdApp, HttpServletRequest request) throws SolrServerException, IOException {
        List<SpecItem> specItemList = Lists.newArrayList();
        List<String> specList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(specIdList);
        if (specList.size() == 0) {
            return new ResListResponse<>(0, "", new Result<>(specItemList.size(), 1, 1, specItemList));
        }
        Map<Integer, List<EntryResult>> entryResultList = Maps.newHashMap();
        ResListResponse<EntryResult> resListResponse = GetResMulti("MainAppPic", resId, String.valueOf(cityId), seriesId, specIdList, requestId, sessionIdApp, "", true, request);
        if (resListResponse.returnCode == 0) {
            entryResultList = resListResponse.result.list.stream().collect(Collectors.groupingBy(EntryResult::getSpecId));
            for (String specStr : specList) {
                Integer specId = Integer.parseInt(specStr);
                if (entryResultList.containsKey(specId)) {
                    SpecItem specItem = JSON.parseObject(entryResultList.get(specId).get(0).getRawJson(), SpecItem.class);
                    specItem.setAccessId(entryResultList.get(specId).get(0).getAccessId());
                    specItemList.add(specItem);
                } else {
                    specItemList.add(new SpecItem(specId, "", "", ""));
                }
            }
            return new ResListResponse<>(0, "", new Result<>(specItemList.size(), 1, 1, specItemList));
        } else {
            return new ResListResponse<>(1, resListResponse.message, new Result<>(specItemList.size(), 1, 1, specItemList));
        }
    }




    @RequestMapping(value = "/v1/GetResMulti", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResListResponse<EntryResult> GetResMulti(@RequestParam(value = "pvId", required = false) String pvId, @RequestParam("resId") int resId, @RequestParam("cityId") String cityIdStr, @RequestParam("seriesId") String seriesIdStr, @RequestParam("specId") String specIdStr, @RequestParam(value = "requestId",required = false) String requestId, @RequestParam(value = "sessionId", required = false) String sessionIdApp, @RequestParam(value = "sessionVid", required = false) String sessionVidApp, @RequestParam(value = "needLog", required = false) Boolean needLog, HttpServletRequest request) throws SolrServerException, IOException {
        //传入多个车系或车型时只支持多个车系或者一个车系地下的多个车型
        if (Strings.isNullOrEmpty(pvId)) {
            statsdClient.incrementCounter("View.NoPvId." + Integer.toString(resId));
            return new ResListResponse<>(1, "pvid不能为空", null);
        }
        if (Strings.isNullOrEmpty(specIdStr) && Strings.isNullOrEmpty(seriesIdStr)) {
            return new ResListResponse<>(1, "车型和车系不能同时为空", null);
        }
        statsdClient.incrementCounter("View." + Integer.toString(resId));
        needLog = needLog != null ? needLog : true;
        String referUrl = com.google.common.base.Optional.fromNullable(request.getHeader("Referer")).or("");
        Map<String, String> cookieList = StringExtensions.getCookieMap(request.getHeader("Cookie"));
        String sessionId = MoreObjects.firstNonNull(sessionIdApp, StringExtensions.getCookie("sessionid", cookieList));
        String sessionVid = MoreObjects.firstNonNull(sessionVidApp, StringExtensions.getCookie("sessionvid", cookieList));
        Integer cityId = com.google.common.base.Optional.fromNullable(Ints.tryParse(cityIdStr)).or(0);
        BaseAttribute attribute = convertBean( resId,  pvId,  requestId,  sessionId,  sessionVid,  referUrl,  needLog);
        EntryInfo ei = entryInfoDAO.findByResIdCache(resId).orNull();
        if (ei == null) {
            return new ResListResponse<>(1, "未找到产品位", null);
        }
        List<String> identities = ei.getProductList().stream().map(EntryProductRel::getProductIdentity).collect(Collectors.toList());//当前产品位关联的产品线
        List<ProductActivityInfo> activityInfoList = Lists.newArrayList();
        QueryResponse result = resService.getSolrResponse(seriesIdStr, specIdStr, cityId, ei, identities);
        if (result != null) {
            activityInfoList = result.getBeans(ProductActivityInfo.class);
        } else {
            return new ResListResponse<>(1, "查询Solr失败", null);
        }

        if(ei.getShowCompetitive() != null && 1 == ei.getShowCompetitive().intValue()) {
            return competeService.getByProductIdentity(ei,activityInfoList,identities,cityId,seriesIdStr, specIdStr,request,attribute );

        }
        List<EntryResult> resultList = resService.queryActivityByOrder(activityInfoList, identities, cityId, seriesIdStr, specIdStr, ei, request,attribute,0);
        if (resultList.size() == 0) {
            if(ei.getShowCompetitive()!=null && 2==ei.getShowCompetitive().intValue())  {//按车系或车型投放竞品
                return competeService.getCompeteById(identities, cityId, seriesIdStr, specIdStr, ei, request,attribute);
            }
            return new ResListResponse<>(1, "未找到素材", new Result<>(resultList.size(), 1, 0, resultList));
        }
        return new ResListResponse<>(0, "", new Result<>(resultList.size(), 1, 0, resultList));
    }


    public BaseAttribute convertBean(Integer resId, String pvId, String requestId, String sessionId, String sessionVid, String referUrl, boolean needlog){
        BaseAttribute attribute = new BaseAttribute();
        attribute.setResId(resId);
        attribute.setPvId(pvId);
        attribute.setRequestId(requestId);
        attribute.setSessionId(sessionId);
        attribute.setSessionVid(sessionVid);
        attribute.setReferUrl(referUrl);
        attribute.setNeedlog(needlog);
        return attribute;
    }




}
