<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<div class="bot_bar_out">
        <ul class="bot_bar clearfix">
            <li class="fore1">
                <a href="${basePath}/takeOutController.do?merchantList" class="active">
                   <i></i>
                   <span>首页</span> 
                </a>
            </li>
            <li class="fore2">
                <a href="${basePath}/takeOutController/orderlist.do">
                   <i></i>
                   <span>订单</span> 
                </a>
            </li> 
            <li class="fore1">
                <a href="${basePath}/takeOutController.do?mine">
                   <i></i>
                   <span>我的</span> 
                </a>
            </li>                   
        </ul>
    </div>