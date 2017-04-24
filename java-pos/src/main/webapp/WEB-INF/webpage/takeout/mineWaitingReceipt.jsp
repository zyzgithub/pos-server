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
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/order.css?version=${webVersion}" />	
</head>

<body style="background:#eee">
<div class="wrapper">   
    <div class="index_head" >
        <div class="common_bar " style="background:#f7f7f7">
           <a href="#" class="back"></a><p>待收货</p>
        </div>
    </div>
    <div class="page">
                                          
    </div>
    <div id="loading_div" class="msg">点击加载更多</div>
</div>  


	<script type="text/javascript" src="${basePath }/plug-in/taskout/js/orderlist.js?id=6"></script>
	<script type="text/javascript">
		var basePath="${basePath}";
		var omvList= ${omvList};
	 
		var orderListMng;
		$(function(){
			$(".back").click(function(){
				window.location.href = basePath+"/takeOutController.do?mine";
			});
			$("#loading_div").click(function(){
				var url = basePath+"/takeOutController.do?queryMineOrderListByState&";
				orderListMng.loadMore(url, orderListMng.daiShouhuoState);
			});
			orderListMng=new OrderListMng();
			orderListMng.basePath = basePath;
			//orderListMng.init(basePath);
			if(omvList){
				orderListMng.orderListByState(omvList, orderListMng.daiShouhuoState);
			}
		});
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
		
		
	</script>
	
</body>

</html>