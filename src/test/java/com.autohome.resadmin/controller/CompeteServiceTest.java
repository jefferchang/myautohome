package com.autohome.resadmin.controller;
import com.autohome.resadmin.dao.CompeteSeriesDAO;
import com.autohome.resadmin.dao.CompeteSpecsDAO;
import com.autohome.resadmin.dao.ProductCityFactoryRelDAO;
import com.autohome.resadmin.dao.SpecSeriesRelationDAO;
import com.autohome.resadmin.domain.*;
import com.autohome.resadmin.service.CompeteService;
import com.autohome.resadmin.service.ResService;
import com.autohome.resadmin.service.impl.CompeteServiceImpl;
import com.autohome.resadmin.service.impl.ResSolrService;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CompeteServiceTest{


    @Mock
    private CompeteService competeService;

    @Mock
    private ResService resService;
    @Mock
    private CompeteSeriesDAO competeSeriesDAO;
    @Mock
    private CompeteSpecsDAO competeSpecsDAO;
    @Mock
    private SpecSeriesRelationDAO specSeriesRelationDAO;
    @Mock
    ProductCityFactoryRelDAO productCityFactoryRelDAO;

    EntryInfo ei;
    List<ProductActivityInfo> activityInfoList;
    @Mock
    HttpServletRequest request = null;

    BaseAttribute attribute = null;
    List<String> identities = null;

    @Before
    public void initData() throws IOException, SolrServerException {
        ei = new EntryInfo();
        activityInfoList = Lists.newArrayList();
        EntryProductRel entryProductRel1 = new EntryProductRel();
        entryProductRel1.setProductIdentity("CSH");
        entryProductRel1.setShowCompetitive(1);
        EntryProductRel entryProductRel2 = new EntryProductRel();
        entryProductRel2.setProductIdentity("TGH");
        entryProductRel2.setShowCompetitive(1);
        EntryProductRel entryProductRel4 = new EntryProductRel();
        entryProductRel4.setProductIdentity("JKHAUTOSHOW");
        entryProductRel4.setShowCompetitive(1);
        //产品线
        List<EntryProductRel> prs = Lists.<EntryProductRel>newArrayList
                (entryProductRel1, entryProductRel2, entryProductRel4);
        ei.setProductList(prs);
        Map productMap =new HashMap<String,String>();
        productMap.put("CSH","<div>品牌专区1</div>");
        productMap.put("TGH","<div>品牌专区2</div");
        productMap.put("JKHAUTOSHOW","<div>品牌专区3</div");
        ei.setProductMap(productMap);
        request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");
        attribute = new BaseAttribute();
        attribute.setResId(10003);
        attribute.setPvId("1470186545006YJrkCfX9");
        attribute.setRequestId(null);
        attribute.setSessionId("");
        attribute.setSessionVid("");
        attribute.setReferUrl("http://localhost:8080/");
        attribute.setNeedlog(true);
        resService = Mockito.mock(ResSolrService.class);
        competeSeriesDAO = Mockito.mock(CompeteSeriesDAO.class);
        competeSpecsDAO = Mockito.mock(CompeteSpecsDAO.class);
        specSeriesRelationDAO = Mockito.mock(SpecSeriesRelationDAO.class);
        productCityFactoryRelDAO = Mockito.mock(ProductCityFactoryRelDAO.class);
        identities = ei.getProductList().stream().map(EntryProductRel::getProductIdentity).collect(Collectors.toList());//当前产品位关联的产品线\


        SpecSeriesRelation specSeriesRelation = new SpecSeriesRelation();
        specSeriesRelation.setSeriesId(1006);
        Mockito.when(competeSeriesDAO.findCompeteSeriesCstrBySeriesidCache(871)).thenReturn(Optional.fromNullable("1006,2342,5421,4231"));
        Mockito.when(competeSpecsDAO.findCompeteSpecsCstrBySpecidCache(3547)).thenReturn(Optional.fromNullable("3452,3944"));
        Mockito.when(competeSpecsDAO.findCompeteSpecsCstrBySpecidCache(1934)).thenReturn(Optional.fromNullable("3541,3422,3941"));
        Mockito.when(specSeriesRelationDAO.findSpecSeriesRelationByCache(3452)).thenReturn(Optional.fromNullable(specSeriesRelation));
        Mockito.when(specSeriesRelationDAO.findSpecSeriesRelationByCache(3944)).thenReturn(Optional.fromNullable(specSeriesRelation));
        Mockito.when(specSeriesRelationDAO.findSpecSeriesRelationByCache(3547)).thenReturn(Optional.fromNullable(specSeriesRelation));

        Mockito.when(productCityFactoryRelDAO.countProductCityFactoryCache("110100,871,TGH")).thenReturn(Optional.fromNullable(1));
        Mockito.when(productCityFactoryRelDAO.countProductCityFactoryCache("110100,871,CSH")).thenReturn(Optional.fromNullable(1));
        Mockito.when(productCityFactoryRelDAO.countProductCityFactoryCache("110100,871,JKHAUTOSHOW")).thenReturn(Optional.fromNullable(1));

        competeService = new CompeteServiceImpl();
        ReflectionTestUtils.setField(competeService, "resService", resService);
        ReflectionTestUtils.setField(competeService, "competeSeriesDAO", competeSeriesDAO);
        ReflectionTestUtils.setField(competeService, "competeSpecsDAO", competeSpecsDAO);
        ReflectionTestUtils.setField(resService, "specSeriesRelationDAO", specSeriesRelationDAO);
        ReflectionTestUtils.setField(competeService, "productCityFactoryRelDAO", productCityFactoryRelDAO);
        //   ReflectionTestUtils.setField(competeService, "specSeriesRelationDAO", specSeriesRelationDAO);
    }


    @Test //主车系有值 ei.showCompete =1
    public void EI_showCompete_Series_Has_Value() throws IOException, SolrServerException {
        Map solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SeriesId",true);
        solrIndexMap.put("SpecId",false);
        ei.setSolrIndexMap(solrIndexMap);


        QueryResponse response = new QueryResponse();
        NamedList<Object> naList = new NamedList<Object>();
        SolrDocument solrDocument1 =new SolrDocument();
        solrDocument1.put("ProductIdentity","JKHYGJ");
        solrDocument1.put("SeriesId",1006);
        solrDocument1.put("ActivityIdentity","JKHYGJ");
        solrDocument1.put("RawJson","rawJson");
        SolrDocument  solrDocument2 =new SolrDocument();
        solrDocument2.put("ProductIdentity","JKHYGJ");
        solrDocument2.put("SeriesId",2342);
        solrDocument2.put("ActivityIdentity","JKHYGJ");
        solrDocument2.put("RawJson","rawJson");
        SolrDocumentList listinfo =new SolrDocumentList();
        listinfo.add(solrDocument1);
        listinfo.add(solrDocument2);
        naList.add("response",listinfo);
        response.setResponse(naList);
        Mockito.when(resService.getSolrResponse("1006,2342,5421,4231", "0", 110100, ei, Lists.newArrayList("JKHAUTOSHOW"))).thenReturn(response);




        ProductActivityInfo info1 = new ProductActivityInfo();
        info1.setProductIdentity("TGH");
        info1.setSeriesId(871);
        info1.setActivityIdentity("TGH");
        activityInfoList.add(info1);
        ProductActivityInfo info2 = new ProductActivityInfo();
        info2.setProductIdentity("JKHYGJ");
        info2.setActivityIdentity("JKHYGJ");
        info2.setSeriesId(871);
        activityInfoList.add(info2);
        Mockito.when(resService.queryActivityByOrder(activityInfoList, identities, 110100, "871", "0", ei, request, attribute, 0))
                .thenCallRealMethod();
        ResListResponse resListResponse = competeService.getByProductIdentity(ei, activityInfoList, identities, 110100, "871", "0",
                request, attribute);
        List<EntryResult> er = resListResponse.result.list;
        Assert.assertEquals("871", er.get(0).getSeriesId().toString());
    }


    @Test //竞品车系有值 (主车系没有值) 产品线 showCompete = 1
    public void EI_showCompete_JingPin_Series_Has_Value() throws IOException, SolrServerException {
        Map solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SeriesId",true);
        solrIndexMap.put("SpecId",false);
        ei.setSolrIndexMap(solrIndexMap);

        QueryResponse response = new QueryResponse();
        NamedList<Object> naList = new NamedList<Object>();
        SolrDocument solrDocument1 =new SolrDocument();
        solrDocument1.put("ProductIdentity","JKHYGJ");
        solrDocument1.put("SeriesId",1006);
        solrDocument1.put("ActivityIdentity","JKHYGJ");
        solrDocument1.put("RawJson","rawJson");
        SolrDocument  solrDocument2 =new SolrDocument();
        solrDocument2.put("ProductIdentity","JKHYGJ");
        solrDocument2.put("SeriesId",2342);
        solrDocument2.put("ActivityIdentity","JKHYGJ");
        solrDocument2.put("RawJson","rawJson");
        SolrDocumentList listinfo =new SolrDocumentList();
        listinfo.add(solrDocument1);
        listinfo.add(solrDocument2);
        naList.add("response",listinfo);
        response.setResponse(naList);
        Mockito.when(resService.getSolrResponse("1006,2342,5421,4231", "0", 110100, ei, Lists.newArrayList("JKHAUTOSHOW"))).thenReturn(response);



        ProductActivityInfo activityInfo = new  ProductActivityInfo();
        activityInfo.setRawJson("rawJson");
        activityInfo.setSeriesId(1006);
        activityInfo.setProductIdentity("JKHYGJ");
        activityInfo.setActivityIdentity("JKHYGJ");
        ProductActivityInfo activityInfo1 = new  ProductActivityInfo();
        activityInfo1.setRawJson("rawJson");
        activityInfo1.setSeriesId(2342);
        activityInfo1.setProductIdentity("JKHYGJ");
        activityInfo1.setActivityIdentity("JKHYGJ");
        activityInfoList.add(activityInfo);
        activityInfoList.add(activityInfo1);
        Mockito.when(resService.queryActivityByOrder(activityInfoList, Lists.newArrayList("JKHAUTOSHOW"), 110100,
                "1006,2342,5421,4231", "0", ei, request, attribute, 1)).thenCallRealMethod();
        ResListResponse resListResponse = competeService.getByProductIdentity(ei, activityInfoList, identities, 110100, "871", "0",
                request, attribute);

        List<EntryResult> er = resListResponse.result.list;
        Assert.assertEquals("1006", er.get(0).getSeriesId().toString());
    }


    @Test //主车型有值 ei.showCompete =1
    public void EI_showCompete_Spec_Has_Value() throws IOException, SolrServerException {
        Map solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SeriesId",false);
        solrIndexMap.put("SpecId",true);
        ei.setSolrIndexMap(solrIndexMap);

        ProductActivityInfo info1 = new ProductActivityInfo();
        info1.setProductIdentity("TGH");
        info1.setSeriesId(871);
        info1.setSpecId(1934);
        info1.setActivityIdentity("TGH");
        activityInfoList.add(info1);
        ProductActivityInfo info2 = new ProductActivityInfo();
        info2.setProductIdentity("JKHYGJ");
        info2.setActivityIdentity("JKHYGJ");
        info2.setSeriesId(871);
        info2.setSpecId(1935);
        activityInfoList.add(info2);
        Mockito.when(resService.queryActivityByOrder(activityInfoList, identities, 110100, "871", "1934", ei, request, attribute, 0))
                .thenCallRealMethod();
        ResListResponse resListResponse = competeService.getByProductIdentity(ei, activityInfoList, identities, 110100, "871", "1934",
                request, attribute);
        List<EntryResult> er = resListResponse.result.list;
        Assert.assertEquals("871", er.get(0).getSeriesId().toString());
    }


    @Test //竞品车型有值 (主车型没有值) 产品线 showCompete = 1
    public void EI_showCompete_JingPin_Spec_Has_Value() throws IOException, SolrServerException {
        Map solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SeriesId",false);
        solrIndexMap.put("SpecId",true);
        ei.setSolrIndexMap(solrIndexMap);

        QueryResponse response = new QueryResponse();
        NamedList<Object> naList = new NamedList<Object>();
        SolrDocument solrDocument1 =new SolrDocument();
        solrDocument1.put("ProductIdentity","JKHYGJ");
        solrDocument1.put("SeriesId",1006);
        solrDocument1.put("ActivityIdentity","JKHYGJ");
        solrDocument1.put("RawJson","rawJson");
        solrDocument1.put("SpecId",3944);
        SolrDocument  solrDocument2 =new SolrDocument();
        solrDocument2.put("ProductIdentity","JKHYGJ");
        solrDocument2.put("SeriesId",1006);
        solrDocument2.put("ActivityIdentity","JKHYGJ");
        solrDocument2.put("RawJson","rawJson");
        solrDocument2.put("SpecId",3452);
        SolrDocumentList listinfo =new SolrDocumentList();
        listinfo.add(solrDocument1);
        listinfo.add(solrDocument2);
        naList.add("response",listinfo);
        response.setResponse(naList);
        Mockito.when(resService.getSolrResponse("0", "3452,3944", 110100, ei, Lists.newArrayList("JKHAUTOSHOW"))).thenReturn(response);


        ProductActivityInfo activityInfo = new  ProductActivityInfo();
        activityInfo.setRawJson("rawJson");
        activityInfo.setSeriesId(1006);
        activityInfo.setSpecId(3549);
        activityInfo.setProductIdentity("JKHYGJ");
        activityInfo.setActivityIdentity("JKHYGJ");
        ProductActivityInfo activityInfo1 = new  ProductActivityInfo();
        activityInfo1.setRawJson("rawJson");
        activityInfo1.setSeriesId(1006);
        activityInfo1.setSpecId(3548);
        activityInfo1.setProductIdentity("JKHYGJ");
        activityInfo1.setActivityIdentity("JKHYGJ");
        activityInfoList.add(activityInfo);
        activityInfoList.add(activityInfo1);
        Mockito.when(resService.queryActivityByOrder(activityInfoList, Lists.newArrayList("JKHAUTOSHOW"), 110100,
                "0", "3452,3944", ei, request, attribute, 1)).thenCallRealMethod();
        ResListResponse resListResponse = competeService.getByProductIdentity(ei, activityInfoList, identities, 110100, "871", "3547",
                request, attribute);
        List<EntryResult> er = resListResponse.result.list;
        Assert.assertEquals("1006", er.get(0).getSeriesId().toString());
    }





    /**
     * 按车型查竞品案例(查询已经存在的竞品)
     *    查询竞品车型：1000001
     *    返回竞品车型：16276
     * @throws Exception
     */
    @Test
    public void setCompeteBySpecId_Has_Value()throws Exception{
        //设置查1000001车型时返回结果集
        String SpecIdStr = "1000027,1000027,1002000,1000023,1002094,1000027,1001939,1002184,1001186,16276";
        Mockito.when( competeSpecsDAO.findCompeteSpecsCstrBySpecidCache(1000001)).thenReturn(Optional.fromNullable(SpecIdStr));
        //构造数据
        Map<String,Boolean> solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SpecId",true);
        solrIndexMap.put("SeriesId",false);
        solrIndexMap.put("CityId",true);
        List solrIndexList=new ArrayList();
        EntryIndexRel entryIndexRel1=new EntryIndexRel();
        entryIndexRel1.setIndexName("CityId");
        entryIndexRel1.setIsSel(true);
        EntryIndexRel entryIndexRel2=new EntryIndexRel();
        entryIndexRel2.setIndexName("SeriesId");
        entryIndexRel2.setIsSel(false);
        EntryIndexRel entryIndexRel3=new EntryIndexRel();
        entryIndexRel3.setIndexName("SpecId");
        entryIndexRel3.setIsSel(true);
        solrIndexList.add(entryIndexRel1);
        solrIndexList.add(entryIndexRel2);
        solrIndexList.add(entryIndexRel3);
        List productList=new ArrayList();
        EntryProductRel EntryProductRel1 = new EntryProductRel();
        EntryProductRel1.setProductIdentity("JKHYGJ");
        EntryProductRel1.setProductOrder(1);
        EntryProductRel1.setShowCompetitive(1);
        EntryProductRel1.setTemplate("<div class=\"uibox-title uibox-title-border\"><span><a href=\"http://hjk.autohome.com.cn/autoshow/Home/Index?cityid={{cityId}}&pvareaid=104479\">品牌专区</a><a class=\"more\" href=\"http://hjk.autohome.com.cn/autoshow/Home/Index?cityid={{cityId}}&pvareaid=104479\">查看更多&gt;&gt;</a></span></div><div class=\"uibox-con\"><div class=\"brandzone\"><div class=\"brandzone-pic\"><a href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\" target=\"_blank\"><img width=\"120\" height=\"90\" src=\"{{imagePath}}s_{{imageName}}\"></a></div><div class=\"brandzone-cont\"><div class=\"cont-title\"><a href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\">{{SeriesName}} 购车钜惠</a><a class=\"orange-link\" href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\" class=\"orange-link\">[车展活动]</a></div><div class=\"cont-main\"><div class=\"main-left\"><p class=\"price\">优惠：<span class=\"red\">{{shortTitle}}</span></p><p><a  class=\"grey-link\" href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\" target=\"_blank\">{{longTitle}}</a></p></div><div class=\"main-right\"><p class=\"price\"><span class=\"red\">抽红包直抵车款<br/>车展优惠等你来</span></p></div></div></div></div></div>");
        productList.add(EntryProductRel1);
        HashMap productMap = new HashMap();
        productMap.put("JKHYGJ" ,"<div class=\"uibox-title uibox-title-border\"><span><a href=\"http://hjk.autohome.com.cn/autoshow/Home/Index?cityid={{cityId}}&pvareaid=104479\">品牌专区</a><a class=\"more\" href=\"http://hjk.autohome.com.cn/autoshow/Home/Index?cityid={{cityId}}&pvareaid=104479\">查看更多&gt;&gt;</a></span></div><div class=\"uibox-con\"><div class=\"brandzone\"><div class=\"brandzone-pic\"><a href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\" target=\"_blank\"><img width=\"120\" height=\"90\" src=\"{{imagePath}}s_{{imageName}}\"></a></div><div class=\"brandzone-cont\"><div class=\"cont-title\"><a href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\">{{SeriesName}} 购车钜惠</a><a class=\"orange-link\" href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\" class=\"orange-link\">[车展活动]</a></div><div class=\"cont-main\"><div class=\"main-left\"><p class=\"price\">优惠：<span class=\"red\">{{shortTitle}}</span></p><p><a  class=\"grey-link\" href=\"http://hjk.autohome.com.cn/autoshow/SpecDetails/Index?activityId={{collectorId}}&specId={{specId}}&cityId={{cityId}}&pvareaid=104479\" target=\"_blank\">{{longTitle}}</a></p></div><div class=\"main-right\"><p class=\"price\"><span class=\"red\">抽红包直抵车款<br/>车展优惠等你来</span></p></div></div></div></div></div>\"");
        EntryInfo ei= new EntryInfo();
        ei.setEntryName("PC车型综述页品牌专区");
        ei.setEntryTypeId(101);
        ei.setResId(10004);
        ei.setShowCompetitive(2);
        ei.setId(21);
        ei.setEntryTypeName("PC");
        ei.setRendTemplate(true);
        ei.setUseServerTemplate(false);
        ei.setSolrIndexList(solrIndexList);
        ei.setSolrIndexMap(solrIndexMap);
        ei.setProductList(productList);
        ei.setProductMap(productMap);
        List<String> identities = ei.getProductList().stream().map(EntryProductRel::getProductIdentity).collect(Collectors.toList());
        QueryResponse queryResponse = new QueryResponse();
        SolrDocumentList SolrDocumentList = new SolrDocumentList();
        SolrDocument solrDocument = new SolrDocument();
        solrDocument.setField("ProductIdentity","JKHYGJ");
        solrDocument.setField("ActivityIdentity","JKHYGJ-8-SpecId16276");
        solrDocument.setField("ActivityIdentity","JKHYGJ-8-SpecId16276");
        solrDocument.setField("SeriesId",0);
        solrDocument.setField("SpecId",16276);
        solrDocument.setField("RawJson","{\"collectorId\":8,\"seriesId\":620,\"SeriesName\":\"smart fortwo\",\"SpecName\":\"2013款 1.0T 硬顶激情版\",\"shortTitle\":\"13\",\"longTitle\":\"13\",\"specId\":16276,\"price\":\"13万元\",\"imagePath\":\"http://car0.autoimg.cn/upload/2013/6/17/\",\"imageName\":\"201306171715171604178.jpg\"}");
        SolrDocumentList.add(solrDocument);
        SimpleOrderedMap ss = new SimpleOrderedMap();
        ss.add("response",SolrDocumentList);
        queryResponse.setResponse( ss);
        attribute.setResId(10004);
        activityInfoList = queryResponse.getBeans(ProductActivityInfo.class);
        Mockito.when( resService.queryActivityByOrder(activityInfoList, identities, 110100, "0", SpecIdStr, ei, request, attribute, 1) ).thenCallRealMethod();
        Mockito.when(resService.getSolrResponse("0","1000027,1000027,1002000,1000023,1002094,1000027,1001939,1002184,1001186,16276",110100,ei,identities)).thenReturn( queryResponse);
        SpecSeriesRelation SpecSeriesRelation =new SpecSeriesRelation();
        SpecSeriesRelation.setSeriesId(620);
        Mockito.when(specSeriesRelationDAO.findSpecSeriesRelationByCache(16276)).thenReturn(Optional.fromNullable(SpecSeriesRelation));

        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,0,JKHYGJ")).thenReturn(Optional.fromNullable(1));
        //查询
        ResListResponse resListResponse = competeService.getCompeteById(identities ,110100, "0", "1000001", ei, request, attribute );
        List<EntryResult> er = resListResponse.result.list;
        Assert.assertEquals("16276", er.get(0).getSpecId().toString());
    }

    /**
     * 按车型查询竞品 （查询不存在的竞品）
     *
     * @throws Exception
     */
    @Test
    public void setCompeteBySpecId_Not_Has_Value()throws Exception{
        Mockito.when( competeSpecsDAO.findCompeteSpecsCstrBySpecidCache(1000001)).thenReturn(Optional.fromNullable(""));
        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,0,CSH")).thenReturn(Optional.fromNullable(1));
        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,0,TGH")).thenReturn(Optional.fromNullable(1));
        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,0,JKHAUTOSHOW")).thenReturn(Optional.fromNullable(1));
        Map<String,Boolean> solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SpecId",true);
        solrIndexMap.put("SeriesId",false);
        solrIndexMap.put("CityId",true);
        ei.setSolrIndexMap(solrIndexMap);
        //查询
        ResListResponse resListResponse = competeService.getCompeteById(identities ,110100, "0", "1000001", ei, request, attribute );
        Assert.assertEquals("未找到素材",resListResponse.message);
    }

    /**
     * 按车系查询竞品 （查询不存在的竞品）
     *
     * @throws Exception
     */
    @Test
    public void setCompeteBySeriesId_Not_Has_Value()throws Exception{
        Mockito.when( competeSeriesDAO.findCompeteSeriesCstrBySeriesidCache(123)).thenReturn(Optional.fromNullable(""));
        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,123,CSH")).thenReturn(Optional.fromNullable(1));
        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,123,TGH")).thenReturn(Optional.fromNullable(1));
        Mockito.when( productCityFactoryRelDAO.countProductCityFactoryCache("110100,123,JKHAUTOSHOW")).thenReturn(Optional.fromNullable(1));
        Map<String,Boolean> solrIndexMap =new HashMap<String,Boolean>();
        solrIndexMap.put("SpecId",false);
        solrIndexMap.put("SeriesId",true);
        solrIndexMap.put("CityId",true);
        ei.setSolrIndexMap(solrIndexMap);
        //查询
        ResListResponse resListResponse = competeService.getCompeteById(identities ,110100, "123", "0", ei, request, attribute );
        Assert.assertEquals("未找到素材",resListResponse.message);
    }
}
