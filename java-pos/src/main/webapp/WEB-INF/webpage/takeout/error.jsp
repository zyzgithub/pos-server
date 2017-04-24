<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
	<meta http-equiv="refresh" content="1;url=${basePath}/takeOutController.do?merchantList">
	<style type="text/css">
	ol{list-style: none;width:100%;}
	li{line-height: 3em;width:100%;}
	a{color: #A59D9D;font-size: 1.2em;width:100%;text-align: center;display: block;}
	</style>
	<script type="text/javascript">
	var basePath="${basePath}";
	$(function(){
		$(".backBtn,.head_title,#btnBack").click(function(){
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
		<div  class="head_back">
			<a href="javascript:void(0);" class="backBtn">&nbsp;</a>
		</div>
  		<div class="head_l">
	  		<span class="head_title" >返回</span>
  		</div>
	</div>
 	<div class="main">
 		<div class="msg">
	 		<c:choose>
	 			<c:when test="${not empty msg }">
	 				${msg}
	 			</c:when>
	 			<c:otherwise>
			   		<p>
			   			<c:choose>
			   				<c:when test="${errorCode  eq '500'}">抱歉！很不幸的告诉你，程序出错了！</c:when>
			   				<c:when test="${errorCode == 400}">你所访问的页面不存在或者已经被管理员删除！</c:when>
			   				<c:when test="${errorCode  eq 'exception'}">抱歉！出错了，服务器开小差了！</c:when>
			   				<c:otherwise>抱歉！出错了！</c:otherwise>
			   			</c:choose>
			   			<br/>给你带来的不便我们致以承挚的歉意！
			   		</p>
	 			</c:otherwise>
	 		</c:choose>
 		</div>
 		<div class="clear"></div>
 		<br/>
 		<div>
   			<h3>请尝试一下操作</h3><br/>
	   		<ol>
	   			<li><a class="line line_top" href="javascript:window.location.reload();" >刷新</a></li>
	   			<li><a class="line" href="${basePath }/takeOutController.do?merchantList" >首页</a></li>
	   			<li><a class="line" href="javascript:void(0);"  id="btnBack">返回</a></li>
	   		</ol>
   		</div>
   	</div>
  </body>
</html>
