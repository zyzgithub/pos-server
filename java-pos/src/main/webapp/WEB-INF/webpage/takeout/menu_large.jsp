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
		        		
		        		<li class="list_item <c:if test="${status.index==0}">active</c:if>"><a typeid="${t.id }" href="javascript:void(0)">${t.name}</a></li>
		        	</c:forEach>
		        </ul>
		    </div>
			<div class="shopmenu-list">
		    	<c:forEach var="t" items="${types}" varStatus="status">
		    		<c:if test="${status.index==0}">
		        	<ul class="listgroup" id="_${status.index}">
		        		<li><h2 class="words">${t.name}</h2></li>
		        		<c:forEach var="menu" items="${menus.get(status.index)}">
		        			<li class="clearfix">
		                    <div class="list-img"><img myurl="" src="${menu.picUrl }@55w" alt="" /></div>
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
		        		<li>
		        			<div typeid="${t.id}" page="${page }" style="color:black;" class="msg loading_menu">点击加载更多</div>
		        		</li>
		        	</ul>
		        	</c:if>
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
            <div id="loading_div" class="msg">点击加载更多</div></div>  
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
        
        function loadPageMenu(mid, tid, page, callback){
        	var url = menuManage.basePath + "/takeOutController/menuByPage.do";
        	$.get(
	            url,
	            {
	                "merchantId" : mid,
	                "menuTypeId" : tid,
	                "page" : page,
	                "pageSize" : 20
	            }, 
	            function(data) {
	            	if(callback) callback(data);
	            }, 
	            "json"
           );
        }
        
        function loadMenuClick(btn){
        	var typeid = $(btn).attr("typeid");
            var newPage = page + 1;
            if(loadEnd && typeid == loadTypeId){
                $(btn).text('已加载全部');
                return;
            }
            
            $(btn).text('正在加载中...');
            if(isStartLoad) return;
            isStartLoad = true;
            var thisDiv = $(btn);
            loadTypeId = typeid;

            loadPageMenu(merchantId, typeid, newPage, function(data){
            	isStartLoad = false;
                if(data && data.state == "success") {
                	
                	var ids = "";
                    $.each(data.obj, function(i, v){
                    	var html = getItemHtml(v);
                    	thisDiv.parent().before($(html));
                    	ids += ","+v.id;
                    });
					page = newPage;//当前页切换新的一页
					thisDiv.text('点击加载更多');
                }
                else if(data){
                    if(data.stateCode == "02"){//最后页 进行记录
                    	loadEnd = true;
                    	thisDiv.text('已加载全部');
                    }
                }
            });
		}
        
        //分页加载菜单
        var isStartLoad = false;
        var loadTypeId = 0;
        var loadEnd = false;
        var page = 1;
        var merchantId = "${mer.id}";
        
        //点击左边分类 清空菜单后重新加载当前分类的第一页
        $('.list_item').click(function(){
        	var typeid = $(this).find('a').attr("typeid");
        	$('.listgroup').empty();//清空菜单
        	$('.listgroup').append('<li><h2 class="words">'+$(this).find('a').html()+'</h2></li>');//增加标题
        	$('.listgroup').append('<li><div typeid="'+ typeid +'" page="0" style="color:black;" class="msg loading_menu">点击加载更多</div></li>');
        	page = 0;
        	loadTypeId = typeid;
        	isStartLoad = false;
        	loadEnd = false;
        	$('div.loading_menu').unbind('click');//解绑
        	$('div.loading_menu').bind('click', function(){ loadMenuClick($(this)); });//绑定点击事件
        	$('div.loading_menu').click();
        });
        //点击加载更多 分页加载数据]
        $('div.loading_menu').unbind('click');
        $('div.loading_menu').bind('click', function(){ loadMenuClick($(this)); });
        
        var mtop = $('.list_item.active').offset().top;
        var sctop = $('.lb').scrollTop();
        var lbtop = sctop + mtop - $(window).height()/2;
        $('.lb').scrollTop(lbtop);
	});
	
	function getItemHtml(v){
	var html  =	'<li class="clearfix">';
        html += 	'<div class="list-img"><img src="'+v.picUrl+'@55w" alt="" /></div>';
        html += 	'<div class="list-content">';
        html += 		'<div class="title">';
        html += 			'<p class="words">'+v.name+'</p>';
        if(v.promoting)	{
        					html += '<span class="less"></span>';
        }
        html += 		'</div>';
        html += 		'<div class="description"><span>销量'+v.buyCount+'</span><p class="spare">'+v.proDesc+'</p></div>';
        html += 		'<p class="list-price">'+v.price+'元/份</p>';
        html += 	'</div>';
        html += 	'<div class="item-add">';
        if(v.repertory > 0){
        	html += 	'<button class="-minus" id="minus'+v.id+'" onclick="menuManage.add(\''+v.id+'\', \''+v.name+'\', -1, '+v.price+', '+v.repertory+', '+v.promoting+', '+v.limitCount+')"></button>';
        	html += 	'<input type="text" class="item-count sy_num" id="count'+v.id+'" readonly="readonly" value="0" />';
        	html += 	'<button class="-plus" id="plus${menu.id}" onclick="menuManage.add(\''+v.id+'\', \''+v.name+'\', 1, '+v.price+', '+v.repertory+', '+v.promoting +', '+v.limitCount +')"></button>';
        }else{
        	html += 	'<span>售完</span>';
        }
        html += 	'</div>';
        html += '</li>';
        return html;
	}
</script> 
	
</body>
</html>
