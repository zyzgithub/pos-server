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
    <script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/main.js?version=${webVersion}"></script>
    <script type="text/javascript" src="${basePath}/plug-in/taskout/js/merchantlist.js?id=9"></script>
    <script type="text/javascript" src="${basePath}/plug-in/taskout/js/orderlist.js?id=9"></script>
    <script type="text/javascript" src="${basePath}/plug-in/taskout/js/Util.js?id=9"></script>
	
	<script type="text/javascript">
		$.fn.csty = function(options){
			var defParam = {
					'color': 'red',
					'fontSize': '12px'
			}
			var settings = $.extend({}, defParam, options);
			this.css("color", settings.color);
			this.each(function(){
				$t = $(this);
				$t.append($t.attr('href'));
			});
			return this;
		}
	</script>
</head>
<body style="background:#ececec">
	<div class="wrap" style="padding-bottom:55px">
	   <div class="mine_bar">
	       <p><a href="${basePath}/takeOutController.do?mineSetting" class="setting"></a>我的
	       <a href="${basePath}/takeOutController.do?mineMsg" class="mes"></a></p>
	       <div class="mine_info clearfix">
	      	 <a href="${basePath}/takeOutController.do?mineAccountInfo">
	            <div class="mine_icon">
	            <c:choose>
	            	<c:when test="${not empty photourl }">
	            		<img src="${photourl }"  alt="" />
	            	</c:when>
	            	<c:otherwise>
	            		<img src="${basePath}/plug-in/wxIndex/images/mine_default-photo.png"  alt="" />
	            	</c:otherwise>
	            </c:choose>
	            </div>
	            <div  class="mine_detail">
	                <p class="words">${nickname }</p>
	                <c:choose>
						<c:when test="${mobile != null && not empty mobile }"><p> ${mobile }</p></c:when>
						<c:otherwise><p>未绑定手机</p></c:otherwise>
					</c:choose>
	                <i></i>
	            </div>       
	         </a>     
	       </div>
	       <div class="mine_div"></div>
	       <ul class="clearfix">
	         <li>余额：<span>${balance}</span>元</li>
	         <li>积分：<span>${score}</span>分</li>
	       </ul>
	   </div>
	   <div class="mine_dd">
	      <ul class="clearfix">
	        <li><a href="${basePath}/takeOutController/mineWaitingPay.do"><i></i><span>待付款</span></a></li>
	        <li><a href="${basePath}/takeOutController.do?mineWaitingReceipt"><i></i><span>待收货</span></a></li>
	        <li><a href="${basePath}/takeOutController.do?mineWaitingEvaluates"><i></i><span>待评价</span></a></li>
	        <li><a href="${basePath}/takeOutController.do?mineRefunds"><i></i><span>退款单</span></a></li>
	
	      </ul>
	   </div>
 	   <div class="mine_djq">
	     <!--  <p><a href="${basePath}/takeOutController.do?mineRecharge">充值<i></i></a></p> -->
	      <p><a href="${basePath}/coupons/mylist.do">红包<i></i></a></p>
	   </div> 
	   <div class="mine_gl">
	      <p><a href="${basePath}/takeOutController.do?mineAddresses">管理我的地址<i></i></a></p>
	      <p><a href="${basePath}/takeOutController.do?mineFavorites">收藏<i></i></a></p>
<%-- 	       <p class="clearfix openidBh"><a href="javascript:void(0)">编号</a><span>${openId}</span></p> --%>
	   </div>
	  <!--展开遮罩--> 
	  <div class="shadow_div"></div>
	  <!--底部栏-->  
	  <%@ include file="./navMine.jsp" %>
	</div>
<script type="text/javascript">
   $('.openidBh>span').css('width',($(window).width()-85)+'px');
</script>
</body>
</html>
