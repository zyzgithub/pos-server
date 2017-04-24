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
	
</head>

<body style="background:#ececec">
<div class="wrapper">
  <div class="index_head">
    <div class="common_bar">
      <a href="#" class="back"></a><p>充值</p>
    </div>
  </div>

  <div class="mine_cz clearfix">
      <span>充值金额</span><input id="rechargeTextId" type="text" placeholder="请输入充值金额"  maxlength="10" />
  </div>
  <div class="mine_czfs">
    <ul>
      <li>微信支付<i class="cur"></i></li>
    </ul>
  </div>
  <a href="#" class="mine_submit">确认充值</a>
  
</div>



    <script type="text/javascript" src="${basePath}/plug-in/taskout/js/wxrecharge.js"></script>
	<script type="text/javascript">
		var basePath="${basePath}";
		var wxRecharge ;
		var rechargeTextId = "#rechargeTextId";
		function rechargeSubmit(){
			var rechargeAmount = $(rechargeTextId).val();//充值金额
			var testIsValid = /^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/;
			if(rechargeAmount){
				var flag = testIsValid.test(rechargeAmount);
				if(!flag){
					$.WapDialog.tip("请输入有效的金额");
					return;
				}
				//console.log(rechargeAmount);
			}else{
				$.WapDialog.tip("请输入有效的金额");
				return;
			}
			//$.WapDialog.tip("亲，评价太短了，可以评价下快递员的服务态度等。。。");
			//return;
			
			//$.WapDialog.lock({content:'正在保存评价...'});//锁屏
			//保存
			/*$.ajax({
				url: basePath+'/takeOutController.do?mineEvaluateSave&time=' + Math.round(new Date().getTime()/1000),
				data:{
					orderId: orderId,
					courierId: courierId,
					courierEvaScore: courierEvaScore,
					courierContent: courierContent,
					merchantId: merchantId,
					generalEvaScore: generalEvaScore,
					generalContent: generalContent,
					commentDisplay: 'Y'
				},
				type: "POST",
				dataType:'json',
				contentType:"application/x-www-form-urlencoded; charset=utf-8",
				success: function(data){
					$.WapDialog.close();
					if(data.success == true){
						$.WapDialog.open({content:'保存成功,3秒后将自动返回待评价列表',ok:{show:false},times:3000});
						setTimeout(function(){
							window.location.href = basePath+'/takeOutController.do?mineWaitingEvaluates';
						},3000);
					}else{
						$.WapDialog.tip(data.msg);
					}
				},
				error:function(e){
					$.WapDialog.close();
					$.WapDialog.tip("操作失败,请重试");
				}
			});*/
		}
			
		$(function(){
			wxRecharge = new WxRecharge();
			wxRecharge.init(basePath);
			wxRecharge.amountInput(rechargeTextId);

			$(".back").click(function(){
				if("${callbackUrl}"){
					window.location.href = basePath+"${callbackUrl}";
				}else{
					window.location.href = basePath+"/takeOutController.do?mine";
				}
			});
			$(".mine_submit").click(function(){
				//submit
				rechargeSubmit();
			});
		});
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
	
		function onBridgeReady() {
			var appId = $("#appId").val();
			/*
			var timeStamp = urlparameter("timeStamp");
			var nonceStr = urlparameter("nonceStr");
			var pg = urlparameter("pg");
			var signType = urlparameter("signType");
			var paySign = urlparameter("paySign");
			*/
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				//"appId" : appId, //公众号名称，由商户传入       
				//"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数       
				//"nonceStr" : nonceStr, //随机串       
				//"package" : "prepay_id=" + pg,//订单详情扩展字符串
				//"signType" : signType, //微信签名方式:       
				//"paySign" : paySign //微信签名   
			},

			function(res) {
				if (res.err_msg == "get_brand_wcpay_request:ok") {
					alert("支付成功");
				} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。   
			});
		}

		//微信公众号支付
		function wxgzhpay() {
			if (typeof WeixinJSBridge == "undefined") {
				if (document.addEventListener) {
					document.addEventListener('WeixinJSBridgeReady',
							onBridgeReady, false);
				} else if (document.attachEvent) {
					document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
					document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
				}
			} else {
				onBridgeReady();
			}
		}
	</script>

</body>

</html>