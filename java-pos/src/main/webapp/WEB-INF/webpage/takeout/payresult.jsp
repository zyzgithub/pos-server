<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
	<script type="text/javascript">
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
        WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
    });
	</script>
  </head>
  
  <body>
  		<div class="head">
  			<div class="head_back" >
				<a href="${basePath }/takeOutController.do?waimai&merchantid=${merchantid}"  class="head_title" >
					<em class="backBtn_ico"></em>返回
				</a>
			</div>
	  		<div class="head_l">
		  		<span class="head_title" >支付结果</span>
	  		</div>
	  		<div class="head_r">
	  			<a href="${basePath }/takeOutController.do?orderList" class="head_title">我的订单</a>
	  		</div>
		</div>
    	<div class="main">
    		<div class="payMsg">
    			<c:choose>
    				<c:when test="${result.stateCode eq '01' }">
    					操作失败,原因：${result.msg eq '重复支付'?'亲,你已经支付过该订单了哦!':result.msg}
    				</c:when>
    				<c:otherwise>
    					<img src="${basePath }/plug-in/taskout/images/correct.png" style="width:50px;height:50px;">
    					<font style="font-size:20px;color:#458B00">恭喜您，支付成功啦！</font>
    				</c:otherwise>
    			</c:choose>
    			<div style="margin-top:20px;">
  					<a href="${basePath }/takeOutController.do?waimai&merchantid=${merchantid}" class="btn_common ok_btn">继续点餐</a>
 				</div>
    		</div>
    	</div>
  </body>
</html>
