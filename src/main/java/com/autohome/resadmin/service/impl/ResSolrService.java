package com.autohome.resadmin.service.impl;

import com.autohome.resadmin.dao.SpecSeriesRelationDAO;
import com.autohome.resadmin.domain.*;
import com.autohome.resadmin.service.ResService;
import com.autohome.resadmin.util.ServerUtil;
import com.autohome.resadmin.util.SolrConst;
import com.autohome.resadmin.util.SolrFilterQueriesHelper;
import com.autohome.resadmin.util.StringExtensions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class ResSolrService implements ResService {
    private static final Logger dataLogger = LoggerFactory.getLogger("dealerResRequestLogger");
    @Autowired
    HttpSolrServer resSolrServer;
    @Autowired
    String accessLoggerVersion;
    @Autowired
    private SpecSeriesRelationDAO specSeriesRelationDAO;

    @Override
    public QueryResponse getSolrResponse(String seriesIdstr, String specIdStr, Integer cityId, EntryInfo ei, List<String> identities) throws SolrServerException {
        String entryTypeName = ei.getEntryTypeName();//当前产品位对应的终端
        SolrQuery solrQuery = new SolrQuery(SolrConst.Q_DEFALUT_VALUE);
        solrQuery.setFields("RawJson", "ProductIdentity","ActivityIdentity","SeriesId","SpecId");

//        HashMap<String, String> fieldValueMap = new HashMap<>();
//        if (ei.getSolrIndexMap().get("SpecId")) {
//            fieldValueMap.put("SpecId", String.valueOf(specId));
//        } else {
//            fieldValueMap.put("SpecId", String.valueOf(0));
//        }
//        if (ei.getSolrIndexMap().get("SeriesId")) {
//            fieldValueMap.put("SeriesId", String.valueOf(seriesId));
//        } else {
//            fieldValueMap.put("SeriesId", String.valueOf(0));
//        }
        ArrayList<String> cityIdList = Lists.newArrayList("0");//TODO:0即代表未勾选也代表全国,会把未勾选城市维度的查出来，但是最大量也就是产品线的个数
        if (ei.getSolrIndexMap().get("CityId")) {
            cityIdList.add(String.valueOf(cityId));
        }
        //String fieldQueryString = SolrFilterQueriesHelper.getAndQueryString(fieldValueMap);
        String entryTypeQueryString = SolrFilterQueriesHelper.getOrQueryString("EntryType", Lists.newArrayList(entryTypeName, "ALL"));
        String productQueryString = SolrFilterQueriesHelper.getOrQueryString("ProductIdentity", identities);
        String cityQueryString = SolrFilterQueriesHelper.getOrQueryString("CityId", cityIdList);
        String seriesQueryString=ei.getSolrIndexMap().get("SeriesId")?SolrFilterQueriesHelper.getOrQueryString("SeriesId", Splitter.on(',').trimResults().omitEmptyStrings().splitToList(seriesIdstr)):SolrFilterQueriesHelper.getFieldQueryString("SeriesId","0");
        String specQueryString=ei.getSolrIndexMap().get("SpecId")?SolrFilterQueriesHelper.getOrQueryString("SpecId", Splitter.on(',').trimResults().omitEmptyStrings().splitToList(specIdStr)):SolrFilterQueriesHelper.getFieldQueryString("SpecId","0");

        String fieldQueryString = SolrFilterQueriesHelper.getJoinQueryStrings(entryTypeQueryString, productQueryString,cityQueryString,seriesQueryString,specQueryString);
        solrQuery.setFilterQueries(fieldQueryString);
        solrQuery.setRows(100);
        return resSolrServer.query(solrQuery);
    }


    /**
     * 排序转化
     * @param activityInfoList
     * @param identities
     * @param cityId
     * @param seriesIdStr
     * @param specIdStr
     * @param ei
     * @param request
     * @param attribute
     * @param isCompeteQuery
     * @return
     * @throws IOException
     */
    public List<EntryResult> queryActivityByOrder(List<ProductActivityInfo> activityInfoList, List<String> identities, int cityId, String seriesIdStr, String specIdStr, EntryInfo ei, HttpServletRequest request, BaseAttribute attribute, int isCompeteQuery) throws IOException {
        List<EntryResult> resultList = Lists.newArrayList();
        List<Integer> multiList = Lists.newArrayList();
        Boolean multiSeries = false;
        Boolean multiSpec = false;
        Map<Integer, List<ProductActivityInfo>> groupActivitys = Maps.newHashMap();//TODO:没有处理只选cityId的
        if (ei.getSolrIndexMap().get("SpecId")) {
            multiSpec = true;
            groupActivitys = activityInfoList.stream().collect(Collectors.groupingBy(ProductActivityInfo::getSpecId));
            multiList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(specIdStr).stream().map(Integer::parseInt).collect(Collectors.toList());
        }
        if (ei.getSolrIndexMap().get("SeriesId")) {
            multiSeries = true;
            groupActivitys = activityInfoList.stream().collect(Collectors.groupingBy(ProductActivityInfo::getSeriesId));
            multiList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(seriesIdStr).stream().map(Integer::parseInt).collect(Collectors.toList());
        }
        if (multiList.size() == 0) {
            throw new IllegalArgumentException("产品位未设置关联车型或车系");
        }
        for (Integer splitKey : multiList) {
            String accessId=attribute.getRequestId();
            if (Strings.isNullOrEmpty(accessId)) {
                accessId = UUID.randomUUID().toString();
            }
            if (groupActivitys.containsKey(splitKey)) {
                if(isCompeteQuery == 1 && multiSpec == true){
                    SpecSeriesRelation specSeriesRelation = specSeriesRelationDAO.findSpecSeriesRelationByCache(splitKey).orNull();
                    if(specSeriesRelation != null ){
                        seriesIdStr = String.valueOf(specSeriesRelation.getSeriesId());
                    }
                }
                List<ProductActivityInfo> productActivityInfos = groupActivitys.get(splitKey);
                for (ProductActivityInfo item : productActivityInfos) {
                    item.setProductOrder(identities.indexOf(item.getProductIdentity()));
                }
                Collections.sort(productActivityInfos);
                ProductActivityInfo productActivityInfo = productActivityInfos.get(0);
                String resultJson = productActivityInfo.getRawJson();
                if (ei.isUseServerTemplate()) {
                    resultJson = ServerUtil.parseServerJson(multiSeries ? splitKey : Integer.parseInt(seriesIdStr), multiSpec ? splitKey : Integer.parseInt(specIdStr), cityId, ei, productActivityInfo, resultJson);
                }
                String activityIdentity = productActivityInfo.getActivityIdentity();
                List<String> activityIdentityInfo = Splitter.on('-').trimResults().omitEmptyStrings().splitToList(activityIdentity);
                if (activityIdentityInfo.size() == 3) {
                    activityIdentity = activityIdentityInfo.get(1);
                }

                if (attribute.isNeedlog()) {
                    dataLogger.warn(StringExtensions.getAccessLog(accessLoggerVersion, Long.toString(Instant.now().toEpochMilli()), StringExtensions.getIp(request), attribute.getReferUrl(), attribute.getSessionId(), attribute.getSessionVid(), attribute.getPvId(), accessId, Integer.toString(attribute.getResId()), productActivityInfo.getProductIdentity(), Integer.toString(cityId), multiSeries ? String.valueOf(splitKey) : seriesIdStr, multiSpec ? String.valueOf(splitKey) : specIdStr, ei.getEntryTypeName(), activityIdentity,String.valueOf(isCompeteQuery)));
                }
                resultList.add(new EntryResult(ei.getProductMap().get(productActivityInfo.getProductIdentity()), resultJson, productActivityInfo.getProductIdentity(), multiSeries ? splitKey : Integer.parseInt(seriesIdStr), multiSpec ? splitKey : Integer.parseInt(specIdStr), activityIdentity, accessId));
            } else {
                if (attribute.isNeedlog()) {//TODO：现在记录多条导致左连接算的数订单据多，更换为只记录一条，就算多个车系车型实际也是一次请求，但是导致按车系车型计算时数据不正确，明天讨论
                    dataLogger.warn(StringExtensions.getAccessLog(accessLoggerVersion, Long.toString(Instant.now().toEpochMilli()), StringExtensions.getIp(request), attribute.getReferUrl(), attribute.getSessionId(), attribute.getSessionVid(), attribute.getPvId(), accessId, Integer.toString(attribute.getResId()), "NONE", Integer.toString(cityId), multiSeries ? String.valueOf(splitKey) : seriesIdStr, multiSpec ? String.valueOf(splitKey) : specIdStr, ei.getEntryTypeName(), "0",String.valueOf(isCompeteQuery)));
                }
            }
        }
        return resultList;
    }


}
