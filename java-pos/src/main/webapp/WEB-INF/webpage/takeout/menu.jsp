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
    <script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/jquery.nav.js?version=${webVersion}"></script>
    <script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/compent.js?version=${webVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
</head>
<body>
<div class="wrapper">   
    <div class="index_head" style="background:#fff">
        <div class="common_bar clearfix">
           <a href="${basePath}/takeOutController.do?merchantList" class="back"></a>
           <p class="words">${mer.title}</p>
           <a href="javascript:void(0)" class="collect" id="collect"></a> 
        </div>
       <ul class="menu-bar clearfix">
            <li ><span class="active">点菜</span></li>
            <li ><span id="comment">评价</span></li>            
            <li><span>店铺</span></li>
        </ul>
    </div>
    <div class="menu_lists">
    	<div class="menu_orders">
	        <c:if test="${not empty mer.notice}">
		        <div class="notice"><marquee scrolldelay="250" behavior="scroll" direction="left">${mer.notice}</marquee></div>
	        </c:if>    		
		    <div class="dc_sort">
		        <ul class="lb">
		        	<c:forEach var="t" items="${types}" varStatus="status">
		        		<li class="list_item <c:if test="${status.index==0}">active</c:if>"><a href="#_${status.index}">${t.name}</a></li>
		        	</c:forEach>
		        </ul>
		    </div>
			<div class="shopmenu-list">
		    	<c:forEach var="t" items="${types}" varStatus="status">
		        	<ul class="listgroup" id="_${status.index}">
		        		<li><h2 class="words">${t.name}</h2></li>
		        		<c:forEach var="menu" items="${menus.get(status.index)}">
		        			<li class="clearfix">
		                    <div class="list-img"><img myurl="${menu.picUrl }@55w" src="" alt="" /></div>
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
		        		</c:forEach>
		        	</ul>
		        </c:forEach>
		    	<div class="zhanwei"></div>
		    </div>		        	
    	</div>
        <div class="menu_evaluate">
            <div class="evaluate">
            <div class="evaluate-total">
                <span>总体评分</span>
                <p>
                	<c:forEach begin="1" end="5" var="s">
                       <i 
                       	<c:choose>
                    		<c:when test="${s <= score.score }">class="active "</c:when>
                    		<c:when test="${s-1 < score.score }">class="half_active"</c:when>
                       	</c:choose>
                       ></i>
                   	</c:forEach>
                </p>
                <span class="pingfenTatal">${score.score}分</span>
                <p class="tab-content">共${score.total}条</p>
            </div>
            <ul class="comment-list-all"></ul>
            <div id="loading_div" class="msg">点击加载更多</div>
            </div>            
        </div>    	
       <div class="menu_merchant">
            <div class="shop_pic_out ">
                <div class="shop_info clearfix">
                    <div class="shop_icon"><img src="${mer.logoUrl }"  alt="" /></div>
                    <div class="shop_detail">
                        <p class="words">${mer.title}</p>
                        <p>
                        	<c:forEach begin="1" end="5" var="s">
                        		<i 
		                       	<c:choose>
		                    		<c:when test="${s <= score.score }">class="active "</c:when>
		                    		<c:when test="${s-1 < score.score }">class="half_active"</c:when>
		                       	</c:choose>
		                       ></i>
                        	</c:forEach>
                        </p>
                    </div>
                </div>
                <div class="sc_info_div"></div>
                <ul class="sc_info clearfix">
                    <li>
                        <p>￥${mer.deliveryBegin}</p>
                        <span>起送价</span>
                    </li>
                    <li>
                        <p>${buycount}</p>
                        <span>销量</span>
                    </li>
                    <li>
                        <p>${score.score}</p>
                        <span>评价</span>
                    </li>            
                </ul>
            </div>
            <div class="shop_sj">
                <p class="words">${mer.notice }</p>
                <p>${mer.openTime }</p>
                <p>${mer.address}<a href="tel:${mer.mobile }"></a></p>
            </div>
            <ul class="shop_zxzf">
                <li class="clearfix">
                    <i></i><p>营业执照</p><a href="javascript:void(0)">上传中</a>          
                </li>
                <li class="clearfix">
                    <i></i><p>卫生许可证</p><a href="javascript:void(0)">上传中</a>          
                </li>
            </ul>             
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
            	<c:when test="${mer.opening}">
            		<c:if test="${mer.deliveryBegin > 0}">
			            <a href="javascript:void(0)" class="cb-disable selectOK">差￥${mer.deliveryBegin }起送</a>
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
    	
		menuManage=new MenuManage();
    	menuManage.init("${basePath}", "${mer.id}", "${mer.deliveryBegin}");
		
    	$('.lb').onePageNav();
    	
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
        
        $("#comment").click(function() {
        	if(!clickComment) {
	        	menuManage.listComment();
	        	clickComment = true;
        	}
        });
        
        $("#loading_div").click(function() {
	        menuManage.listComment();
        });
      
        $('.collect').click(function() {
            /* Act on the event */
         	$(this).toggleClass('onCollect');
            var url = "${basePath}/wxfavoritesController/add.do";
            $.post(url, {"merchantId":"${mer.id}"});
     	});  
        
        $.get("${basePath}/wxfavoritesController/get.do", {"merchantId":"${mer.id}"}, function(data) {
        	if(data.state  == "success") {
        		$('.collect').addClass('onCollect');
        	}
        }, "json");
        
        function loadShowItemImg(){
        	var windowHeight = $(window).innerHeight();//可见区域高度
        	var scrollHeight = $(window).scrollTop();//滑动后不可见高度--可见区域高度开始
        	var endHeight = scrollHeight + windowHeight;//可见区域结束(已隐藏高度+窗口高度)
        	//找到区域内的所有菜单
        	var start = parseInt((scrollHeight - 53)/83 - 1);//可见开始菜单
        	var end = parseInt((endHeight - 85)/83 + 1);//可见结束菜单
        	if(start < 0) start = 0;
        	//可见菜单如果没有src进行图片加载
        	for(i = start;i <= end;i++){
        		var itemImg = $($('li.clearfix img')[i-1]);
        		var src = itemImg.attr("src");
        		var myurl = itemImg.attr("myurl");
        		//console.log(" item:" + i + "--src:" + src + "---url:" + myurl);
        		if(!src){
        			if(myurl){
        				//console.log("item:" + i + "---------first load");
        				itemImg.attr("src", myurl)
        			}
        		}
        	}
        }
        
        $(window).scroll( function() { loadShowItemImg(); });//滑动事件监听 自动加载可视区域图片
        
        loadShowItemImg();//第一次load
	})
</script> 
	
</body>
</html>


