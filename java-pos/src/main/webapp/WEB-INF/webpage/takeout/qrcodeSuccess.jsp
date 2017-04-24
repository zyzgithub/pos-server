<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
   <%@ include file="./common.jsp" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>1号外卖</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
<meta content="private,must-revalidate" http-equiv="Cache-Control">
<meta content="telephone=no, address=no" name="format-detection">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/taskout/codepay.css" />

</head>
	<body>
		<div id="codepay-success" class="co-icon-box co-main-box center-block text-center">
			<img src="${basePath}/plug-in/taskout/images/icon_yes.png" class="pay-icon center-block"/>
			<h1 class="co-main-text">支付费用${qcCodeMoney}元</h1>
			<p class="co-sub-text">通过微信支付</p>
			<div class="center-block">
				<div class="co-middle-line center-block"></div>
				<div class="center-block">
					<div class="center-block">
						<img id="co-qrcode" src="${basePath}/plug-in/taskout/images/wechat.png"/>
					</div>
					<p class="co-sub-text">长按关注1号外卖公众号</p>
					<p class="co-sub-text">快速点餐不是问题！</p>
				</div>
			</div>
		</div>
	</body>
</html>