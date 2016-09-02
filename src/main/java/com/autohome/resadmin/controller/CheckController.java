package com.autohome.resadmin.controller;


import com.autohome.resadmin.domain.ResListResponse;
import com.autohome.resadmin.domain.Result;
import com.autohome.resadmin.util.GuavaCacheManager;
import com.autohome.resadmin.util.SolrConst;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;


/**
 * Created by Long.F on 2016/5/9.
 */
@Controller
@RequestMapping("/Check")
public class CheckController {

    @Autowired
    HttpSolrServer resSolrServer;

    @RequestMapping(value = "")
    public String checkResponseIndex() {
        return "ChkResponse";
    }

    /**
     * 测试IP:Port响应
     *
     * @return
     */
    @RequestMapping("requestTest")
    @ResponseBody
    public String requestTest() {
        return "success";
    }

    /**
     * 验证服务器实例返回
     * @param ip
     * @param port
     * @param url
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get/{ip}/{port}", produces = "application/json;charset=utf-8")
    public Object getDeployInstanceStatus(@PathVariable String ip, @PathVariable String port, @RequestParam(value = "url", defaultValue = "") String url) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        map.put("returnCode", 0);
        String[] ips = ip.split(",");
        String[] ports = port.split(",");
        for (String host : ips) {
            for (String pt : ports) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s:%s</td>", host, pt));
                sb.append(String.format("<td>%s</td>", getRequest(host, pt, url)));
                sb.append("</tr>");
            }
        }
        map.put("result", sb.toString());
        return map;
    }

    /**
     * 活动标识模糊匹配查看缓存中的数据量
     */
    @ResponseBody
    @RequestMapping(value = "/v1/{ActivityIdentity}", method = RequestMethod.GET)
    public String getCacheNums(@PathVariable String ActivityIdentity) throws SolrServerException, IOException {

        String fmt = "{\"ActivityIdentity\":\"%s\",\"numFound\":%d}";

        SolrQuery solrQuery = new SolrQuery(SolrConst.Q_DEFALUT_VALUE);

        String fieldQueryString = String.format("ActivityIdentity:%s*", ActivityIdentity);

        solrQuery.setFilterQueries(fieldQueryString);

        solrQuery.setRows(0);

        QueryResponse result = resSolrServer.query(solrQuery);

        return String.format(fmt, ActivityIdentity, result.getResults().getNumFound());
    }

    /**
     * getRequest
     *
     * @param ip
     * @param port
     * @param url
     * @return
     */
    private String getRequest(String ip, String port, String url) {
        HttpResponse<String> result = null;
        url = "".equals(url) ? "/Check/requestTest" : url;
        try {
            //Unirest.setTimeouts(3000,3000);
            result = Unirest.get(String.format("http://%s:%s%s", ip, port, url)).asString();
        } catch (Exception e) {
            return e.getMessage();
        }
        return result.getBody();
    }


    /**
     * 缓存查询页面
     * @return
     */
    @RequestMapping(value = "/cacheIndex")
    public String cacheIndex(){
        return ("/check/CacheIndex");
    }

    /**
     * 查看本地缓存统计信息
     * @return
     */
    @RequestMapping(value = "/stats")
    @ResponseBody
    public ResListResponse cacheStats(String cacheName) {
        ResListResponse  resListResponse = null;
        switch (cacheName) {
            case "*":
                resListResponse =  new ResListResponse(1, "成功获取了所有的cache！", new Result<>(0, 1, 0, Lists.newArrayList(GuavaCacheManager.getAllCacheStats())));
                break;
            default:
                break;
        }
        return resListResponse;
    }

//屏蔽清理缓存功能
//    /**
//     * 清空缓存数据、并返回清空后的统计信息
//     * @param cacheName
//     * @return
//     */
//    @RequestMapping(value = "/reset")
//    @ResponseBody
//    public JsonResult cacheReset(String cacheName) {
//        JsonResult jsonResult = new JsonResult();
//        GuavaCacheManager.resetCache(cacheName);
//        jsonResult.setMessage("已经成功重置了" + cacheName + "！");
//        return jsonResult;
//    }

//    /**
//     * 分页查询数据详情
//     * @return
//     */
//    @RequestMapping(value = "/queryDataByPage")
//    @ResponseBody
//    public PageResult<Object> queryDataByPage(@RequestParam Map<String, String> params){
//        int pageSize = Integer.valueOf(params.get("pageSize"));
//        int pageNo = Integer.valueOf(params.get("pageNo"));
//        String cacheName = params.get("cacheName");
//        PageParams<Object> page = new PageParams<>();
//        page.setPageSize(pageSize);
//        page.setPageNo(pageNo);
//        Map<String, Object> param = new HashMap<>();
//        param.put("cacheName", cacheName);
//        page.setParams(param);
//        return GuavaCacheManager.queryDataByPage(page);
//    }



}
