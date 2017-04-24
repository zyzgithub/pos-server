<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
request.setAttribute("basePath", basePath);
%>
<html>
 <head>
  <script type="text/javascript" src="${basePath}/plug-in/jquery/jquery-1.8.3.min.js"></script>
  <script type="text/javascript">
    var basePath="${basePath}";
	$(document).ready(function() {
	 var browserversion = "";
	 //IE8+浏览器
	 if ($.browser.msie) {
		 browserversion = "IE" + $.browser.version;
	 }
	 //谷歌浏览器
	 if ($.browser.webkit) {
		 browserversion = "Chrome" + $.browser.version; 
	 }
	 //火狐浏览器
	 if ($.browser.mozilla) {
		 browserversion = "Mozilla Firefox" + $.browser.version;
	 }
	 //欧朋浏览器
	 if ($.browser.opera) {
		 browserversion = "Opera" + $.browser.version;
	 }
	 
	 window.location.href = basePath+"/loginController.do?login";
	 
 });
</script>
 </head>
 <body>
 </body>
</html>