<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="./common.jsp"  %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>1号外卖</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
<meta content="private,must-revalidate" http-equiv="Cache-Control">
<meta content="telephone=no, address=no" name="format-detection">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/order.css" />
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/common.js" ></script>
</head>
<body>
<div class="wrapper">
    <div class="index_head">
        <div class="common_bar clearfix">
            <a href="${basePath}/takeOutController/menu/${shopcart.merchantId}.do" class="back"></a><p>选择下单类型</p>
        </div>            
    </div>   
    <div class="select-order">
        <ul>
            <li><a href="javascript:void(0)" class="active type" id="1">外卖</a></li>
            <li><a href="javascript:void(0)" class="type" id="2">堂食</a></li>
        </ul>
    </div>
    <a href="javascript:void(0)" class="confirm">确认</a>
   
   <form action="${basePath}/takeOutController/writeOrder.do" method="post" id="writeOrder">
   		<input type="hidden" name="saleType" id="saleType" value="1">
   		<input type="hidden" name="json" id="carts">
   		<input type="hidden" name="merchantId" value="${shopcart.merchantId}">
   		<input type="hidden" name="userId" value="${shopcart.userId}">
   </form>
</div>        
</body>
<script type="text/javascript">
	var json = '${shopcart.json}';
	$(function() {
		$(".type").click(function() {
			var type = $(this).attr("id");
			$("#saleType").val(type);
			$(".type").removeClass("active");
			$(this).addClass("active");
		});
		$(".confirm").click(function() {
			$("#carts").val(json);
			$("#writeOrder").submit();
		});
	});
</script>
</html>