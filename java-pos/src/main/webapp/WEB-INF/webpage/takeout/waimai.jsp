<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="./common.jsp"  %>
	<title>${merchant.title }</title>
	<script type="text/javascript" src="${basePath}/plug-in/taskout/js/Util.js?id=1"></script>
	<script type="text/javascript" src="${basePath}/plug-in/taskout/js/waimai.js?id=8"></script>
	<script>
		var basePath="${basePath}";
	    var shopCartManage;//购物车管理
		$(function(){
			shopCartManage=new ShopCartManage();
			shopCartManage.init("${merchant.id}","${merchant.deliveryBegin}",basePath);//初始化
			shopCartManage.load();								//加载列表数据
		});
	    
    </script>
</head>

<body>
	<!-- 头部 -->
	<div class="head">
		<div class="head_back" >
			<a href="${basePath }/takeOutController.do?merchantList"  class="head_title" >
				<em class="backBtn_ico"></em>返回
			</a>
		</div>
  		<div class="head_l">
	  		<span class="head_title" >${merchant.title }</span>
  		</div>
		<div class="head_r">
			<a href="${basePath }/takeOutController.do?merchantDetail&merchantId=${merchant.id}" class="head_title" >
				<em class="merchant_icon"></em><!-- 商家详情 -->
			</a>
		</div>
	</div>
	
	<!-- 食物列表 --> 
    <div class="main menu_list" id="list" >
    	<div class="msg" id="msg">正在加载中...</div>
    </div>
    
    <!-- 购物车 -->
	<div class="bottom">
		<div class="back">
			<p class="back_s"></p>
		</div>
		<div class="bottom_bod">
			<p class="bottom_img">
				<span class="hint" id="suz">0</span>
			</p>
			<p class="bottom_time">
				￥<span class="bottom_time_sapn" id="sum">0.00</span>
			</p>
		</div>
		<a id="submitBtn" href="javascript:void(0);" class="bottom_je">去结算</a> <br />
	</div>
	
	<!-- 分类列表 -->
	<div class="m_type_div typeBtn" id="m_type_div" >
		<div class="m_type_l" id="m_type_l" style="display: none;">
			<ul class="m_type_ul" id="m_type_ul">
				<!-- <li><a href="javascript:void(0);"  class="hover" >9.1元特价套餐</a></li> -->
			</ul>
		</div>
		<div class="m_type_div_t">
			<a class="m_type_hs" id="showOrHideType"><em class="m_type_ico"></em>&nbsp;<span>分类</span></a>
		</div>
	</div>
</body>
</html>


