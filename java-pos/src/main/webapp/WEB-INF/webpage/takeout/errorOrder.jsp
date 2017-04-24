<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<%@ include file="./common.jsp" %>
	<meta http-equiv="refresh" content="2;url=${basePath}/takeOutController/menu/${merchantId}.do">
	<title>${baseTitle}</title>
	<style type="text/css">
	ol{list-style: none;width:100%;}
	li{line-height: 3em;width:100%;}
	a{color: #A59D9D;font-size: 1.2em;width:100%;text-align: center;display: block;}
	</style>
  </head>
  
  <body>
 	<div class="main">
 		<div class="msg">
	 		菜单价格发生变动,请重新确定是否购买
 		</div>
 		<div class="clear"></div>
 		<br/>
   	</div>
  </body>
</html>
