package com.autohome.resadmin.service;

import com.autohome.resadmin.domain.BaseAttribute;
import com.autohome.resadmin.domain.EntryInfo;
import com.autohome.resadmin.domain.EntryResult;
import com.autohome.resadmin.domain.ProductActivityInfo;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by hujinliang on 2016/6/22.
 */
public interface ResService {

    QueryResponse getSolrResponse(String seriesIdStr, String specIdStr, Integer cityId, EntryInfo ei, List<String> identities) throws SolrServerException;

    public List<EntryResult> queryActivityByOrder(List<ProductActivityInfo> activityInfoList, List<String> identities, int cityId, String seriesIdStr, String specIdStr, EntryInfo ei, HttpServletRequest request, BaseAttribute attribute, int isCompeteQuery) throws IOException;

}
