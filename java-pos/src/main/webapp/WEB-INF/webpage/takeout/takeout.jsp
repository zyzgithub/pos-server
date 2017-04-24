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
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/order.js?version=${webVersion}" ></script>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/address.js?version=${webVersion}" ></script>
<script type="text/javascript" src="${basePath}/plug-in/wxIndex/js/common.js?version=${webVersion}" ></script>
<script src="http://api.map.baidu.com/api?v=1.3&ak=hf7GGTXwqxndSLLYBfrKVwZh" type="text/javascript"></script>
</head>
<body style="color:#4d4d4d;">
<div class="wrapper">
    <div class="index_head">
        <div class="common_bar clearfix">
            <a href="javascript:void(0)" class="back"></a><p class="words">提交订单</p><a href="javascript:;"></a>
        </div>
    </div> 
    
    <div class="tj_names">
    	<c:if test="${addr == null }">
	        <a href="javascript:void(0)" class="fristAdd addAddr">+新增地址</a> 
    	</c:if>
    	<c:if test="${addr != null }">
	        <!-- 有地址或修改地址 -->
	        <a href="javascript:void(0)" class="editAddr">  
	            <p><span>${addr.name}</span><span>${addr.mobile}</span></p>
	            <p>
	            	地址:<c:if test="${not empty addr.buildingName}" >${addr.buildingName}&nbsp;</c:if>
	            		 <c:if test="${not empty addr.buildingFloor}" >${addr.buildingFloor}楼&nbsp;</c:if>
	            		${addr.addressDetail}
	            </p>
	            <i></i>
	        </a>
    	</c:if>
    </div>
    
    <!--       点击地址进入修改页面                     -->   
    <div class="enterAddress">
      <div class="index_head">
        <div class="common_bar">
            <a href="javascript:void(0)" class="cancel"></a><p>选择地址</p><a href="javascript:void(0)" class="addAddress_btn addAddr"></a>
        </div>    
      </div>
      <div class="mine_gldz"></div>        
    </div>
    
    <!-- 新建修改地址使用，默认隐藏 -->
    <div class="writeAddress">
        <div class="add_users">
          <div>
            <span>联系人&nbsp;&nbsp;</span>
            <div>
	            <input type="text" placeholder="您的姓名" class="lxrname" id="atten"/>
	            <p class="selectGender">
	                <input type="radio" checked="checked" name="sex" value="m" class="radioclass on">男<input type="radio" name="sex" value="f" class="radioclass">女
	            </p>
            </div>
          </div> 
          <div><span>联系电话</span><div><input type="text" placeholder="电话" id="tel"/></div></div> 
          <div>
            <span>配送范围</span>
            <div class="scAddress">
              <i></i>
              <input type="text" placeholder="请选择" readonly="readonly" class="words xsAddress" />
            </div>
          </div>
          <div style="border-bottom:0;"><span>详细地址</span><div><input type="text" placeholder="公司或门牌号" id="addr"/></div></div>  
        </div>
        <div class="selectAddress">
          <ul class="searchAddress"></ul>
          <div class="selectFloor">
          	<p style="padding-bottom: 5px;font-size: 16px;color:#333;border-bottom:1px solid #d9d9d9; margin-bottom:10px">请选择楼层</p>
            <ul class="clearfix" id="showFloor"></ul>
          </div>          
        </div>
       <div class="addBtn clearfix" id="operation">
           <a href="javascript:void(0)" class="addressSave">保存</a>
           <a href="javascript:void(0)" class="addressCancle">取消</a>
        </div>  
    </div>
    
    <div class="tj_pssj clearfix">
        <span>送达时间</span>
        <input type="text" readonly="readonly" class="selectTimes" placeholder="请选择配送时间" />
    </div> 
    <div class="timeListDiv">
        <div class="timeListShadow"></div>
        <ul class="sel_timeList"></ul>
               
    </div> 

    <div class="tj_fs">
        <ul>
            <li  class="cur"><i></i>微信支付</li>
            <c:if test="${isMoney }">
            	<li style="border-top:1px solid #d9d9d9; " id="2"><i></i>余额支付(￥${myMoney})</li>
            </c:if>
        </ul>
    </div>

    <c:if test="${isCredit || isCoupons }"> 
	    <div class="tj_djq">
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
         	<c:if test="${isDelevery }">
	         	<li class="clearfix  tj_psf" >
	        		<p>配送费</p>
	        		<div>
					<span></span>
					<span>￥${deliveryFee }</span>
	        		</div>
	        	</li>
        	</c:if>
            <li class="clearfix">
                <p>合计</p>
                <div>
                    <span>×${count}</span>
                    <span>￥${oughtPay}</span>
                </div>
            </li>
        </ul>
        <p>本单由<span>一号快递员</span>提供配送服务</p>
    </div> 
    <div class="comments">
        <input type="text" placeholder="给商家留言" id="remark"/>
    </div>
    <div class="syfp">
       <p>索要发票</p>
       <input type="text" placeholder="输入个人或公司抬头" id="invoice"/>
    </div> 
    <div class="zhanwei"></div>         
    <div class="tj_xd clearfix">
        <p id="oughtPay" totalPri="${oughtPay}">应付<span>￥${oughtPay}</span></p>
        <a href="javascript:void(0)" class="submitOrder">确认下单</a>
    </div>               
