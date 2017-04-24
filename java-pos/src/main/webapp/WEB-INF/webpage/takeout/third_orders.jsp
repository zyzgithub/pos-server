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
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/third/css/common.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/plug-in/third/css/order.css" />
<script type="text/javascript" src="${basePath}/plug-in/third/js/jquery-1.11.3.min.js"></script>
</head>
<body style="background:#ececec">
<div class="wrapper">   
    <div class="index_head" >
        <div class="common_bar " style="background:#f7f7f7">
           <p>我的订单</p>
        </div>
    </div>
    <div class="page">
      <div class="tksq">
            <p class="tksq_sj">头等舱互联网餐饮</p>
            <div class="tksq_info clearfix">
                <div class="tksq_pic"><img src="images/20150804134022.png" alt="" /></div>
                <div class="tksq_content ">
                    <p><span>2015-09-09<span></span>18:30</span></p>
                    <p>总价：<span>￥58</span></p>
                </div>
                <div class="tksq_status">
                    <p style="">退款申请中</p>
                </div>
            </div>
        </div>
        <div class="tksq">
            <a href="##"><p class="tksq_sj">i玩派<span>（第三方订单）</span></p></a>
            <div class="tksq_info clearfix">
            <a href="##"><div class="tksq_pic"><img src="images/20150804134022.png" alt="" /></div>
                <div class="tksq_content ">
                    <p><span>2015-09-09<span></span>18:30</span></p>
                    <p>总价：<span>￥58</span></p>
                </div>
            </a>
                <div class="tksq_status">
                    <p style="color:#808080">已付款</p>
                    <a href="#" class="qrsh third-statu">查看订单</a>
                </div>
            </div>
        </div>
        <div class="tksq">
            <a href="##"><p class="tksq_sj">头等舱互联网餐饮<span>（第三方订单）</span></p></a>
            <div class="tksq_info clearfix">
            <a href="##"><div class="tksq_pic"><img src="images/20150804134022.png" alt="" /></div>
                <div class="tksq_content ">
                    <p><span>2015-09-09<span></span>18:30</span></p>
                    <p>总价：<span>￥58</span></p>
                </div>
            </a>
                <div class="tksq_status">
                    <p style="color:#808080">待付款</p>
                    <a href="##" class="qrsh third-statu">去付款</a>
                </div>
            </div>
        </div>          
        <div class="tksq">
            <a href="##"><p class="tksq_sj">头等舱互联网餐饮</p></a>
            <div class="tksq_info clearfix">
            <a href="##"><div class="tksq_pic"><img src="images/20150804134022.png" alt="" /></div>
                <div class="tksq_content ">
                    <p><span>2015-09-09<span></span>18:30</span></p>
                    <p>总价：<span>￥58</span><span class="paihao">排号：0987</span></p>
                </div>
            </a>
                <div class="tksq_status">
                    <p style="color:#808080">待收货</p>
                    <a href="#" class="qrsh">确认收货</a>
                </div>
            </div>
        </div> 
        <div class="tksq">
            <a href="##"><p class="tksq_sj">头等舱互联网餐饮</p></a>
            <div class="tksq_info clearfix">
                <a href="##"><div class="tksq_pic"><img src="images/20150804134022.png" alt="" /></div>
                <div class="tksq_content ">
                    <p><span>2015-09-09<span></span>18:30</span></p>
                    <p>总价：<span>￥58</span></p>
                </div>
                </a>
                <div class="tksq_status">
                    <p style="color:#808080">待付款</p>
                    <a href="#" class="qrsh">去付款</a>
                </div>
            </div>
        </div>
        <div class="tksq">
            <a href="##"><p class="tksq_sj">头等舱互联网餐饮</p></a>
            <div class="tksq_info clearfix">
                <a href="##"><div class="tksq_pic"><img src="images/20150804134022.png" alt="" /></div>
                <div class="tksq_content ">
                    <p><span>2015-09-09<span></span>18:30</span></p>
                    <p>总价：<span>￥58</span></p>
                </div>
                </a>
                <div class="tksq_status">
                    <p style="color:#808080">待评价</p>
                    <a href="#" class="qrsh">去评价</a>
                </div>
            </div>
        </div>                          
    </div>

    <div class="bot_bar_out">
        <ul class="bot_bar clearfix">
            <li class="fore1">
                <a href="index.html">
                   <i></i>
                   <span>首页</span> 
                </a>
            </li>
            <li class="fore2">
                <a href="evalute.html"  class="active">
                   <i></i>
                   <span>订单</span> 
                </a>
            </li> 
            <li class="fore1">
                <a href="mine.html">
                   <i></i>
                   <span>我的</span> 
                </a>
            </li>                   
        </ul>
    </div>    
</div>  
</body>
</html>
