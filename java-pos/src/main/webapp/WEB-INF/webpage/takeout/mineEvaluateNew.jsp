<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html style="background:#fff">
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
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/order.css?version=${webVersion}" />
	
</head>
<body>
<div class="wrapper">   
    <div class="index_head">
        <div class="common_bar"><a href="javascript:void(0)" class="back"></a><p>添加评价</p></div>    
    </div>
    <div class="addevaluate">
    	<input type="hidden" name="orderId" value="${orderId}">
    	<input type="hidden" name="merchantId" value="${merchantId}">
    	<input type="hidden" name="courierId" value="${courierId}">
    	
        <div class="wholeEvaluate">
            <h4>整体评价</h4>
            <div class="valuateDemo">
                <div id="generalEvaluationId" class="target-demo"></div>
                <div id="function-hint" class="hint"></div>
            </div>
            <textarea name="" id="" cols="30" rows="6" class="contentEaluate" placeholder="亲，对这次点外卖的整体体验如何呢，不爽的话吐槽一下吧！"></textarea>
        </div>
        <div class="kdEvaluate">
            <h4>对快递员的表现</h4>
            <div class="valuateDemo">
                <div id="courierEvaluationId" class="target-demo"></div>
                <div id="function-hint1" class="hint"></div>
            </div>
            <textarea name="" id="" cols="30" rows="6" class="contentEaluate" placeholder="亲，快递员这么辛苦，想对他说点什么吗？跟他表白也行！"></textarea>            
        </div>        
    </div>
    <div class="subimtEvaluate"><a href="javascript:void(0)">提交</a></div>
    <!--   提交评论弹出页面    -->
    <div class="evaluateBox">
        <div class="bonusPointClose">
            <a href="${basePath}/takeOutController.do?mineWaitingEvaluates"></a>
        </div>
        <div class="bonusPoint">         
            <!-- <h3>恭喜你</h3>
            <div>
               <p>20</p><span>积分</span>
            </div>
            <p>已领取</p> -->
        </div>
        <div class="bonusPointBtn">
            <p>1号外卖给你最赤裸裸的优惠</p>
            <a href="${basePath}/takeOutController.do?merchantList" class="toConsume">去消费</a>
        </div>
    </div>
</div>

    <script type="text/javascript" src="${basePath }/plug-in/wxIndex/js/jquery.raty.min.js"></script>
	<script type="text/javascript" src="${basePath }/plug-in/taskout/js/wmraty.js"></script>
	<script type="text/javascript">
		window.history.forward(1);
		var basePath="${basePath}";
		var omvList= "";
	 
		var orderListMng;
		var waiMaiRaty ;
		var geId = "#generalEvaluationId" ;
		var ceId = "#courierEvaluationId" ;
		function postAddEvaluate(){
			var orderId = $(".addevaluate").find("input:hidden[name='orderId']").attr("value");
			var merchantId = $(".addevaluate").find("input:hidden[name='merchantId']").attr("value");
			var courierId = $(".addevaluate").find("input:hidden[name='courierId']").attr("value");

			var generalEvaScore = 0;
			var courierEvaScore = 0;
			if(waiMaiRaty){
				generalEvaScore = waiMaiRaty.getRateScore(geId) || 0;
				courierEvaScore = waiMaiRaty.getRateScore(ceId) || 0;
			}
			
			var generalContent = $(".wholeEvaluate > .contentEaluate").val() || "";
			var courierContent = $(".kdEvaluate > .contentEaluate").val() || "";

			if(generalEvaScore <= 0){
				$.WapDialog.tip("亲，请勾选整体评价的星星");
				return;
			}

			if(courierEvaScore <= 0){
				$.WapDialog.tip("亲，请勾选快递员评价的星星");
				return;
			}
			
			$.WapDialog.lock({content:'正在提交...'});//锁屏
			//保存
			$.ajax({
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
						$('.bonusPoint').empty();
						var html = '';
						if(data.obj) {
							html +='<div class="hexagon" id="first"><div class="hiveTop"></div><div class="hiveCenter"></div><div class="hiveBottom"></div></div>';
							html +='<div class="hexagon" id="second"><div class="hiveTop"></div><div class="hiveCenter"></div><div class="hiveBottom"></div></div>';
							html +='<div class="hexagon" id="third"><div class="hiveTop"></div><div class="hiveCenter"></div><div class="hiveBottom"></div></div>';							
				            html += '<div class="hexagonOut"><h3>恭喜你</h3>';
				            html += '<div><p>'+data.obj+'</p><span>积分</span></div><p>已领取</p></div>';
						} else {
							html +='<div class="hexagon" id="first"><div class="hiveTop"></div><div class="hiveCenter"></div><div class="hiveBottom"></div></div>';
							html +='<div class="hexagon" id="second"><div class="hiveTop"></div><div class="hiveCenter"></div><div class="hiveBottom"></div></div>';
							html +='<div class="hexagon" id="third"><div class="hiveTop"></div><div class="hiveCenter"></div><div class="hiveBottom"></div></div>';														
							html += '<div class="hexagonOut"><h3>提醒</h3>';
							html += '<div><span>该评论</span></div><p>已评论</p></div>';
						}
				        $('.bonusPoint').append(html);
				        $('.evaluateBox').show();
						/* $.WapDialog.open({content:data.msg,ok:{show:false},times:1500});
						setTimeout(function(){
							window.location.href = basePath+'/takeOutController.do?mineWaitingEvaluates';
						},1500); */
					}else{
						$.WapDialog.tip(data.msg);
					}
				},
				error:function(e){
					$.WapDialog.close();
					$.WapDialog.tip("操作失败,请重试");
				}
			});
		}
			
		$(function(){
			$(".back").click(function(){
				if("${callbackUrl}"){
					window.location.href = basePath+"${callbackUrl}";
				}else{
					window.location.href = basePath+"/takeOutController.do?mineWaitingEvaluates";
				}
			});
			$(".subimtEvaluate").click(function(){
				postAddEvaluate();
			});
			//加载及显示评价数据
			waiMaiRaty = new WaiMaiRaty();
			waiMaiRaty.init(basePath);
			waiMaiRaty.displayRate(geId);
			waiMaiRaty.displayRate(ceId);
		});
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
		
	
	</script>
	
</body>

</html>