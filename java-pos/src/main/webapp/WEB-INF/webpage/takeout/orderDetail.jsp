<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="./common.jsp" %>
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
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css" /> 
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/order.js" ></script>
</head>
<body style="background:#ececec;">
<div class="wrapper">
    <div class="index_head">
        <div class="common_bar clearfix">
            <a href="${basePath}/takeOutController/orderlist.do" class="back"></a><p class="words">${order.merchantName}</p>
        </div>
    </div>   
    <div class="order_detail">
        <ul class="order_detail_bar clearfix">
            <li><span class="active">订单状态</span></li>
            <li><span >订单详情</span></li>
        </ul>
        <div class="order_status ">
            <div class="order_status_con">
            <c:choose>
            	<c:when test="${order.state == 'unpay' || order.state == 'cancel'}">
	                <p class="order_status_dfk">${order.stateCn }</p>
            	</c:when>
            	<c:when test="${order.state == 'delivery' }">
            		<div class="kd_status">
	                    <div class="kd_info clearfix">
	                        <div class="kd_icon"><img src="${order.courierIcon}"  alt="" /></div>
	                        <div class="kd_pf">
	                            <p>${order.courierName }<span>正在配送中</span></p>
	                            <p>已服务<span>${order.deliveryCount }</span>单</p>
	                            <p>
	                            	<c:forEach begin="1" end="5" var="s">
		                        		<i 
				                       	<c:choose>
				                    		<c:when test="${s <= order.score }">class="active "</c:when>
				                    		<c:when test="${s-1 < order.score }">class="half_active"</c:when>
				                       	</c:choose>
				                       ></i>
		                        	</c:forEach>
	                                <span>${order.score}</span>
	                            </p>
	                        </div>
	                        <a href="tel:${order.couriermobile }" class="kd_call"></a>
	                    </div>
       
                	</div>
            	</c:when>
            	<c:when test="${order.restate == 'berefund' }">
	                <p class="order_status_ywc">已退款</p>
	            </c:when>
            	<c:otherwise>
	                <p class="order_status_ywc">${order.stateCn }</p>
            	</c:otherwise>
            </c:choose>
           
            <!-- 已退款状态  对应 重新下单 背景图不同  
                <p class="order_status_ytk">已退款</p> -->
            <!-- 已付款状态,  已付款对应 退单和催单按钮      
                <p class="order_status_yfk">已付款</p> 
            -->
            </div>
            <!-- 退款失败原因  -->
            <div class="tksbyy">
                	退款失败原因：该商品正在配送中，很快为您送达，请耐心等候，给您造成的不便敬请谅解。
            </div>
            <div class="order_status_btn_wm">
	            <!-- 配送完成确认收货 -->
	            <c:if test="${order.state == 'cancel'}">
			        <a href="${basePath}/takeOutController/menu/${order.merchantId}.do" class="enter">进入店铺</a>
		        </c:if>
	            <c:if test="${order.state == 'unpay' }">
			       <span class="qfk topay" id="${order.orderId}">去付款</span>
		        </c:if>
		        <c:if test="${order.state == 'pay'}">
		            <div class="cd_td clearfix">
		            	<c:choose>
	                		<c:when test="${order.restate == 'normal' }">
		                   		<div><span class="tuid" orderId="${order.orderId}">退单</span></div>
	                		</c:when>
	                		<c:when test="${order.restate == 'askrefund' }">
			                    <div><span class="cur_btn">退款申请中</span></div>
	                		</c:when>
	                		<c:when test="${order.restate == 'norefund' }">
			                    <div><span class="cur_btn">拒绝退款</span></div>
	                		</c:when>
	                	</c:choose>
			        	<div><span class="qrsh" orderId="${order.orderId}">催单</span></div>
               		</div>
		        </c:if>
		        <c:if test="${order.state == 'accept' || order.state == 'delivery'}">
		        	<c:if test="${order.orderType == 'normal' }">
			       		<div class="psz clearfix">
		                    <c:choose>
		                		<c:when test="${order.restate == 'normal' }">
			                   		<div><span class="tuid" orderId="${order.orderId}">退单</span></div>
		                		</c:when>
		                		<c:when test="${order.restate == 'askrefund' }">
				                    <div><span class="cur_btn">退款申请中</span></div>
		                		</c:when>
		                		<c:when test="${order.restate == 'norefund' }">
				                    <div><span class="cur_btn">拒绝退款</span></div>
		                		</c:when>
		                	</c:choose>
		                	
		                	<div><span class="cuid">催单</span></div>
				        	
				        	<div><span class="qrsh toConfirm" id="${order.orderId}" merid="${order.merchantId }" courId="${order.courierId }">确认收货</span></div>
		          		</div>
		        	</c:if>
		        	<c:if test="${order.orderType == 'mobile' }">
				        <span class="enter" >电话订单</span>
		        	</c:if>
		        </c:if>
		        <c:if test="${order.state == 'confirm' && order.hasComment == 0 && order.restate != 'berefund' }">
			        <a href="${basePath}/takeOutController.do?mineEvaluateNew&orderId=${order.orderId }&merchantId=${order.merchantId}&courierId=${order.courierId}" class="qpj">去评价</a>
		         </c:if>
		        <c:if test="${order.state == 'confirm' && order.hasComment > 0}">
			        <a href="${basePath}/takeOutController/menu/${order.merchantId}.do" class="enter">进入店铺</a>
		        </c:if>
            </div>            
        </div>
        <div class="order_detail_wm  order_detail_ts"   style="display:none">
            <div class="order_detail_inner" >
                <a href="${basePath}/takeOutController/menu/${order.merchantId}.do" class="order_names">${order.merchantName}<i></i></a>
                <div class="order_num_out">
                    <ul class="order_count">
	                    <c:forEach var="m" items="${order.menus}">
	                        <li class="order_num clearfix">
	                            <p>${m.name}</p>
	                            <div>
	                            	<span>×${m.quantity}</span>
	                            	<span>￥${m.totalPrice}</span>
	                            </div>
	                        </li>
	                    </c:forEach>
	                    <li class="order_num money clearfix">
	                        <p>合计</p>
	                        <div>
	                        	<span>×${order.totalQuantity}</span>
	                        	<span>￥${order.origin}</span>
	                        </div>
	                    </li>
                    </ul>
                    <c:if test="${order.scoreMoney > 0 || order.deliveryFee > 0 || order.couponsMoney > 0}">
	                    <ul class="order_discount">    
                    		<c:if test="${order.scoreMoney > 0}">
	                        <li class="order_num clearfix">
	                            <p>积分抵消</p>
	                            <div>
	                            	<span></span>
	                            	<span>-￥${order.scoreMoney}</span>
	                            </div>
	                        </li>
	                        </c:if>
                    		<c:if test="${order.couponsMoney > 0}">
	                        <li class="order_num clearfix">
	                            <p>红包</p>
	                            <div>
	                            	<span></span>
	                            	<span>-￥${order.couponsMoney}</span>
	                            </div>
	                        </li>
	                        </c:if>
                    		<c:if test="${order.deliveryFee > 0}">
	                        <li class="order_num clearfix">
	                            <p>配送费</p>
	                            <div>
	                            	<span></span>
	                                <span>￥${order.deliveryFee}</span>
	                            </div>
	                        </li>
	                        </c:if>
	                        <li class="order_num money clearfix">
	                            <p>应付</p>
	                            <div>
	                            	<span></span>
	                            	<span>￥${order.oughtPayMoney}</span>
	                            </div>
	                        </li>
	                    </ul>
                    </c:if>
                </div>                 
            </div>
            <ul class="user_info">
                <li>联系人信息</li>
                <li>排号：<span>${order.orderNum}</span></li>
                <li>订单号：<span>${order.payId}</span></li>
                <li>订单类型：<span>${order.saleTypeCn}</span></li> 
                <li>支付方式：<span>${order.payTypeCn}</span></li>
                <li>联系人：<span>${order.name}</span></li>
                <li>电话：<span>${order.mobile}</span></li>
                <c:if test="${not empty order.address}">
	                <li>送餐地址：<span>${order.address}</span></li>
                </c:if>
                <c:if test="${not empty order.timeRemark}">
	                <li>送餐时间：<span>${order.timeRemark}</span></li>
                </c:if>
            </ul>
            <div class="order_status_btn">
                <c:if test="${order.state == 'unpay' }">
		       		<span class="qfk topay" id="${order.orderId}">去付款</span>
	        	</c:if>
                <c:if test="${order.state == 'confirm' && order.hasComment == 0 && order.restate != 'berefund' }">
	                <a href="${basePath}/takeOutController.do?mineEvaluateNew&orderId=${order.orderId }&merchantId=${order.merchantId}&courierId=${order.courierId}" class="qpj">去评价</a>       
	         	</c:if>
	            <c:if test="${order.state == 'confirm' && order.hasComment > 0}">
	                <a href="${basePath}/takeOutController/menu/${order.merchantId}.do" class="enter">进入店铺</a>       
		        </c:if>
            </div>               
        </div> 
    </div>       
</div> 
<script type="text/javascript">

	var orderManager;
    $(function(){    /*退单和催单的点击事件*/
        $('.order_detail_bar>li').click(function() {
            /* Act on the event */
            $(this).children('span').addClass('active');
            $(this).siblings().children('span').removeClass('active');
            $('.order_detail>div').eq($(this).index()).show().siblings('div').hide();
        });
    
        orderManager = new OrderManager();
		orderManager.init("${basePath}");
		
		$(".topay").click(function() {
			var orderId = $(this).attr("id");
 			orderManager.toPay(orderId);
		});
		
		$(".toConfirm").click(function() {
			var orderId = $(this).attr("id");
 			var merid = $(this).attr("merid");
			var courId = $(this).attr("courId");
 			orderManager.confirmOrder(orderId, merid, courId);
		});
		
        $('.tuid').click(function() {
            /* Act on the event */
            var orderId = $(this).attr("orderId");
            orderManager.askRefund(orderId);
        }); 
        $('.cuid').click(function() {
            /* Act on the event 
            $(this).addClass('cur_btn');
            $(this).text('已催单');
            */
            $.WapDialog.tip("已催单");
        });
    });
</script>
</body>
</html>