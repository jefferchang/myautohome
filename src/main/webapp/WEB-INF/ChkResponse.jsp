<%--
  User: Long.F
  Date: 2016/5/9
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="resources/css/bootstrap.min.css">
    <title>系统检测</title>
    <style type="text/css">
        table {
            border: none;
            width: 100%;
            border-radius: 5px;
        }

        table td {
            border: 1px solid #dedede;
            margin: 0;
            border-collapse: collapse;
            padding: 5px;
        }
    </style>
</head>
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
                <label for="ip">Ip,Ip</label>
                <input type="text" class="form-control" id="ip" value="10.20.56.2,10.20.56.3"/>
            </div>
            <div class="form-group">
                <label for="port">Port,Port</label>
                <input type="text" class="form-control" id="port" value="10001,11001,12001"/>
            </div>
            <div class="form-group">
                <label for="RelativeUrl">RelativeUrl</label>
                <input type="text" class="form-control" id="RelativeUrl"
                       value="/Res/v1/Get?cityId=110100&specId=14941&resId=10000&seriesId=0&requestId=e055805b-48b0-fbf4-0c6d-086232beaaca&pvId=1000&callback=jsonp668170"/>
            </div>
            <div class="form-group">
                <button type="button" id="getData" class="btn btn-success">请求数据</button>
            </div>
        </form>
    </div>
    <table id="ad_word_01">

    </table>
</div>

<script type="text/javascript" charset="utf-8" src="resources/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="resources/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $(function () {
        $('#getData').click(function () {
            if ($('#ip').val() == "" || $('#port').val() == "") {
                alert("ip:port都不能为空!");
                return false;
            }
            var url = "?r=" + Math.random() + "&url=" + encodeURIComponent($('#RelativeUrl').val())
            $.ajax({
                type: "POST",
                url: "/Check/get/" + $('#ip').val() + "/" + $('#port').val() + url,
                success: function (data) {
                    if (data.returnCode == 0) {
                        $('#ad_word_01').html(data.result);
                    } else {
                        alert("实例异常!");
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                },
                beforeSend: function () {
                    $('#ad_word_01').html("<tr><td>系统处理中， 请耐心等待。。</td></tr>");
                }

            });

        });
    })
</script>
</body>
</html>
