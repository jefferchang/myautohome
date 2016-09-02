package com.autohome.resadmin.service.impl;

import com.autohome.resadmin.dao.*;
import com.autohome.resadmin.domain.*;
import com.autohome.resadmin.service.CompeteService;
import com.autohome.resadmin.service.ResService;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xw.luan on 2016/7/25.
 */
@Service
public class CompeteServiceImpl implements CompeteService {


    @Autowired
    private CompeteSeriesDAO competeSeriesDAO;
    @Autowired
    private CompeteSpecsDAO competeSpecsDAO;
    @Autowired
    ResService resService;
    @Autowired
    BrandsDAO brandsDAO;
    @Autowired
    ProductCityFactoryRelDAO productCityFactoryRelDAO;

    @Override
    public String findCompeteSeriesCstrBySeriesid(int seriesid) {
        Optional<String> seriesIds = competeSeriesDAO.findCompeteSeriesCstrBySeriesidCache(seriesid);
        return seriesIds.orNull();
    }

    @Override
    public String findCompeteSpecsCstrBySpecId(int specid) {
        Optional<String> spedIds = competeSpecsDAO.findCompeteSpecsCstrBySpecidCache(specid);
        return spedIds.orNull();
    }

    @Override
    public ResListResponse getCompeteById(List<String> identities, int cityId, String seriesIdStr, String specIdStr, EntryInfo ei, HttpServletRequest request, BaseAttribute attribute) throws SolrServerException, IOException {
        //按产品线匹配数据后投放竞品
        List<EntryProductRel> productList = ei.getProductList();
        for (int i = 0; i < productList.size(); i++) {
            int count = 0;
            if (productList.get(i).getShowCompetitive().equals(1)) {
                count = this.countProductCityFactory(cityId, seriesIdStr, productList.get(i).getProductIdentity());
            }
            if (count > 0) {
                identities = Lists.newArrayList(productList.get(i).getProductIdentity()); //entryProductRelList.stream().map(EntryProductRel::getProductIdentity).collect(Collectors.toList());//过滤之后的产品线
                //按车型查询竞品
                String specIdStrCompete="0";
                String seriesIdStrCompete="0";
                if (ei.getSolrIndexMap().get("SpecId")) {
                    specIdStrCompete = this.findCompeteSpecsCstrBySpecId(Ints.tryParse(specIdStr));
                    if (Strings.isNullOrEmpty(specIdStrCompete)) {
                        continue;
                    }
                }
                //按车系查询竞品
                if (ei.getSolrIndexMap().get("SeriesId")) {
                    seriesIdStrCompete = this.findCompeteSeriesCstrBySeriesid(Ints.tryParse(seriesIdStr));
                    if (Strings.isNullOrEmpty(seriesIdStrCompete)) {
                        continue;
                    }
                }
                //查询solr是只查询投放竞品的产品线
                List<ProductActivityInfo> activityInfoList = Lists.newArrayList();
                QueryResponse queryResponse = resService.getSolrResponse(seriesIdStrCompete, specIdStrCompete, cityId, ei, identities);
                if (queryResponse != null) {
                    activityInfoList = queryResponse.getBeans(ProductActivityInfo.class);
                } else {
                    continue;
                }
                List<EntryResult> resultList = resService.queryActivityByOrder(activityInfoList, identities, cityId, seriesIdStrCompete, specIdStrCompete, ei, request, attribute, 1);
                if (resultList.size() == 0) {
                    continue;
                }
                return new ResListResponse<>(0, "", new Result<>(resultList.size(), 1, 0, resultList));
            }
        }
        return new ResListResponse<>(1, "未找到素材", new Result<>(0, 1, 0, null));
    }

    @Override
    public ResListResponse getByProductIdentity(EntryInfo ei, List<ProductActivityInfo> activityInfoList, List<String> identities, int cityId, String seriesIdStr, String specIdStr, HttpServletRequest request, BaseAttribute attribute) throws SolrServerException, IOException {
        //按产品线投放竞品
        List<EntryResult> competeResultList = Lists.newArrayList();
        List<EntryProductRel> productList = ei.getProductList();
        for (int p = 0; p < productList.size(); p++) {
            String productIndentity = productList.get(p).getProductIdentity();
            java.util.Optional<ProductActivityInfo> productActivityInfo = activityInfoList.stream().filter((activityInfo) ->
                    productIndentity.equals(activityInfo.getProductIdentity())).findFirst();//取出是不是有相等productIdentity
            //有值，直接取主车系进行返回
            if (productActivityInfo.isPresent()) {
                competeResultList = resService.queryActivityByOrder(activityInfoList, identities, cityId, seriesIdStr, specIdStr, ei, request, attribute, 0);
                return new ResListResponse<>(0, "", new Result<>(competeResultList.size(), 1, 0, competeResultList));
            }
            if (productList.get(p).getShowCompetitive() != null && 1 == productList.get(p).getShowCompetitive().intValue()) { //取主车系竞品（showcompetitive = 1）
                String seriesIds = "0";
                String specIds = "0";
                int count = this.countProductCityFactory(cityId, seriesIdStr, productIndentity);
                if (count == 0) { //投放竞品 精准 到城市 厂商 产品线 。
                    continue;
                }
                if (ei.getSolrIndexMap() != null) {
                    if (ei.getSolrIndexMap().get("SeriesId")) {//车系
                        seriesIds = this.findCompeteSeriesCstrBySeriesid(Ints.tryParse(seriesIdStr));//拼接好的 竞品
                        if (Strings.isNullOrEmpty(seriesIds)) {//没有查询到竞品 如果直接调用 getSolrResponse 会返回所有数据 不符合业务
                            continue;
                        }
                    }
                    if (ei.getSolrIndexMap().get("SpecId")) {//车型
                        specIds = this.findCompeteSpecsCstrBySpecId(Ints.tryParse(specIdStr));//拼接好的 竞品
                        if (Strings.isNullOrEmpty(specIds)) {//没有查询到竞品 如果直接调用 getSolrResponse 会返回所有数据 不符合业务
                            continue;
                        }
                    }
                }
                QueryResponse competeResult = resService.getSolrResponse(seriesIds, specIds, cityId, ei, Lists.newArrayList(productIndentity));//竞品查询
                if (competeResult != null) {
                    List competeActivityInfoList = competeResult.getBeans(ProductActivityInfo.class);
                    if (competeActivityInfoList.size() > 0) {
                        competeResultList = resService.queryActivityByOrder(competeActivityInfoList, Lists.newArrayList(productIndentity), cityId, seriesIds, specIds,
                                ei, request, attribute, 1);//1 代表 通过车型 要查找对应的车系
                        return new ResListResponse<>(0, "", new Result<>(competeResultList.size(), 1, 0, competeResultList));
                    }
                }
            }
        }
        return new ResListResponse<>(1, "未找到素材", new Result<>(competeResultList.size(), 1, 0, competeResultList));
    }

    @Override
    public int countProductCityFactory(int cityId, String seriesIdStr, String productIndentity) {
        if (!Strings.isNullOrEmpty(seriesIdStr)) {
            String CFP = cityId + "," + seriesIdStr + "," + productIndentity;
            return productCityFactoryRelDAO.countProductCityFactoryCache(CFP).or(0);
        }
        return 0;
    }
}
