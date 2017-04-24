<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
</head>
<body style="background:#ececec">
<div class="wrapper">
  <div class="index_head">
    <div class="common_bar">
      <a href="${basePath}/takeOutController.do?mine" class="back"></a><p>我的红包</p>
    </div>
  </div>
  <div class="mine-coupon">
    <c:forEach items="${coupons}" var="c">
    	<div class="mine-couponItem">
	        <div class="couponItem-left">
	            <p>￥${c.couponsMoney/100.0}元</p>
	            <p>消费满<span>￥${c.couponsLimitMoney/100.0}</span>可用</p>
	        </div>
	        <div class="couponItem-right">
	           <p>有效期至</p>
	           <p>
	           	  <c:if test="${c.endTime == 0}">不限</c:if>
	           	  <c:if test="${c.endTime > 0}">
	           	  	<fmt:formatDate value="${c.endDate }" pattern="yyyy-MM-dd" />
	           	  </c:if>
	           </p>
	        </div>
    	</div>
    </c:forEach>
  </div>
</div>
</body>
</html>