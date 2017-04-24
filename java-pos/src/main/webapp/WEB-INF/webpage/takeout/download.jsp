<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<% 
String basePath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
request.setAttribute("basePath", basePath);
%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="format-detection" content="telephone=no" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="shortcut icon" type="image/x-icon" href="${basePath}/plug-in/taskout/logo.ico"/>
	<script type="text/javascript" src="${basePath}/plug-in/jquery/jquery-1.8.3.min.js"></script>
	<link type="text/css" rel="stylesheet" href="${basePath }/plug-in/wapDialog/css/wap.dialog.css" />
	<script type="text/javascript" src="${basePath }/plug-in/wapDialog/jquery.wap.dialog.min.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/taskout/js/Util.js"></script>
	<title>1号外卖-app下载</title>
	<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/taskout/download.css" />
	<script type="text/javascript">
	$(function(){
		$('.b_button >a').click(function(){
			if($(this).attr('href')=='javascript:void(0);'){
				$.WapDialog.tip('亲！该版本尚未上线啦!');
			}else if(browser.versions.weixin){
				$('#po_text').show();
				$("html,body").animate({scrollTop:$("#po_text").offset().top},600);
				return false;
			}else if($(this).attr('href').indexOf('.apk')>-1 && !browser.versions.android){
				var tempHref=$(this).attr('href');
				$.WapDialog.open({content:'亲！您手机不是安卓系统，无法安装安卓版本，请确定是否要下载?',
					ok:{
						name:'继续下载',
						callBack:function(){
							window.location.href=tempHref;
						}
					},cancel:{show:true}
				});
				return false;
			}else if($(this).attr('href').indexOf('.ipa')>-1 && !browser.versions.ios){
				var tempHref=$(this).attr('href');
				$.WapDialog.open({content:'亲！您手机不是IOS系统，无法安装苹果版本，请确定是否要下载?',
					ok:{
						name:'继续下载',
						callBack:function(){
							window.location.href=tempHref;
						}
					},cancel:{show:true}
				});
				return false;
			}
		});
		
		$('#po_text').click(function(){
			$(this).hide();
		});
	});
	</script>
</head>

<body>
<div class="po_text" id="po_text" style="display:none;height:260%;" >
	<div class="po_jt"><img src="${basePath }/plug-in/taskout/images/tip_02.png" width="100%"></div>
	<div class="po_jt"> <img src="${basePath }/plug-in/taskout/images/tip_03.png" width="100%"></div>
	<div class="po_llq">
		<span>安装小窍门：</span><br>
		亲！微信内安装不了<br>
		请点击右上角“在浏览器中打开”<br>
		打开后再选择安装就没问题了！
	</div>
	<div class="clear"></div>
</div>

<div class="main" style="top:0px;">
	<div class="head_log">
		<img src="${basePath}/plug-in/taskout/images/head_ad.jpg"  />
	</div>
	
	<div class="div_verion">
		<div class="b_title b_title2">
			<span>个人版</span>
		</div>
		<div class="b_button">
			<a href="${basePath }/upload/apk/Waimai_personal.apk"><em class="a_logo"></em>安卓用户下载</a>
		</div>
		<div class="b_button b_button2">
			<a href="javascript:void(0);"><em class="p_logo"></em>苹果用户下载</a>
		</div>
		<div class="line"></div>
	</div>
	
	<div class="div_verion">
		<div class="b_title b_title2">
			<span>商家版</span>
		</div>
		<div class="b_button">
			<a href="${basePath }/upload/apk/Waimai_business.apk"><em class="a_logo"></em>安卓用户下载</a>
		</div>
		<div class="b_button b_button2">
			<a href="${basePath }/upload/ipa/Waimai_business.ipa"><em class="p_logo"></em>苹果用户下载</a>
		</div>
		<div class="line"></div>
	</div>
	
	<div class="div_verion">
		<div class="b_title b_title2">
			<span>快递员版</span>
		</div>
		<div class="b_button">
			<a href="${basePath }/upload/apk/Waimai_send.apk"><em class="a_logo"></em>安卓用户下载</a>
		</div>
		<div class="b_button b_button2">
			<a href="${basePath }/upload/ipa/Waimai_send.ipa"><em class="p_logo"></em>苹果用户下载</a>
		</div>
		<div class="line"></div>
	</div>
	
	<div class="div_verion">
		<div class="b_title b_title2">
			<span>其他终端APP</span>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_kitchen_pad.apk"><em class="a_logo"></em>厨房PAD</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_kitchen_tv.apk"><em class="a_logo"></em>厨房电视</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_tv_menu.apk"><em class="a_logo"></em>电视菜单</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_peican_pad.apk"><em class="a_logo"></em>配餐PAD</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_qiantai.apk"><em class="a_logo"></em>前台PAD</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_pad.apk"><em class="a_logo"></em>外卖PAD</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_tv.apk"><em class="a_logo"></em>外卖电视机</a>
		</div>
		<div class="b_button b_button3">
			<a href="${basePath }/upload/apk/wm_paihao_tv.apk"><em class="a_logo"></em>排号电视</a>
		</div>
	</div>
</div>
</body>
</html>
