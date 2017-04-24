<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<% 
String basePath = request.getContextPath();
request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<script src="http://api.map.baidu.com/components?v=1.0&ak=hf7GGTXwqxndSLLYBfrKVwZh" type="text/javascript"></script>
	<title>${name }</title>
	<style type="text/css">
		body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
		#golist {display: none;}
		@media (max-device-width: 800px){#golist{display: block!important;}}
		.head{background-color:#FE6600;padding-left:1%;padding-right:1%;width:92%;height:50px;line-height:50px;
		border-bottom: 1px solid #eee;color:#FFFFFF;font-size:18px;text-decoration: none;
		padding-left:8%;  background-position: left center;
		background-image:url("${basePath}/plug-in/taskout/images/back.png");background-size:30px 25px; background-repeat:no-repeat;}
	</style>
</head>
<body>
	
	<a id="golist" href="javascript:history.back();" class="head"> 返回</a>
    <lbs-map width="100px" style="height:100%" center="${lng },${lat}">
    	<lbs-poi name="${name }" location="${lng },${lat}" addr="${address }" ></lbs-poi>
    </lbs-map>
</body>
</html>

