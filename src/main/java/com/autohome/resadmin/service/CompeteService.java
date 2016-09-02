package com.autohome.resadmin.service;

import com.autohome.resadmin.domain.*;
import org.apache.solr.client.solrj.SolrServerException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by xw.luan on 2016/7/25.
 */
public interface CompeteService {



    /**
     *
     * @param seriesid
     * @return
     */
    public String  findCompeteSeriesCstrBySeriesid(int seriesid);
    /**
     *
     * @param specid
     * @return
     */
    public String  findCompeteSpecsCstrBySpecId(int specid);



    /**
     * 按车型或车系投放竞品
     *
     * @param seriesIdStr
     * @param specIdStr
     * @return
     */
    public ResListResponse getCompeteById(List<String> identities, int cityId, String seriesIdStr, String specIdStr, EntryInfo ei, HttpServletRequest request,BaseAttribute attribute) throws SolrServerException, IOException;


    /**
     * 按产品线投放竞品
     *
     */
    public ResListResponse getByProductIdentity(EntryInfo ei, List<ProductActivityInfo> activityInfoList, List<String> identities, int cityId, String seriesIdStr, String specIdStr, HttpServletRequest request, BaseAttribute attribute ) throws SolrServerException, IOException;

    /**
     *
     * @param cityId
     * @param seriesIdStr
     * @param productIndentity
     * @return
     */
    public int countProductCityFactory(int cityId,String seriesIdStr,String productIndentity);
}
