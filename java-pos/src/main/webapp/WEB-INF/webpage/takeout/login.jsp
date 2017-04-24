<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
	<script type="text/javascript">
	var basePath="${basePath}";
	$(function(){
		$(".head_back").click(function(){
			if(document.referrer){
				window.history.back();
			}else{
				$('#btnBack').hide();
				window.location.href=basePath+"/takeOutController.do?merchantList";
			}
		});
	});
	</script>
  </head>
  
  <body>
  <div class="head">
		<div class="head_back" >
			<a href="javascript:void(0);"  class="head_title" >
				<em class="backBtn_ico"></em>返回
			</a>
		</div>
  		<div class="head_l">
	  		<span class="head_title" >操作失败</span>
  		</div>
	</div>
 	<div class="main">
 		<form id="loginForm">
 			<input type="hidden" id="backUrl" value="${backUrl }" />
 		</form>
	   	<div class="msg">你的登录凭证已过期或未登录,请重新登录<br/>如是微信内请关闭后重开即可</div>
   	</div>
  </body>
</html>
