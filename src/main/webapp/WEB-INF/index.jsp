<%@ page language="java" pageEncoding="utf-8" %>

<%@ page contentType="text/html;charset=utf-8" %>
<html>
<meta http-equiv="Content-Type" content="text/html;charset=gb2312">
<link type="text/css" rel="stylesheet" href="resources/css/bootstrap.min.css">
<body>
<div class="container">
    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-xs-right">

            </ul>
        </nav>

    </div>

    <div class="row">
        <form>
            <div class="form-group">
                <label for="resId">产品位标识</label>
                <input type="text" class="form-control" id="resId" value="10000"/>
            </div>
            <div class="form-group">
                <label for="cityId">城市Id</label>
                <input type="text" class="form-control" id="cityId" value="110100"/>
            </div>
            <div class="form-group">
                <label for="seriesId">车系Id</label>
                <input type="text" class="form-control" id="seriesId" value="0"/>
            </div>
            <div class="form-group">
                <label for="specId">车型Id</label>
                <input type="text" class="form-control" id="specId" value="0"/>
            </div>
            <div class="form-group">
                <button type="button" id="getData" class="btn btn-success">请求数据</button>
            </div>
        </form>
    </div>
    <div class="row" style="background-color: transparent">
        <div class="col-sm-12">
            <div id="ad_word_01"></div>
        </div>

    </div>
    <%--<div id="ResContentId">
        <a link="http://localhost:8080?id=1&name=2&cn=测试" href="javascript:void(0)" target="_blank">服务端新窗口</a>
        <br/>
        <a link="http://localhost:8080?id=1&name=2&cn=测试" href="javascript:void(0)" target="_top">服务端当前页面</a>
    </div>--%>
</div>


<script type="text/javascript" charset="utf-8" src="resources/js/template.js"></script>
<script type="text/javascript" charset="utf-8" src="resources/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="resources/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $(function () {
        $("#getData").click(function () {
            DealerResAdmin.resLoader('ad_word_01', $("#resId").val(), $("#cityId").val(), $("#seriesId").val(), $("#specId").val());//(divid, resid, cityid, seriesid, specid)
        });
    })
</script>
</body>
</html>
