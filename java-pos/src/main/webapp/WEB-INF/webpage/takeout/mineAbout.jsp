<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
	<meta content="private,must-revalidate" http-equiv="Cache-Control">
	<meta content="telephone=no, address=no" name="format-detection">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<script src="http://api.map.baidu.com/api?v=2.0&ak=U8yYl7OgxAoAiDxvGZkG4wFk" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
	
	<script type="text/javascript">
		var basePath="${basePath}";
		$(function(){
			$(".back").click(function(){
				window.location.href=basePath+"/takeOutController.do?mineSetting";
			});
		});
	</script>
</head>

<body style="background:#ececec">
<div class="wrapper">
    <div class="index_head">
        <div class="common_bar">
            <a href="#" class="back"></a><p>关于我们</p>
        </div>
    </div>
  <div class="about_pic">
    <img src="${basePath}/plug-in/wxIndex/images/logo.png"  alt="logo" />
    <p>一号外卖 v1.5</p>
  </div>   
</div>
</body>

</html>