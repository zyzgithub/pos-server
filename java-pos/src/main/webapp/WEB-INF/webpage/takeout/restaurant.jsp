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
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/common.js?version=${webVersion}" ></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/order.js?version=${webVersion}" ></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/address.js?version=${webVersion}" ></script>
<script src="http://api.map.baidu.com/api?v=1.3&ak=hf7GGTXwqxndSLLYBfrKVwZh" type="text/javascript"></script>
</head>
<body style="background:#eee;color:#4d4d4d;">
<div class="wrapper">
    <div class="index_head">
        <div class="common_bar clearfix">
            <a href="javascript:void(0)" class="back"></a><p class="words">提交订单</p><a href="javascript:;"></a>
        </div>
    </div> 
    <div class="ts_customer">
     <!--    第一次没有地址的时候     -->
     <c:if test="${addr == null }">
        <span class="ts_fristAdd">+新增联系人</span> 
     </c:if>
     <c:if test="${addr != null }">
        <div class="ts_userinfo">
            <span id="userName">${addr.name }</span><span id="userMobile">${addr.mobile }</span>
            <%-- <p id="addressDetail" style="margin:3px 0 0 5px;">${addr.addressDetail }</p> --%>
        </div>
     </c:if>
    </div>
<!--   联系人电话      --> 
    <div class="ts_userdetail">
        <div class="wirte_userinfo">
            <div>
                <span>联系人</span>
                <div><input type="text" placeholder="您的姓名" id="yourName"/></div>
            </div> 
            <div>
                <span>电话</span>
                <div><input type="text" placeholder="您的电话" id="yourMobile"/></div>
            </div>
	        <!-- <div>
	            <span>配送范围</span>
	            <div class="scAddress">
	              <i></i>
	              <input type="text" placeholder="请选择" readonly="readonly" class="words xsAddress" id="yourAddress" />
	            </div>
	        </div>
          	<div style="border-bottom:0;"><span>详细地址</span><div><input type="text" placeholder="公司或门牌号" id="yourAddressDetail"/></div></div>                -->
        </div> 
		<div class="selectAddress">
          <ul class="searchAddress"></ul>
          <div class="selectFloor">
            <p style="padding-bottom: 5px;font-size: 16px;color:#333;border-bottom:1px solid #d9d9d9; margin-bottom:10px">请选择楼层</p>
            <ul class="clearfix" id="showFloor"></ul>
          </div>          
        </div>
        <div class="userinfoBtn addBtn clearfix" >
            <a href="#" class="userinfoSave">保存</a>
            <a href="#" class="userinfoCancel">取消</a>
        </div>
    </div>     
    
    <div class="tj_fs  tj_fs_ts">
        <ul>
            <li class="cur" id="1"><i></i>微信支付</li>
            <c:if test="${isMoney }">
            	<li style="border-bottom:0; border-top:1px solid #d9d9d9" id="2"><i></i>余额支付(￥${myMoney})</li>
            </c:if>
        </ul>
    </div>
   
    <c:if test="${isCredit || isCoupons }"> 
    <div class="tj_djq clearfix">
        <ul>
       		<c:if test="${isCoupons}">
		       	<li id="coupons" money="${coupons.couponsMoney}" couponsId="${coupons.id}"><p>红包</p><i>满${coupons.couponsLimitMoney/100.0}减${coupons.couponsMoney/100.0 }</i></li>
	       	</c:if>
	       	<c:if test="${isCredit}">
		       	<li id="credit" money="${creditMoney*100}"><p>积分抵消</p><i>可抵消${creditMoney}元</i></li>
	       	</c:if>
       </ul>
    </div> 
    </c:if>
    <div class="tj_cd">
        <ul>
        	<c:forEach var="c" items="${carts}">
	            <li class=" clearfix">
	                <p>${c.menuName}</p>
	                <div>
	                    <span>×${c.count }</span>
	                    <span>￥${c.totalPrice }</span>
	                </div>
	            </li>
        	</c:forEach>
        </ul>
        <ul>
            <li class="clearfix">
                <p>合计</p>
                <div>
                    <span>×${count}</span>
                    <span>￥${totalPrice}</span>
                </div>
            </li>                
        </ul>
        <p>本单为<span>堂食订单</span></p>
    </div>  
    <div class="tj_xd clearfix">
        <p id="oughtPay" totalPri="${totalPrice}">应付<span>￥${totalPrice}</span></p>
        <a href="javascript:void(0)" class="submitOrder">确认下单</a>
    </div>               
</div>
<form action="${basePath}/takeOutController/addShopcart.do" method="post" id="backOrderType">
	<input type="hidden" name="json" class="json">
	<input type="hidden" name="merchantId" class="merchantId">
