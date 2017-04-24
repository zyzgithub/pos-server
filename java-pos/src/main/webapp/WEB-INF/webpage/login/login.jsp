<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>1号生活-接口管理平台</title>
  <link rel="shortcut icon" type="image/x-icon" href="${basePath}/plug-in/taskout/images/logo.ico"/>
<!--[if lt IE 9]>
   <script src="${basePath }/plug-in/login/js/html5.js"></script>
<![endif]-->
<!--[if lt IE 7]>	
  <script src="${basePath }/plug-in/login/js/iepng.js" type="text/javascript"></script>
  <script type="text/javascript">
	EvPNG.fix('div, ul, img, li, input'); //EvPNG.fix('包含透明PNG图片的标签'); 多个标签之间用英文逗号隔开。
	</script>
<![endif]-->
	<link href="${basePath }/plug-in/login/css/zice.style.css" rel="stylesheet" type="text/css" />
  	<link href="${basePath }/plug-in/login/css/buttons.css" rel="stylesheet" type="text/css" />
  	<link href="${basePath }/plug-in/login/css/icon.css" rel="stylesheet" type="text/css" />
  	<link href="${basePath }/plug-in/login/css/tipsy.css" rel="stylesheet" type="text/css" media="all" />
  	<style type="text/css">
		html {
			background-image: none;
		}
		
		label.iPhoneCheckLabelOn span {
			padding-left: 0px
		}
		
		#versionBar {
			background-color: #212121;
			position: fixed;
			width: 100%;
			height: 35px;
			bottom: 0;
			left: 0;
			text-align: center;
			line-height: 35px;
			z-index: 11;
			-webkit-box-shadow: black 0px 10px 10px -10px inset;
			-moz-box-shadow: black 0px 10px 10px -10px inset;
			box-shadow: black 0px 10px 10px -10px inset;
		}
		
		.copyright {
			text-align: center;
			font-size: 10px;
			color: #CCC;
		}
		
		.copyright a {
			color: #A31F1A;
			text-decoration: none
		}
		
		
		/*update-begin--Author:tanghong  Date:20130419 for：【是否】按钮错位*/
		.on_off_checkbox{
			width:0px;
		}
		/*update-end--Author:tanghong  Date:20130419 for：【是否】按钮错位*/
		
		#login .logo {
			width: 500px;
			height: 51px;
		}
	</style>
 </head>
 <body>
  <div id="alertMessage"></div>
  <div id="successLogin"></div>
  <div class="text_success">
   <img src="${basePath }/plug-in/login/images/loader_green.gif" alt="Please wait" />
   <span>登陆成功!请稍后....</span>
  </div>
  <div id="login">
   <div class="ribbon" style="background-image:url(${basePath }/plug-in/login/images/typelogin.png);"></div>
   <div class="inner">
    <div class="logo"><%--
     <img src="${basePath }/plug-in/login/images/toplogo-jeecg.png"/>
     	--%><div  style="margin-left:20px;padding-top:10px;padding-bottom:5px;font-size:40px;color:#0099cc;font-family: STXingkai">1号生活-接口管理平台</div>
    </div>
    <div class="formLogin">
     <form name="formLogin" id="formLogin" action="${basePath }/loginController.do?login" check="loginController.do?checkuser" method="post">
      <input name="userKey" type="hidden" id="userKey" value="D1B5CC2FE46C4CC983C073BCA897935608D926CD32992B5900"/>
      <div class="tip">
       <input class="userName" name="userName" type="text" id="userName" title="用户名" iscookie="true"   nullmsg="请输入用户名!"/>
      </div>
      <div class="tip">
       <input class="password" name="password" type="password" id="password" title="密码"  nullmsg="请输入密码!"/>
      </div>
      <div class="loginButton">
       <div style="float: left; margin-left: -9px;">
        <input type="checkbox" id="on_off" name="remember" checked="ture" class="on_off_checkbox" value="0" />
        <span class="f_help">是否记住用户名 ?</span>
       </div>
       
       <div style="float: right; padding: 3px 0; margin-right: -12px;">
        <div>
         <ul class="uibutton-group">
          <li>
           <a class="uibutton normal" href="#" id="but_login">登录</a>
          </li>
          <li>
           <a class="uibutton normal" href="#" id="forgetpass">重置</a>
          </li>
         </ul>
        </div>
        <div style="float: left; margin-left: 30px;">
       </div>
       <div class="clear"></div>
      </div>
     </form>
    </div>
   </div>
   <div class="shadow"></div>
  </div>
  <!--Login div-->
  <div class="clear"></div>
  <div id="versionBar">
   <div class="copyright">
    &copy; 版权所有
    <span class="tip"><a href="javascript:void(0);" title="广东点吧科技股份有限公司">点吧科技</a> 技术支持:<a href="#" title="广东点吧科技股份有限公司">点吧科技-研发中心</a></span>
   </div>
  </div>
    <!-- Link JScript-->
  <script type="text/javascript" src="${basePath }/plug-in/jquery/jquery-1.8.3.min.js"></script>
  <script type="text/javascript" src="${basePath }/plug-in/jquery/jquery.cookie.js"></script>
  <script type="text/javascript" src="${basePath }/plug-in/login/js/jquery-jrumble.js"></script>
  <script type="text/javascript" src="${basePath }/plug-in/login/js/jquery.tipsy.js"></script>
  <script type="text/javascript" src="${basePath }/plug-in/login/js/iphone.check.js"></script>
  <script type="text/javascript" src="${basePath }/plug-in/login/js/login.js"></script>
 </body>
</html>