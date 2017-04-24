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
	<script type="text/javascript" src="${basePath }/plug-in/taskout/js/signmobile.js?version=${webVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
	
</head>

<body style="background:#ececec">
	<div class="wrapper">
	    <div class="index_head">
	        <div class="common_bar">
	            <a href="#" class="back"></a><p>绑定手机</p>
	        </div>
	    </div>
	    <div class="bind_phone">
	        <ul>
	            <li>
	                <span>手机号</span>
	                <input type="text" id="mobile" name="mobile"  class="" />
	            </li>
	            <li>
	                <span>校证码</span>
	                <input type="text" id="code" name="code"   class="" />
	                <a href="javascript:void(0);"  id="sendBtn" class="getCode" >获取验证码</a>
	            </li>
	        </ul>
	    </div> 
	    <a href="javascript:void(0);" class="bind_btn" id="bindBtn" style="color:#fff;">绑定</a>
	</div>
	
	
	<script type="text/javascript">
		var basePath="${basePath}";
		$(function(){
			$(".back").click(function(){
				window.location.href=basePath+"/takeOutController.do?mine";
			});
			//初始化手机绑定器
			signMobileMng=new SignMobileMng();
			signMobileMng.init(basePath);
		});
	</script>
</body>

</html>