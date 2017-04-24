<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="./common.jsp"  %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>1号外卖</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
<meta content="private,must-revalidate" http-equiv="Cache-Control">
<meta content="telephone=no, address=no" name="format-detection">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
</head>
<body>
<div class="wrapper">
    <div class="evaluateBox" style="display:block;">
        <div class="rebackIndex">
            <a href="${basePath}/takeOutController.do?merchantList"></a>
        </div> 
        <div class="bonusPoint">
          <div class="hexagon" id="first">
            <div class="hiveTop"></div>
            <div class="hiveCenter"></div>
            <div class="hiveBottom"></div>
          </div>
          <div class="hexagon" id="second">
            <div class="hiveTop"></div>
            <div class="hiveCenter"></div>
            <div class="hiveBottom"></div>
          </div>
          <div class="hexagon" id="third">
            <div class="hiveTop"></div>
            <div class="hiveCenter"></div>
            <div class="hiveBottom"></div>
          </div> 
          <div class="hexagonOut">                  
            <h3>恭喜你</h3>
            <div>
               <p>${score}</p><span>积分</span>
            </div>
            <p>已领取</p>
          </div>
        </div>
        <div class="bonusPointBtn">
            <p>下单成功</p>
            <a href="javascript:void(0)" class="toConsume">Share</a>
        </div>
        <!--    点击出现        -->
        <div class="shareDiv">
           <div class="shadowDiv"></div>
           <div class="sharePic"><img src="${basePath}/plug-in/wxIndex/images/shareBg.png" alt="" /></div>
        </div>

    </div>
</div>
<script type="text/javascript">
	wx.config({
	    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: "${jsTicket.appId}", // 必填，公众号的唯一标识
	    timestamp: "${jsTicket.timestamp}", // 必填，生成签名的时间戳
	    nonceStr: "${jsTicket.nonceStr}", // 必填，生成签名的随机串
	    signature: "${jsTicket.signature}",// 必填，签名，见附录1
	    jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage", "hideMenuItems"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	
	wx.ready(function(){
		wx.hideMenuItems({
		    menuList: ["menuItem:copyUrl", "menuItem:share:qq", 
					   "menuItem:share:weiboApp", "menuItem:share:QZone",
					   "menuItem:openWithSafari", "menuItem:openWithQQBrowser",
					   "menuItem:share:email", "menuItem:favorite"] // 要显示的菜单项，所有menu项见附录3
		});
		
		 wx.onMenuShareAppMessage({
	    		title: '${share.title}', // 分享标题
	    		desc: '${share.desc}', // 分享描述
	    		link: '${share.link}', // 分享链接
	    		imgUrl: '${share.imgUrl}', // 分享图标
	    		type: 'link', // 分享类型,music、video或link，不填默认为link
	    		dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	    		success: function () { 
	    		        // 用户确认分享后执行的回调函数
	    		    $.post("${basePath}/coupons/share.do", {"serial":"${share.serial}"}, function() {
		    		    window.location.href=  encodeURI("${basePath}/takeOutController.do?merchantList");
	    		    });
	    		 }
	    	});
	      
	       wx.onMenuShareTimeline({
	    		title: '${share.title}', // 分享标题
	    		link: '${share.link}', // 分享链接
	    		imgUrl: '${share.imgUrl}', // 分享图标
	    		success: function () { 
	    		   // 用户确认分享后执行的回调函数
	    		   $.post("${basePath}/coupons/share.do", {"serial":"${share.serial}"}, function() {
		    		   window.location.href=  encodeURI("${basePath}/takeOutController.do?merchantList");
	    		   });
	    		 }
	    	});
	});

 /*  $(function(){
      $('.toConsume').click(function() {
    
        $('.shareDiv').fadeIn();
      });
      
      $('.shadowDiv').click(function() {
      
        $('.shareDiv').fadeOut();
      });
      $('.sharePic').click(function() {

          $('.shareDiv').fadeOut();
        });      
  }) 
 */
</script>
</body>
</html>