</div> 
<form action="${basePath}/takeOutController/addShopcart.do" method="post" id="backOrderType">
	<input type="hidden" name="json" class="json">
	<input type="hidden" name="merchantId" class="merchantId">
	<input type="hidden" name="userId" class="userId">
</form> 
<script type="text/javascript">
	window.history.forward(1);
	var orderManager; 
	var address;
	var lng = "${location.lng}";
	var lat = "${location.lat}";
	
    $(function(){
    	var dt = new Date();
	    var hourt = dt.getHours(); 
	    var minutet = dt.getMinutes();
    	if(hourt < 11 || (hourt == 11 && minutet <= 30)) {
    		$(".selectTimes").val("11:30-12:00");
    	} else {
    		if(minutet < 30) {
    			var t1 = (minutet - minutet % 10 + 30);
    			var t2 = minutet - minutet % 10;
    			if(t2 < 10)
    				t2 = "0"+t2;
    			$(".selectTimes").val(hourt+':'+t1+'-'+(++hourt)+':'+t2);
    		} else if(minutet < 40) {
    			$(".selectTimes").val((++hourt)+':00-'+hourt+':30');
    		} else {
    			var t1 = (minutet - minutet % 10 + 30)%60;
    			var t2 = minutet - minutet % 10;
    			if(t2 < 10)
    				t2 = "0"+t2;
    			$(".selectTimes").val((++hourt)+':'+t1+'-'+hourt+':'+t2);
    		}
    	}
        
           /* 选择配送时间出现选择时间 */
       $('.selectTimes').click(function() {
    	   orderManager.selectDeliveryTime();
       });
       /* 点击空白时间消失 */          
       $('.timeListShadow').click(function() {
    	  $('.timeListDiv').hide();
       });  
           /* 索要发票切换*/
       $('.syfp').children('p').click(function() {
           $(this).toggleClass('cur');
           $('.syfp').children('input').toggle();
           orderManager.isInvoice = !orderManager.isInvoice;
       });

       /* 修改或新增地址 */
       address = new AddressMng();
       address.init('${basePath}');
       //点击出现新增地址
       $(".addAddr").click(function() {
    	   $('.writeAddress').show();
    	   
    	   //兼容地址列表点击新增时，把地址列表隐藏
    	   $('.enterAddress').hide();
       });
       
       /* 点击加载可选地址列表 */
       
       $('.xsAddress').click(function() {
       		address.select();
       });
       /* 点击按钮切换性别 */
       $('.radioclass').click(function(event) {
            $(this).addClass('on');
            $(this).siblings().removeClass('on');
         });
       /* 保存地址 */
       $('.addressSave').click(function() {
    	   address.add();
       });
       
       /* 取消保存地址*/
       $(".addressCancle").click(function() {
    	   $('.writeAddress').hide();
    	   $('.xsAddress').val('');
    	   $("#atten").val('');
    	   $("#tel").val('');
    	   $("#addr").val('');
       });
       
       /*低级修改地址*/
       $(".editAddr").click(function() {
    	   address.load();
       });
       
       $(".cancel").click(function() {
    	   $('.enterAddress').hide();
       });
       $('.tj_djq>ul>li').click(function(event) {
 	      /* Act on the event */
 	 	  /* $(this).children('i').addClass('onSelect');*/
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
       
       /* 提交订单代码 */
       orderManager = new OrderManager();
   	   orderManager.init('${basePath}');
       $(".back").click(function() {
	       	$(".json").val('${shopcart.json }');
	       	$(".merchantId").val('${shopcart.merchantId }');
	       	$(".userId").val('${shopcart.userId }');
	       	$("#backOrderType").submit();
       });
       
       $(".submitOrder").click(function() {
    	   var remarkTime = '', remark = '';
    	   if($(".selectTimes").val())
    		   remarkTime = $(".selectTimes").val();
    	   if($("#remark").val())
    		   remark = $("#remark").val();
       		orderManager.createOrder( '${shopcart.json }', '${shopcart.merchantId }', '${shopcart.saleType}', remarkTime, remark);
       }); 
       
       $('.tj_fs>ul').children('li').click(function() {
           /* 微信支付方式  */
           $(this).addClass('cur');
           $(this).siblings().removeClass('cur');
           orderManager.payType = $(this).attr("id");
       }); 
       
       
       
    })
</script>      
</body>
</html>