<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="res_path" value="${pageContext.request.contextPath }" />
<!DOCTYPE HTML>
<html>
<head>
    <title>Cache Admin</title>
    <meta charset="UTF-8">
    <script type="text/javascript" charset="utf-8" src="${res_path}/resources/js/template.js"></script>
    <script type="text/javascript" charset="utf-8" src="${res_path}/resources/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${res_path}/resources/js/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" href="${res_path}/resources/css/bootstrap.min.css">
</head>
<body>
<div>
    <div id="operations"  class="form-group" >
        <label >Cache列表：</label>
        <input type="button" value="刷新" onClick="refreshStatsAll();">
        <input type="checkbox" id="autoRefresh" onClick="toggleAutoRefreshStats();"><label for="autoRefresh">自动刷新（3s）</label>
    </div>
    <div><pre id="response" class="attention"></pre></div>
   <%-- <div><br><pre id="responseRawData" ></pre></div>--%>
</div>
</body>
<script>
    var autoRefreshInterval = 3000;
    var autoRefershObject;
    var requestStatsAll = {url : "/Check/stats", params : 'cacheName=*', callback: requestStatsAllCallback};
    $(function() {
        refreshStatsAll();
    });
    function refreshStatsAll(){
        ajaxRequest(requestStatsAll.url, requestStatsAll.params, requestStatsAll.callback);
    }
    function sizeStatistics(obj){
        var c = "当前数据量：" + obj.size ;
        return c;
    }
    function hitStatistics(obj){
        var c = "命中数量：" + obj.hitCount;
        c += "\n命中比例：" + obj.hitRate;
        c += "\n读库比例：" + obj.missRate;
        return c;
    }
    function loadStatistics(obj){
        var c = "成功加载数：" + obj.loadSuccessCount;
        c += "\n失败加载数：" + obj.loadExceptionCount;
        c += "\n总加载毫秒：" + obj.totalLoadTime;
        return c;
    }
    function requestStatsAllCallback(jsonResult){
        var html = "<div class='table-responsive'> <table class='table'><tr><th>Cache名称</th> <th>数据量统计</th> <th>命中统计</th> <th>加载统计</th> <th>开始/重置时间</th> <!--<th>操作</th> --></tr>";
        $.each(jsonResult.result, function(idx, o){
            $.each(o, function(idx, obj) {
                html += "<tr><th>" + obj.cacheName + "</th>"
                        + "<td>" + sizeStatistics(obj) + "</td>"
                        + "<td>" + hitStatistics(obj) + "</td>"
                        + "<td>" + loadStatistics(obj) + "</td>"
                        + "<td>" + obj.resetTime + "\n\n失效时长：" + obj.survivalDuration + "</td>"
                        /*       + "<td>"
                    + "<a href='javascript:void(0)' onclick='queryDataByPage(\""+obj.cacheName+"\");'>显示详情</a>"*/
                        /*     + "\t<a href='javascript:void(0)' onclick='resetCache(\""+obj.cacheName+"\");'>清空缓存</a>"*/
                        /*    + "</td>"*/
                    + " </tr> </div>";
                });
        });
        html += "</table>";
        $("#response").html(html);
    }
    function resetCache(cacheName){
        $.ajax({
            type : "POST",
            url : getRootPath()+"/Check/reset",
            dataType : "json", //表示返回值类型
            data : {"cacheName":cacheName},
            success : function(jsonResult){alert(jsonResult.message);refreshStatsAll();}
        });
    }
    //定时刷新开关
    function toggleAutoRefreshStats(){
        if($("#autoRefresh").prop("checked")==true){
            autoRefershObject = setInterval(refreshStatsAll, autoRefreshInterval);
        }else{
            clearInterval(autoRefershObject);
        }
    }


    //发送ajax请求
    function ajaxRequest(url, params, successCallback, contentType, errorCallback, async) {
        var _async = async || true;
        $.ajax({
            type : "GET",
            url : "${res_path}" + url,
            async : _async,
            contentType : contentType,
            dataType : "json", //表示返回值类型
            data : params,
            success : successCallback,
            error : errorCallback
        });
    }

</script>
</html>