</form>
<script type="text/javascript">
	window.history.forward(1);
	var orderManager;
	var address;
	var lng = "${location.lng}";
	var lat = "${location.lat}";
	var userInfo = {};

    $(function(){
		
    	orderManager = new OrderManager();
    	orderManager.init('${basePath}');
    	
    	if($("#userName").text()) {
    		userInfo.name = $("#userName").text();
    		orderManager.userName = userInfo.name;
    	}
    	if($("#userMobile").text()) {
        	userInfo.mobile = $("#userMobile").text();
            orderManager.userMobile = userInfo.mobile;
    	}
    	
        $(".back").click(function() {
        	$(".json").val('${shopcart.json }');
        	$(".merchantId").val('${shopcart.merchantId }');
        	$("#backOrderType").submit();
        });
        
        $(".submitOrder").click(function() {
        	orderManager.createOrder( '${shopcart.json }', '${shopcart.merchantId }', '${shopcart.saleType}', '', '');
        });
        
	    $('.tj_djq>ul>li').click(function(event) {
		      /* Act on the event */
	    	$(this).children('i').toggleClass('onSelect');
 	 		$(this).siblings().children('i').removeClass('onSelect'); 
 	 		
 	 		var id = $(this).attr("id");
 	 		var money = $(this).attr("money");
		 	var pri =  parseFloat($("#oughtPay").attr("totalPri"))*100;
		 	
		 	$("#oughtPay").empty();
 	 		if(id == 'coupons') {
 	 			if(orderManager.isCoupons) {
	 	 			orderManager.isCoupons = false;
	 	 			orderManager.couponsId = 0;
	 	 			$("#oughtPay").append('应付<span>￥'+(pri/100.0).toFixed(2)+'</span>');
 	 			} else {
	 	 			orderManager.isCoupons = true;
	 	 			orderManager.couponsId = $(this).attr("couponsId");
	 	 			orderManager.isCredit = !orderManager.isCoupons;
	 	 			$("#oughtPay").append('应付<span>￥'+((pri-money)/100.0).toFixed(2)+'</span>');
 	 			}
 	 			
 	 		} else if(id == 'credit') {
 	 			if(orderManager.isCredit) {
 	 				orderManager.isCredit = false;
 	 				$("#oughtPay").append('应付<span>￥'+(pri/100.0).toFixed(2)+'</span>');
 	 			} else {
	 	 			orderManager.isCredit = true;
	 	 			orderManager.isCoupons = !orderManager.isCredit;
	 	 			$("#oughtPay").append('应付<span>￥'+((pri-money)/100.0).toFixed(2)+'</span>');
 	 				
 	 			}
 	 			
 	 		}
 	 		
		}); 
	    
	    $('.tj_fs>ul').children('li').click(function() {
            /* 微信支付方式  */
            $(this).addClass('cur');
            $(this).siblings().removeClass('cur');
            orderManager.payType = $(this).attr("id");
        });  
	    
        $('.ts_userinfo').click(function() {
            /* 点击修改联系人 */
            editUserInfo();
        }); 
        
        $('.ts_fristAdd').click(function() {
            /* 点击新增联系人 */
            addUserInfo();
        }); 
        
		/* ------选择地址出现 -------- */        
	   	/* 点击加载可选地址列表 */
        /* 修改或新增地址 */
        /* 
        address = new AddressMng();
        address.init('${basePath}');
        $('.xsAddress').click(function() {
       		address.select();
        });
         */
        $('.userinfoCancel').click(function() {
            /* 取消 */
            $('.ts_userdetail').hide();
        }); 
        
        $('.userinfoSave').click(function() {
            /* 保存  */
            var p = /^1\d{10}$/;
			var t = /^(0\d{2,3})?-?\d{7,8}$/;
            
            userInfo.name = $("#yourName").val();
            userInfo.mobile = $("#yourMobile").val();
            
    		if(userInfo.name == null || $.trim(userInfo.name) == '') {
    			$.WapDialog.tip("联系人不能为空");
    			return;
    		}
    		if(userInfo.mobile == null || userInfo.mobile == '') {
    			$.WapDialog.tip("联系电话不能为空");
    			return;
    		} else if(!p.test(userInfo.mobile) && !t.test(userInfo.mobile)) {
    			$.WapDialog.tip("联系电话有误");
    			return;
    		}
    		/* if($("#yourAddress").val() == null || $.trim($("#yourAddress").val()) == '') {
    			$.WapDialog.tip("配送范围不能为空");
    			return;
    		}
    		if($("#yourAddressDetail").val() == null || $.trim($("#yourAddressDetail").val()) == '') {
    			$.WapDialog.tip("详细地址不能为空");
    			return;
    		} 
    		
            var addr = $("#yourAddress").val();
            if(addr != '其他地址')
            	userInfo.address = addr+" ";
            else
            	userInfo.address = "";
            	
            
            userInfo.address += $("#yourAddressDetail").val();
            */
            $(".ts_customer").empty();
            var html  = '<div class="ts_userinfo">';
                html += '<span id="userName">'+userInfo.name+'</span><span id="userMobile">'+userInfo.mobile+'</span>';
                //html += '<p id="addressDetail" style="margin:3px 0 0 5px;">'+userInfo.address+'</p>';
                html += '</div>';
            
            $(".ts_customer").append(html);
            
            $('.ts_userdetail').hide();
            $('.ts_userinfo').click(function() {
                /* 点击修改联系人 */
                editUserInfo();
            }); 
            orderManager.userName = userInfo.name;
            orderManager.userMobile = userInfo.mobile;
            //orderManager.userAddress = userInfo.address;
        }); 	
        
    });
    
    function editUserInfo() {
    	userInfo.name = $("#userName").text();
        userInfo.mobile = $("#userMobile").text();
        //userInfo.address = $("#addressDetail").text();
        
        /* 
        var addr = '';
        var detail = '';
        if(userInfo.address.indexOf(" ") > 0) {
        	var split = userInfo.address.split(" ");
        	addr = split[0];
        	detail = split[1];
        } else {
        	addr = '其他地址';
        	detail = userInfo.address;
        } 
        */
        
        $("#yourName").val(userInfo.name);
        $("#yourMobile").val(userInfo.mobile);
        //$("#yourAddress").val(addr);
        //$("#yourAddressDetail").val(detail);
        $('.ts_userdetail').show();
    }

    function addUserInfo() {
    	$("#yourName").val('');
    	$("#yourMobile").val('');
    	//$("#yourAddress").val('');
    	//$("#yourAddressDetail").val('');
        $('.ts_userdetail').show(); 
    }
</script>        
</body>
</html>