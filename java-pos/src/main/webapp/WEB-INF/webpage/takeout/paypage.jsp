<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
 	<script type="text/javascript">
 		function dosubmit(){
 			var password = $('#password').val();
 			if(password == "" || password == undefined || password == null){
 				$('#errMsg').show();
 				$('#password').focus();
 				return false;
 			}
 			
 			$.ajax({
 				url: "takeOutController.do?pay&time=" + Math.round(new Date().getTime()/1000),
 				data:{
 					orderid: "${order.id}"
 				},
 				success:function(data){
 					var d = JSON.parse(data);
 					window.location.href = "takeOutController.do?payresult&issuccess="+ d.success + "&orderid=" + d.obj;
 				}
 			});
 		}
 		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
 	</script>
 	
  </head>
  
  <body>
       <div style="background-color:white;height:180px;padding-top:10px;padding-left:15px;border-radius: 10px;">
       		<div align="center" style="font-size:23px;margin-bottom:15px;font-weight:bold;color:#CD6600">交易信息</div>
       		
       		<div style="color:#636363;margin-bottom:10px;font-size:18px;">
       			订单号：${order.payId }
       		</div>
       		<div style="color:#636363;margin-bottom:10px;font-size:18px;">
       			总金额：<font style="color:red;font-weight:bold;font-size:25px;">￥ ${order.origin } 元</font>
       		</div>
       		<div style="color:#636363;margin-bottom:5px;font-size:18px;">
       			支付密码：<input type="password" id="password" name="password" style="font-size:18px;width:150px;">
       			<div id="errMsg" style="color:#A52A2A;font-weight:bold;display:none;">请输入支付密码</div>
       		</div>
       </div>
       
       <div style="margin-top:20px;">
   			<a href="#" class="btn_common ok_btn" onclick="dosubmit();">立即支付</a>
       </div>
       
       <div style="margin-top:20px;" align="center">
       		<a href="takeOutController.do?orderList" style="font-size:18px;color:#636363;">订单列表</a>&nbsp;&nbsp;&nbsp;
       		<a href="takeOutController.do?waimai&userid=${userid}&merchantid=${merchantid}" style="font-size:18px;color:#636363;">返回点餐</a>
       </div>
       
  </body>
</html>
