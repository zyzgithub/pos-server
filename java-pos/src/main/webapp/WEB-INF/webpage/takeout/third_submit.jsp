<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
  <head>
  	<%@ include file="./common.jsp" %>
  	<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>1号外卖</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
	<meta content="private,must-revalidate" http-equiv="Cache-Control">
	<meta content="telephone=no, address=no" name="format-detection">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/third/css/common.css" />
	    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/third/css/thirdOrder.css" />
	    <script type="text/javascript">
	    var retStr = '<%=request.getAttribute("OpenResult") %>';
		var ret = $.parseJSON(retStr);
			$(function(){
				if(ret.state == 100){//success
					$($('.third-page p')[0]).html('订单名称：' + ret.data.out_order_title);
					$($('.third-page p')[1]).html('订单金额：' + ret.data.price + "元");
				}else{
					alert(ret.msg);
					window.location.href="${basePath}/error.jsp";
				}
			});
			
			var basePath="${basePath}";//跟路径
	 		//应付金额
			var payMoney = parseFloat(ret.data.price);
			var isWeixin=false;
			var submit=false;
			$(function(){
				var userAgent=window.navigator.userAgent.toLowerCase();
				if(userAgent.match(/MicroMessenger/i)=="micromessenger"){
					isWeixin=true;
				}
				if(isWeixin){//是微信时需要在微信js接口加载好后才绑定支付按钮单击事件
					document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
						bindPayButtonEvent();
					}, false);
				}else{//不是微信时直接绑定支付按钮单击事件
					bindPayButtonEvent();
				}
				
				$('.toMyOrder').click(function(){
					/* window.location.href = "http://wmtest.0085.com/weixin/store/userOrderList"; */
					/*
					1.6版
					window.location.href = "http://no1.0085.com/weixin/store/userOrderList"; 
					*/
					/*1.5版*/
					window.location.href = "http://no1.0085.com/orderList/orderList.do";
				});
				$('#rePay').click(function(){
					submit = false;
					$('#payButton').click();
				});
			});
			
			//绑定支付按钮事件
			function bindPayButtonEvent(){
				$('#payButton').click(function(){
					if(submit){
						return;
					}
					//微信支付
	        		$.WapDialog.lock({content:'正在进入微信支付...'});//锁屏
	        		if(isWeixin){
	        			try{
	        				$.ajax({  
				                url: basePath+"/takeOutController.do?getThirdPayInfo&time=" + Math.round(new Date().getTime()/1000),
				                data:{
				                	orderid: ret.data.orderid,
				                	userid: ret.data.userid
				                },
				                success:function(data){
				                	var d = data;
				                	if("object" != typeof data) d = JSON.parse(data);
				                     if(d.stateCode == "01"){
				                    	$.WapDialog.close();//解锁
				                    	submit=false;
				                    	//=$.WapDialog.tip('支付失败：'+d.msg);
				                    	$('.pay-error').show();
				                     	return;
				                     }
				                     if(parseInt(d.obj.agent) < 5){
				                    	 $.WapDialog.close();//解锁
				                    	 submit=false;
				                    	 $.WapDialog.tip("您的微信版本低于5.0无法使用微信支付");  
				                         return;  
				                     }  
			             			WeixinJSBridge.invoke('getBrandWCPayRequest',{  
			                             "appId" : d.obj.appId,                  //公众号名称，由商户传入  
			                             "timeStamp": d.obj.timeStamp,          //时间戳，自 1970 年以来的秒数  
			                             "nonceStr" : d.obj.nonceStr,         //随机串  
			                             "package" : d.obj.packageValue,      //<span style="font-family:微软雅黑;">商品包信息</span>  
			                             "signType" : d.obj.signType,        //微信签名方式:  
			                             "paySign" : d.obj.paySign           //微信签名  
			                             },function(res){
			                            	 $.WapDialog.close();//解锁
			                                 if(res.err_msg == "get_brand_wcpay_request:ok" ){  
			                                	 //$.WapDialog.lock({content:'支付成功，正在跳转...'});
			                                	 $('.pay-success').show();
			                                 }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
			                                 	//$.WapDialog.lock({content:"支付已取消"});
			                                 	$('.pay-error').show();
			                                 }else{
			                                 	//$.WapDialog.lock({content:"支付失败，请重试"});  
			                                 	$('.pay-error').show();
			                                 }
			                                 submit=false;
			                             });
				                },error:function(e){
				                	$('#showDetail').html(e);
				                	 $.WapDialog.close();//解锁
		                        	 submit=false;
				                	 $.WapDialog.tip("抱歉,操作失败，请重试");  
				                }
				               });
	        			}catch(e){
	        				$.WapDialog.tip("抱歉,操作异常，请重试");  
	        			}
	    			}else{
	    				$.WapDialog.close();
	    				$.WapDialog.tip("抱歉！非微信请使用余额支付吧!"); 
	    				submit=false;
	    			} 
				});
			}
			
			document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
	        });
			
		</script> 
	</head>
  </head>
  
  <body>
<div class="wrapper">
	<input type="hidden" id="code" name="code" value="${sessionScope._WEI_XIN_CODE_}">
    <div class="index_head">
        <div class="common_bar clearfix">
            <a href="###" class="back"></a><p class="words">支付订单</p><a href="javascript:;"></a>
        </div>
    </div> 
    <div class="third-page">
       <p>订单名称：</p>
       <p>订单金额：元</p>
       <p id="showDetail"></p>
    </div>
    <ul class="third-pay">
        <li class="clearfix"><i></i>微信<span class="round"><i class="round"></i></span></li>
    </ul>
    <a id="payButton" href="####" class="confirm-pay">确认支付</a>
<!--支付成功：$('.pay-success').show()-->
    <div class="pay-success">
        <div class="pay-shadow"></div>
        <div class="pay-content vertical-t">
            <div class="pay-detail">
                <p>支付成功</p>
                <a class="toMyOrder" href="javascript:void(0)">请到我的订单中查看</a>
            </div>
        </div>
    </div>
<!--支付失败: $('.pay-error').show()-->    
    <div class="pay-error">
        <div class="pay-shadow"></div>
        <div class="pay-content vertical-t">
            <div class="pay-detail">
                <p>支付失败</p>
                <a id="rePay" href="javascript:void(0)">重新支付试试？</a>
                <a class="toMyOrder" href="javascript:void(0)">到我的订单中查看</a>
            </div>
        </div>
    </div>    
</div>    
</body>
</html>
