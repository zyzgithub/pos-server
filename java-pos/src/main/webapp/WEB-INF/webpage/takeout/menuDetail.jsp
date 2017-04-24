<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="./common.jsp"  %>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>1号外卖</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
	<meta content="private,must-revalidate" http-equiv="Cache-Control">
	<meta content="telephone=no, address=no" name="format-detection">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<script src="http://api.map.baidu.com/api?v=1.3&ak=hf7GGTXwqxndSLLYBfrKVwZh" type="text/javascript"></script>
	<script type="text/javascript" src="${basePath}/plug-in/taskout/js/Util.js?version=${webVersion}"></script>
	<script type="text/javascript" src="${basePath}/plug-in/taskout/js/menu.js?version=${webVersion}"></script>
    <script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/main.js?version=${webVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
</head>
<body>
<div class="wrapper">   
    <div class="index_head" style="background:#fff">
        <div class="common_bar clearfix">
           <a href="${basePath}/takeOutController.do?merchantList" class="back"></a>
           <p class="words">菜品详情</p>
        </div>
    </div>
    
    <c:if test="${menu != null}">
	    <div class="menu_lists">
				<div class="shopmenu-list">
			        	<ul class="listgroup">
			        			<li class="clearfix">
			                    <div class="list-img"><img src="${menu.picUrl }@55w" alt="" /></div>
			                    <div class="list-content">
			                        <div class="title">
			                        	<p class="words">${menu.name}</p>
			                        	<c:if test="${menu.promoting }"><span class="less"></span></c:if>
			                        </div>
			                        <div class="description"><span>销量${menu.buyCount}</span><p class="spare">${menu.proDesc}</p></div>
			                        <p class="list-price">${menu.price}元/份</p>
			                    </div>		  
		                     <div class="item-add">
			                   		<c:choose>
			                    		<c:when test="${menu.repertory > 0 }">
					                        <button class="-minus" id="minus${menu.id}" onclick="menuManage.add('${menu.id}', '${menu.name}', -1, ${menu.price}, ${menu.repertory}, ${menu.promoting }, ${menu.limitCount })"></button> 
					                       <input type="text" class="item-count sy_num" id="count${menu.id}" readonly="readonly" value="0" />
					                        <button class="-plus" id="plus${menu.id}" onclick="menuManage.add('${menu.id}', '${menu.name}', 1, ${menu.price}, ${menu.repertory}, ${menu.promoting }, ${menu.limitCount })"></button>
			                    		</c:when>
			                    		<c:otherwise>
			                    			<span>售完</span>
			                    		</c:otherwise>
			                    	</c:choose>
			                    
			                    </div>
			                </li>
			        	</ul>
			    	<div class="zhanwei"></div>
			    </div>		        	
	    </div>
	    <!-- 弹出大图 -->
	    <div class="msKeimgBox"></div>
	    
	    <div class="gwc_out"></div>
	    <div class="shopmenu-cart ">
	        <div class="shopmenu-cart-bar clearfix">
	            <div class="row-cart">
	                <i class="gwc"></i>
	                <span id="cartCount" style="display:none">0</span>
	            </div>
	            <div class="sqj">
	                <p class="price_sum" id="cartPrice">￥0.00</p>
	            </div>
	            <c:choose>
	            	<c:when test="${mer.display eq 'Y'}">
	            		<c:if test="${mer.deliveryBegin > 0}">
				            <a href="javascript:void(0)" class="cb-disable selectOK">差￥${mer.deliveryBegin}起送</a>
	            		</c:if>
	            		<c:if test="${mer.deliveryBegin == 0}">
				            <a href="javascript:void(0)" class="xhl selectOK">选好了</a>
	            		</c:if>
	            	</c:when>
	            	<c:otherwise>
			            <a href="javascript:void(0)" class="cb-disable">休息中</a>
					</c:otherwise>
	            </c:choose>
	        </div>
	    </div>
	
	    <div class="shadow_div_2"></div>
	    <div class="gwc_click">
	        <p>购物车<a href="javascript:void(0)" id="cleanCart">清空</a></p>
	        <div  id="cartshopList"></div>
	    </div>
    </c:if>
    
</div> 
<form action="${basePath}/takeOutController/addShopcart.do" method="post" id="addShopcart">
	<input type="hidden" name="json" id="shopcartJson">
	<input type="hidden" name="merchantId" value="${mer.id}">
	<input type="hidden" name="userId" value="${userId}">
</form>
<input type="hidden" id="lng" value="${location.lng }"> 
<input type="hidden" id="lat" value="${location.lat }"> 
<script type="text/javascript">
	var clickComment = false;
	var menuManage;
    $(function(){
    	$(".item-count").val(0);
		menuManage=new MenuManage();
    	menuManage.init("${basePath}", "${mer.id}", "${mer.deliveryBegin}");
		
    	$(".selectOK").click(function() {
    		menuManage.checkShopcart();
    	});
		
    	//清空购物车
    	$("#cleanCart").click(function() {
    		menuManage.cleanCartshop();
    	});
    	
        /* 点击遮罩购物车消失 */
        $('.shadow_div_2').click(function() {
            /* Act on the event */
            $(this).hide();
            $('.gwc_click').hide();
        });

        $('.row-cart').click(function() {
            /* Act on the event */
           $('.shadow_div_2').fadeIn();
           $('.gwc_click').fadeIn();
           menuManage.listCartshop();
        });   
       
        $('.list-img').click(function() {
            var url = $(this).children('img').attr('src');
            var Imgurl = url.substring(0, url.indexOf('@55w'));
            $('<div class="mskeLayBg"></div><div class="mskelayBox"><img src="'+Imgurl+'@400w" alt="" /><span class="mskeClaose"></span></div>').appendTo('.msKeimgBox');
            $('.msKeimgBox').fadeIn(50);
       });
        
        $('.msKeimgBox').click(function() {
            /* Act on the event */
            $(this).empty();
            $(this).fadeOut(30);
        });
        
        if($("#lng").val()=='' || $("#lat").val()=='') {
    		var postData = {};
    		// 使用百度API浏览器定位功能
    		var geolocation = new BMap.Geolocation();
    		// 获取当前位置坐标
    		geolocation.getCurrentPosition(function(r){	
    			//获取坐标成功
    	        if(this.getStatus() == BMAP_STATUS_SUCCESS ){
    	        	
    	        	 var myGeocoder = new BMap.Geocoder();
    	        	 
    	        	 postData.lng=r.point.lng;//经度
    	        	 postData.lat=r.point.lat;//纬度
    	        	 // 根据坐标获取当前地址信息
    	 	         myGeocoder.getLocation(new BMap.Point(r.point.lng, r.point.lat), function(rs){
    		 	         //当前地址（省、市、区、街道、门牌号）
    		 	         var addComp = rs.addressComponents;
    		 	         var surrounding = rs.surroundingPois;
    		 	         
    		 	         postData.city = addComp.city;
    		 	         
    		 	         if(surrounding && surrounding.length > 0) {
    		 	        	var surround = surrounding[0];
    		 	        	postData.address = surround.title;
    		 	         } else {
    		 	        	postData.address = addComp.street+addComp.streetNumber;
    		 	         }
    		 	        var url = "${basePath}/takeOutController/setlocation.do";
    		 	      	$.post(url, postData);
    		 	     }); 
    	        }
            });
    	}

	})
</script> 
	
</body>
</html>


