<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="./common.jsp"  %>
   	<title>${baseTitle }</title>
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
	  		<span class="head_title" >个人中心</span>
  		</div>
	</div>
 	<div class="main self">
	   	<div class="s_head">
	   		<div class="s_head_img">
	   			<c:choose>
					<c:when test="${not empty sessionScope.weixinUserInfo and not empty sessionScope.weixinUserInfo.headimgurl}">
						<img  src="${sessionScope.weixinUserInfo.headimgurl }" />
					</c:when>
					<c:when test="${not empty user.photoUrl}">
						<img  src="${rootPath}${user.photoUrl}" />
					</c:when>
					<c:otherwise>
						<img  src="${basePath }/plug-in/taskout/images/ico_self.png" />
					</c:otherwise>
				</c:choose>
	   		</div>
	   		<div class="s_head_msg">
	   			<div class="s_u_n">${user.username}<br/></div>
	   			<div class="s_m_l">
	   				<a href="javascript:void(0);">余额：${user.money}元</a>
					<a href="javascript:void(0);">积分：${user.score}</a>
	   			</div>
	   			<div class="clear"></div>
	   		</div>
		</div>
		<a class="s_line" href="${basePath }/takeOutController.do?orderList">
			<em class="ico ico_order"></em>
			<span>我的订单</span>
			<em class="right_icon"></em>		
		</a>
		<a class="s_line" href="${basePath }/takeOutController.do?myCard">
			<em class="ico ico_card"></em>
			<span>我的代金券</span>		
			<em class="right_icon"></em>
		</a>
		<a class="s_line" href="${basePath }/takeOutController.do?signmobile" >
			<em class="ico ico_mobile"></em>
			<c:choose>
				<c:when test="${not empty user.mobile }"><span> ${user.mobile }(已绑定)</span></c:when>
				<c:otherwise><span>未绑定手机</span></c:otherwise>
			</c:choose>
			<em class="right_icon"></em>
		</a>
		<!-- 
		<a class="s_line" href="javascript:void(0);">
			<em class="ico ico_drawback"></em>
			<span>退款</span>		
			<em class="right_icon"></em>
		</a>
		 -->
		<div style="margin-top:20px;">
   			<a href="${basePath }/takeOutController.do?merchantList" class="btn_common ok_btn" onclick="dosubmit();">开始点餐</a>
       </div>
   	</div>
</body>
</html>
