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
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css" />
	
	<script type="text/javascript">
		var basePath="${basePath}";
		$(function(){
			$(".back").click(function(){
				window.location.href=basePath+"/takeOutController.do?mine";
			});
		});
	</script>
</head>

<body style="background:#ececec">
	<div class="wrapper">
	    <div class="index_head">
	        <div class="common_bar clearfix">
	            <a href="#" class="back"></a><p>设置</p>
	        </div>             
	    </div>   
	    <div class="setting_inner">
	       <ul>
	         <li>
	           <a href="${basePath}/takeOutController.do?mineAbout">关于我们</a>
	         </li>
	         <li>
	           <a href="tel:4008945917">客服电话<span>4008945917</span></a>
	         </li>         
	       </ul>
	
	    </div>    
	</div>
</body>

</html>