<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
   <%@ include file="./common.jsp" %>
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
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/main.js"></script>
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/jquery.nav.js"></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/order.js" ></script>
</head>
<body  style="background:#eee">
<div class="wrapper" >   
    <div class="index_head" >
        <div class="common_bar " style="background:#f7f7f7">
           <p>我的订单</p>
        </div>
    </div>
    <div class="page">
		<c:forEach var="o" items="${orders}">
			<div class="tksq">
				<c:if test="${o.orderType == 'third_part'}">
					<a href="${basePath}/openapi/customer/toThirdPage.do">
		            	<p class="tksq_sj">${o.merchantTitle}<span>(第三方订单)</span></p>
		            </a>
				</c:if>
				<c:if test="${o.orderType != 'third_part'}">
					<a href="${basePath}/takeOutController/menu/${o.merchantId}.do">
		            	<p class="tksq_sj">${o.merchantTitle}</p>
		            </a>
				</c:if>
	            
	            <div class="tksq_info clearfix">
		            <a href="${basePath}/takeOutController/${o.orderId}orderdetail.do<c:if test="${o.orderType == 'third_part'}">?isThirdOrder=true</c:if>">
		                <div class="tksq_pic"><img src="${o.merchantLogoUrl}@50w" alt="" /></div>
		                <div class="tksq_content ">
		                    <p>
		                    	<fmt:formatDate value="${o.orderCreateTime}" pattern="MM-dd HH:mm"/>
		                    </p>
		                    <p>应付:<span>￥${o.oughtPayMoney}</span>
		                    	<c:if test="${not empty o.orderNum }">
		                    		<span class="paihao">排号:${o.orderNum}</span>
		                    	</c:if>
		                    </p>
		                </div>
		            </a>
	                <div class="tksq_status">
	                	<c:choose>
	                		<c:when test="${o.refundState == 'normal'}">
			                    <p style="color:#808080">${o.orderStateCn }</p>
	                		</c:when>
	                		<c:when test="${o.refundState == 'askrefund' }">
			                    <p style="color:#808080">退款申请中</p>
	                		</c:when>
	                		<c:when test="${o.refundState == 'norefund' }">
			                    <p style="color:#808080">拒绝退款</p>
	                		</c:when>
	                		<c:when test="${o.refundState == 'berefund' }">
			                    <p style="color:#808080">已退款</p>
	                		</c:when>
	                	</c:choose>
	                
	                    <c:if test="${o.orderState == 'unpay' }">
		                    <c:if test="${o.orderType != 'third_part'}"><span class="qrsh topay" id="${o.orderId}">去付款</span></c:if>
		                    <c:if test="${o.orderType == 'third_part'}"><span class="qrsh toMyPay" id="${o.orderId}">去付款</span></c:if>
	                    </c:if>
	                    <c:if test="${o.orderState == 'pay' || o.orderState == 'accept' || o.orderState == 'delivery'}">
	                    	<c:if test="${o.orderType == 'normal' }">
			                    <span class="qrsh toConfirm" id="${o.orderId}" merid="${o.merchantId }" courId="${o.courierId }">确认收货</span>
	                    	</c:if>
	                    	<c:if test="${o.orderType == 'mobile' }">
			                    <span class="qrsh">电话订单</span>
	                    	</c:if>
	                    </c:if>
	                    <c:if test="${o.orderState == 'confirm' && !o.evaluated && o.refundState != 'berefund'}">
		                    <span class="qrsh toComment" id="${o.orderId}" merid="${o.merchantId }" courId="${o.courierId }" >去评价</span>
	                    </c:if>
	                </div>
	            </div>
	        </div> 
		</c:forEach>                           
    </div>

  <!--底部栏-->  
  <%@ include file="./navOrder.jsp" %> 
</div>  
<script type="text/javascript">
	var orderManager;
	$(function() {
		orderManager = new OrderManager();
		orderManager.init("${basePath}");
		
		$(".topay").click(function() {
			var orderId = $(this).attr("id");
 			orderManager.toPay(orderId);
		});
		
		$(".toMyPay").click(function() {
			var orderId = $(this).attr("id");
			window.location.href = "${basePath}/takeOutController/myPay.do?orderid=" + orderId;
		});
		
		$(".toConfirm").click(function() {
			var orderId = $(this).attr("id");
			var merid = $(this).attr("merid");
			var courId = $(this).attr("courId");
 			orderManager.confirmOrder(orderId, merid, courId);
		});
		
		$(".toComment").click(function() {
			var orderId = $(this).attr("id");
			var merid = $(this).attr("merid");
			var courId = $(this).attr("courId");
			window.location.href=  encodeURI("${basePath}/takeOutController.do?mineEvaluateNew&orderId="+orderId+"&merchantId="+merid+"&courierId="+courId);
		});
	});
</script>
</body>
</html>
