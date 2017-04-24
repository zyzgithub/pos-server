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
	  		<span class="head_title" >我的代金券</span>
  		</div>
	</div>
 	<div class="main">
		 <c:if test="${empty cardList or fn:length(cardList) < 1}">
	  	 	<div style="height:50px;padding-top:25px;background-color:white" align="center">
	  	 		暂无代金券
	  	 	</div>
	  	 </c:if>
	     
	     <c:forEach items="${cardList}" var="vo">
	     	<div style="color:#636363;padding-top:10px;padding-left:10px;padding-bottom:10px;width:100%;background-color:white;border-bottom:1px dashed #CFCFCF;">
				<div>
	     			<table style="width:100%;color:#636363;font-size:20px;">
	     				<tr>
	     					<td style="width:120px;">
	     						<img src="${basePath }/plug-in/taskout/images/j.png" style="width:100px;height:100px;">
	     					</td>
	     				
	     					<td style="color:#1C86EE;font-weight:bold;font-size:14px;">
								<table>
									<tr>
										<td>
											<c:choose>
												<c:when test="${fn:length(vo.title) > 10}">
													<c:out value="${fn:substring(vo.title, 0, 10)}..." />
												</c:when>
												<c:otherwise>
													<c:out value="${vo.title}" />
												</c:otherwise>
											</c:choose>
										</td>
										<td  style="color:#EE7942;font-weight:bold;">
				     						<c:choose>
				     							<c:when test="${vo.consume eq 'Y'}">已使用</c:when>
				     							<c:when test="${vo.overTime }">已过期</c:when>
				     							<c:when test="${vo.consume eq 'N'}">未使用</c:when>
				     						</c:choose>
				     					</td>
									</tr>
									
									<tr>
										<td colspan="2" style="height:23px;">
								     		金额：<font style="color:red;font-weight:bold;font-size:14px;"> ${vo.credit} </font>元
							     		</td>
									</tr>
									
									<tr>
										<td colspan="2" style="height:23px;">
											券号：${vo.code}
										</td>
									</tr>
									
									<tr>
										<td colspan="2">
											到期时间：<font>${vo.endDate}</font>
										</td>
									</tr>
								</table>
	     					</td>
	     				</tr>
	     			</table>
	     		</div>
	     	</div>
	     </c:forEach>
	 </div>
	</body>
</html>
