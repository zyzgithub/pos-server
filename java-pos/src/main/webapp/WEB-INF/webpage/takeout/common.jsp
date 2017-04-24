<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt2" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %> 
<% 
String basePath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
request.setAttribute("basePath", basePath);
request.setAttribute("baseTitle", "1号外卖");
request.setAttribute("webVersion", 14);
%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="description" content="外卖 点餐 1号外卖  1号外卖 ">
	<meta name="keywords" content="外卖,点餐,1号外卖,1号外卖">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="shortcut icon" type="image/x-icon" href="${basePath}/plug-in/taskout/images/logo.ico"/>
	<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/taskout/index.css?id=32" />
	<script type="text/javascript" src="${basePath}/plug-in/jquery/jquery-1.8.3.min.js"></script>
	<link type="text/css" rel="stylesheet" href="${basePath }/plug-in/wapDialog/css/wap.dialog.css?id=6" />
	<script type="text/javascript" src="${basePath }/plug-in/wapDialog/jquery.wap.dialog.min.js?id=6"></script>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
	//手机浏览器版本
	var browser = {
		versions: function () {
			var u = navigator.userAgent;
			return { //移动终端浏览器版本信息 
				ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
				android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器 
				iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器 
				iPad: u.indexOf('iPad') > -1, //是否iPad 
				weixin:u.toLowerCase().indexOf("micromessenger")>-1
				};
		}(),
	};
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
        WeixinJSBridge.call('showOptionMenu');//显示右上角菜单
    });
	</script>
	
	