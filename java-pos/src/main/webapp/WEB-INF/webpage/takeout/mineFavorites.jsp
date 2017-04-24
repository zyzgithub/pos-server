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
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
	
</head>

<body style="background:#ececec">
	<div class="wrapper">
	    <div class="index_head">
	        <div class="common_bar">
	            <a href="#" class="back"></a><p>我的收藏</p>
	        </div>
	    </div>
	    <div class="mine_sc"  style="background:#fff;">
	        <ul class="order_list" id="merchantList"> </ul>
		    <div id="loading_div" class="msg">点击加载更多</div>
	    </div>
	</div>
</body>

    <script type="text/javascript" src="${basePath}/plug-in/taskout/js/merchantlist.js?id=13"></script>
	<script type="text/javascript">
		var basePath="${basePath}";
		var merchantManager;//商家管理
		$(function(){
			$(".back").click(function(){
				window.location.href=basePath+"/takeOutController.do?mine";
			});
			//初始化数据
			merchantManager=new MerchantManager();
			merchantManager.basePath = "${basePath}";
			//加载数据
 	        merchantManager.loadUserFavMerchants();

			//点击加载更多
			$("#loading_div").click(function(){
				merchantManager.loadUserFavMerchants();
			});
		});
	</script>

</html>