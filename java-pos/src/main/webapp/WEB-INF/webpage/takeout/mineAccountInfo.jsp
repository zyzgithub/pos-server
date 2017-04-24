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
	
	<script type="text/javascript">
		var basePath="${basePath}";
		$(function(){
			$(".back").click(function(){
				if(document.referrer){
					window.history.back();
				}else{
					window.location.href=basePath+"/takeOutController.do?mine";
				}
			});
		});
	</script>
</head>

<body style="background:#ececec">
	<div class="wrapper">
	  <div class="index_head">
	    <div class="common_bar">
	      <a href="#" class="back"></a><p>账户信息</p>
	    </div>
	  </div>
	  <div class="mine_account">
	     <ul>
	      <li class="clearfix ">
	        <span>头像</span>
	        	<div class="mine_account_icon">
		            <c:choose>
		            	<c:when test="${empty photourl }">
		            		<img src="${basePath}/plug-in/wxIndex/images/mine_default-photo.png"  alt="" />
		            	</c:when>
		            	<c:otherwise><img src="${photourl }"  alt="" /></c:otherwise>
		            </c:choose>
	            </div>
	      </li>
	      <li class="clearfix ">
	        <span>我的昵称</span>
	        <p>${nickname }</p>
	      </li> 
	      <li class="clearfix ">
	        <span>绑定手机</span>
            <c:choose>
				<c:when test="${not empty mobile }"><p> ${mobile}
					<a href="${basePath}/takeOutController.do?mineMobileBind" style="color:#FC3F2D">变更</a>
				</p></c:when>
				<c:otherwise><p><a href="${basePath}/takeOutController.do?mineMobileBind" style="color:#FC3F2D">点击绑定<i></i></a></p></c:otherwise>
			</c:choose>
	      </li> 
	      <li class="clearfix ">
	        <span>编号</span>
	        <p>${openid }</p>
	      </li>	           
	    </ul>
	  </div>
	
	</div>
</body>

</html>