<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
  <head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
	<script type="text/javascript">
	$(function(){
		var basePath="${basePath}";
		var merchantid="${merchantDetail.id}";
		$(".head_back").click(function(){
			if(document.referrer){
				window.history.back();
			}else{
				window.location.href=basePath+"/takeOutController.do?waimai&merchantid="+merchantid;
			}
		});
		$('#notice').toggle(function(){
			$('#notice_msg').css({'white-space':'normal','width':'90%'});
		},function(){
			$('#notice_msg').css({'white-space':'nowrap','width':'75%'});
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
	  		<span class="head_title" >商家详情</span>
  		</div>
	</div>
    <div class="main div_m">
    	<div class="div_m_h">
    	 	<div class="div_m_log_img">
    	 		<img  src="${merchantDetail.logo_url }">
    	 	</div>
    	 	<div class="div_m_r">
    	 		<div class="div_m_title">${merchantDetail.title}</div>
    	 		<div class="div_m_time">营业时间：${merchantDetail.start_time} - ${merchantDetail.end_time}</div>
    	 		<div class="div_m_d">${merchantDetail.delivery_begin}元起送</div>
    	 	</div>
    	 	<div class="clear"></div>
    	</div>
    	<a class="div_m_l" id="notice">
    		<em class="notice_ico"></em>
    		<span id="notice_msg">${not empty merchantDetail.notice ? merchantDetail.notice : '暂无公告'}</span>
    		<em class="right_icon"></em>
    	</a>
   	 	<%-- <a class="div_m_l"  href="${basePath }/takeOutController.do?toMap&lng=${lng}&lat=${lat}&name=${merchantDetail.title}&address=${merchantDetail.address}"> --%>
   	 	
   	 	<a class="div_m_l"  href="http://api.map.baidu.com/marker?location=${lat},${lng}&title=${merchantDetail.address }&content=${merchantDetail.address}&output=html">
   	 		<em class="local_ico"></em> 
   	 		<span>${merchantDetail.address}</span>
   	 	 	<em class="right_icon"></em>
   	 	</a>
   	 	<a class="div_m_l" href="tel:${merchantDetail.mobile}">
	   	 	<em class="phone_icon"></em> 
	   	 	<span>${merchantDetail.mobile}</span> 
	   	 	<em class="right_icon"></em>
   	 	</a>
   	 	<div class="clear"></div>
   	 	<div class="div_m_btn">
   	 		<a href="${basePath }/takeOutController.do?waimai&merchantid=${merchantDetail.id}">叫外卖</a>
   	 	</div>
    </div>
  </body>
</html>
