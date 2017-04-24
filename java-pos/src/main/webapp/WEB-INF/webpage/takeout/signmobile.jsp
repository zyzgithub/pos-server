<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="./common.jsp" %>
   	<title>${baseTitle }</title>
	<script type="text/javascript" src="${basePath }/plug-in/taskout/js/signmobile.js"></script>
	<script type="text/javascript">
		var basePath="${basePath}";
		$(function(){
			$(".head_back").click(function(){
				if(document.referrer){
					window.history.back();
				}else{
					window.location.href=basePath+"/takeOutController.do?merchantList";
				}
			});
			signMobileMng=new SignMobileMng();
			signMobileMng.init(basePath);
		});
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
	</script>
</head>

<body>
	<div class="head">
		<div class="head_back" >
			<a href="javascritp:void(0);"  class="head_title" >
				<em class="backBtn_ico"></em>返回
			</a>
		</div>
  		<div class="head_l">
	  		<span class="head_title" >绑定手机</span>
  		</div>
	</div>
	<div class="main">
		<form id="bindForm">
			<%-- <input type="hidden" id="goods" name="goods" value="${goods}"> --%>
			<div class="s_m" style="margin-top:50px;">
				<div class="form_item">
					<span class="form_item_title">手机号：</span>
					<input type="text" id="mobile" name="mobile"  class="input_text" />
					<a href="javascript:void(0);"  id="sendBtn" class="sendBtn" >获取验证码</a>
				</div>
				<div  class="form_item" style="border-top:none;">
					<span class="form_item_title">验证码：</span>
					<input type="text" id="code" name="code"   class="input_text" />
				</div>
				<div id="msg" class="msg" style="color:red;text-align: center;"></div>
			</div>
			<div style="margin-top:50px;">
				<a href="javascript:void(0);" class="btn_common ok_btn"  id="bindBtn" >绑定</a>
	     	</div>
     	</form>
    </div>
</body>
</html>
