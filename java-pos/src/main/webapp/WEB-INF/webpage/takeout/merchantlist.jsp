<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html  style="background:#fff;">
<head>
<%@ include file="./common.jsp"%>
<title>${baseTitle }</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"
	name="viewport">
<meta content="private,must-revalidate" http-equiv="Cache-Control">
<meta content="telephone=no, address=no" name="format-detection">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent">  
<script src="http://api.map.baidu.com/api?v=1.3&ak=hf7GGTXwqxndSLLYBfrKVwZh" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/swiper.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/main.js?version=${webVersion}"></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/merchantlist.js?version=${webVersion}"></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/orderlist.js?version=${webVersion}"></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/Util.js?version=${webVersion}"></script>
<script type="text/javascript">
		var merchantManager;//商家管理
		var orderManager;
		
		// 微信js-sdk
		/* wx.config({
		    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: "${jsTicket.appId}", // 必填，公众号的唯一标识
		    timestamp: "${jsTicket.timestamp}", // 必填，生成签名的时间戳
		    nonceStr: "${jsTicket.nonceStr}", // 必填，生成签名的随机串
		    signature: "${jsTicket.signature}",// 必填，签名，见附录1
		    jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.ready(function(){
		    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
			wx.getLocation({
			    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
			    success: function (res) {
			        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
			        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
			        var speed = res.speed; // 速度，以米/每秒计
			        var accuracy = res.accuracy; // 位置精度
			        alert("lng:"+longitude+"   lat:"+latitude);
			    }
			});
		}); */
		
		$(function(){
		 
			orderManager=new OrderListMng();
			orderManager.lastedOrder("${basePath}");
			
			//初始化数据
			 merchantManager=new MerchantManager();
			 merchantManager.init({basePath:"${basePath}"});
			 
			  //点击加载更多
			  $("#loading_div").click(function(){
				  merchantManager.load();
			  });
			  //重新定位
			  $('#head_title1').click(function(){
				  $("#merchantList").empty();
				  merchantManager.start=0;	 //开始行
				  merchantManager.pageSize=20;//每次加载数量
				  merchantManager.getInitPosition();
			  });
			
		});
		function selectMer(type, typeId, obj) {
			merchantManager.selectMerchant(type, typeId, obj);
		}
		
		function clickSelect(clickId, obj) {
			merchantManager.clickSelect(clickId, $(obj));
		}
	</script>
</head>
<body style="background:#fff;">
	<div class="wrapper">
	  <!-- 第三方banner -->
	  <div class="third-content">
	  	<a href="${basePath}/plug-in/wxIndex/demo/recruit.html"><img src="http://oss.0085.com/weixin/zp-banner01.png" width="100%" alt="招募私厨" /></a>
<%-- 	    <div class="swiper-container">
	        <div class="swiper-wrapper">
	            <div class="swiper-slide"></div>
	            <div class="swiper-slide"><a href="${basePath}/openapi/customer/toThirdPage.do"><img src="http://oss.0085.com/weixin/banner-02.png" width="100%" alt="i玩派" /></a></div>
	        </div>
	        <div class="swiper-pagination"></div>
	    </div> --%>   
	 </div>	
	  <!--  <a href="${basePath}/openapi/customer/toThirdPage.do"><img src="http://oss.0085.com/weixin/banner-02.png@450w" alt="" /></a> --%>	
		<!--页面头部固定部分-->
		<div class="index_head">
			<!--页面头部导航-->
			<div class="header_bar clearfix">
				<div class="left-slogan"></div>
				<div class="location">
					<span id="head_title1"></span><i></i>
				</div>
			</div>
			<ul class="filter_tabs clearfix">
				<li class="all_sort" onclick="clickSelect('all_sort', this);">店铺分类<i></i></li>
				<li class="sortby" onclick="clickSelect('sortby', this);">店铺排序<i></i></li>
				<li class="promotion" onclick="clickSelect('promotion', this);">优惠活动<i></i></li>
			</ul>
			<!--展开列表-->
			<div class="filter-container">
				<ul class="menu clearfix" id="all_sort">
					<li class="filter-item group"><a href="javascript:void(0)"
						class="selected" onclick="selectMer('group', 0, this);"><i></i><span>全部</span></a>
					</li>
					<c:forEach var="g" items="${groups}">
						<li class="filter-item group"><a href="javascript:void(0)"
						onclick="selectMer('group', ${g.id}, this);"><i></i><span>${g.name}</span></a>
						</li>
					</c:forEach>
				</ul>

				<ul class="menu clearfix" id="sortby">
					<li class="filter-item sort"><a href="javascript:void(0)"
						class="selected" onclick="selectMer('sort', 0, this);"><i></i><span>默认排序</span></a>
					</li>
					<li class="filter-item sort"><a href="javascript:void(0)"
						onclick="selectMer('sort', 1, this);"><i></i><span>距离优先</span></a>
					</li>
					<li class="filter-item sort"><a href="javascript:void(0)"
						onclick="selectMer('sort', 2, this);"><i></i><span>销量最高</span></a>
					</li>
					<li class="filter-item sort"><a href="javascript:void(0)"
						onclick="selectMer('sort', 3, this);"><i></i><span>评价最高</span></a>
					</li>
					<li class="filter-item sort"><a href="javascript:void(0)"
						onclick="selectMer('sort', 4, this);"><i></i><span>起送价最低</span></a>
					</li>
				</ul>
				<ul class="menu clearfix" id="promotion">
					<li class="filter-item promote"><a href="javascript:void(0)"
						class="selected" onclick="selectMer('promote', 0, this);"><span>不限</span></a>
					</li>
					<!-- <li class="filter-item promote">
                <a href="javascript:void(0)" onclick="selectMer('promote', 1, this);"><i></i><span>新用户立减</span></a>
            </li> -->
					<li class="filter-item promote"><a href="javascript:void(0)"
						onclick="selectMer('promote', 2, this);"><i></i><span>促销</span></a>
					</li>
					<!-- <li class="filter-item promote">
                <a href="javascript:void(0)" onclick="selectMer('promote', 3, this);"><i></i><span>支持代金券</span></a>
            </li> -->
					<li class="filter-item promote"><a href="javascript:void(0)"
						onclick="selectMer('promote', 4, this);"><i></i><span>支持积分</span></a>
					</li>
				</ul>
			</div>
		</div>
		<!--展开遮罩-->
		<div class="shadow_div"></div>
		<!--列表-->
		<div class="main_cont" >
			<ul class="order_list" id="orderList"></ul>
			<ul class="order_list" id="merchantList">
			</ul>
			<div id="loading_div" class="msg">点击加载更多</div>
		</div>
		<!--底部栏-->
		<%@ include file="./navIndex.jsp"%>

		<%-- <input type="hidden" id="lng" value="${location.lng }"> 
		<input type="hidden" id="lat" value="${location.lat }"> 
		<input type="hidden" id="city" value="${location.city }"> 
		<input type="hidden" id="address" value="${location.address }"> --%>
	</div>
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/swiper.min.js"></script>
<!-- <script type="text/javascript">
  $(function(){
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        autoplay : 3000,
        autoplayDisableOnInteraction : false
    });
  })
</script> -->
</body>
</html>
