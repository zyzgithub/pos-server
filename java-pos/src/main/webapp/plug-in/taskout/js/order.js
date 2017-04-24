function OrderManager() {
	//订单是否已经生成
	this.iscreate=false;
	//订单是否正在提交
	this.submit=false;
	this.isInvoice=false;
	this.isCredit=false;
	this.isCoupons=false;
	this.couponsId=0;
	this.basePath = '';
	this.payType=1;
	this.saleType;
	this.json;
	this.merchantId;
	this.invoice;
	this.userName='';
	this.userMobile='';
	this.userAddress='';
	
	this.init = function(basePath) {
		orderManager.basePath = basePath;
	};
	
	this.createOrder = function(json, merchantId, saleType, remarkTime, remark) {
		if(orderManager.submit)return;
		
		if(orderManager.iscreate) {
			$.WapDialog.tip("你的订单已经生成");
			window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/orderlist.do");
		}
		
		if($(".editAddr").length == 0 && saleType == 1) {
			$.WapDialog.tip("请填写收货地址");
			return;
		}
		
		if($(".ts_userinfo").length == 0 && saleType == 2) {
			$.WapDialog.tip("请填写联系人");
			return;
		}
		
		if(orderManager.isInvoice) {
			orderManager.invoice = $("#invoice").val();
			if(orderManager.invoice == null || orderManager.invoice == '') {
				$.WapDialog.tip("请填写发票抬头");
				return;
			}
		}
		
		orderManager.submit=true;
        $.WapDialog.lock({content:'正在提交订单...'});
		orderManager.json = json;
		orderManager.merchantId = merchantId;
		orderManager.saleType = saleType;
		
		var url = orderManager.basePath + '/takeOutController/createOrder.do';
		$.post(url, {
			"json":orderManager.json,
			"merchantId":orderManager.merchantId,
			"saleType":orderManager.saleType,
			"payType":orderManager.payType,
			"remark":remark,
			"timeRemark":remarkTime,
			"invoice":orderManager.invoice,
			"credit":orderManager.isCredit,
			"coupons":orderManager.isCoupons,
			"couponsId":orderManager.couponsId,
			'userName':orderManager.userName,
			'userMobile':orderManager.userMobile,
			'userAddress':orderManager.userAddress
		}, function(data){
			orderManager.wxpay(data);
		}, "json");
	};
	
	this.toPay = function(orderId) {
		if(orderManager.submit)return;
		orderManager.submit=true;
		$.WapDialog.lock({content:'正在进入支付...'});
		var url = orderManager.basePath + '/takeOutController/wxpay/'+orderId+'.do';
		$.post(url, {"orderId":orderId}, function(data){
			orderManager.wxpay(data);
		}, "json");
	};
	
	this.confirmOrder = function(orderId, merid, courId) {
		$.WapDialog.open({
			title:'温馨提示',
			content:'确认收货？',
			ok:{
				name:'确定',
				callBack:function(){ 
					if(orderManager.submit)return;
					orderManager.submit=true;
					$.WapDialog.lock({content:'正在确认收货...'});
					var url = orderManager.basePath + '/takeOutController/'+orderId+'/confirmorder.do';
					$.post(url, {"courierId":courId, "merchantId":merid}, function(data) {
						$.WapDialog.close();//解锁
						if(data.state == "success") {
							$.WapDialog.tip("确认收货成功");  
							window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/orderlist.do");
						} else {
							$.WapDialog.tip("确认收货失败,请稍候重试");  
						}
						orderManager.submit=false;
					}, "json").error(function() {
						$.WapDialog.close();//解锁
						$.WapDialog.tip("确认收货失败,请稍候重试");
					});
				}
			},
			cancel:{
				show:true
			}
		});
	};
	
	this.wxpay = function(data) {
		if(data.state == "success" && data.payType == "wx") {
			orderManager.iscreate=true;
			/*
			 if(parseInt(data.agent) < 5){  
               	 $.WapDialog.close();//解锁
               	 $.WapDialog.tip("您的微信版本低于5.0无法使用微信支付");  
               	 orderManager.submit=false;
               	 return;  
            } 
            */
			WeixinJSBridge.invoke('getBrandWCPayRequest',{  
                "appId" : data.appId,                  //公众号名称，由商户传入  
                "timeStamp":data.timeStamp,          //时间戳，自 1970 年以来的秒数  
                "nonceStr" : data.nonceStr,         //随机串  
                "package" : data.packageValue,      //<span style="font-family:微软雅黑;">商品包信息</span>  
                "signType" : data.signType,        //微信签名方式:  
                "paySign" : data.paySign           //微信签名  
                },function(res){
               	 	$.WapDialog.close();//解锁
                    if(res.err_msg == "get_brand_wcpay_request:ok" ){  
                   	 	$.WapDialog.lock({content:'支付成功，正在跳转...'});
                   	 	if(data.saleType == 1) {
                   	 		window.location.href=  encodeURI(orderManager.basePath+"/takeOutController.do?merchantList");
                   	 	} else {
                   	 		window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/"+data.orderId+"orderdetail.do");
                   	 	}
                   	 	//window.location.href=  encodeURI(orderManager.basePath+"/coupons/share.do?score="+data.preScore);
                   	 	
                    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                    	$.WapDialog.lock({content:"支付已取消"});
                   	 	window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/orderlist.do");
                    }else{
                    	$.WapDialog.lock({content:"支付失败，请重试"});  
                   	 	window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/orderlist.do");
                    }
                    orderManager.submit=false;
                });
			
		} else if(data.state == "success" && data.payType == "balance") {
			$.WapDialog.close();//解锁
			$.WapDialog.lock({content:'支付成功，正在跳转...'});
			if(data.saleType == 1) {
       	 		window.location.href=  encodeURI(orderManager.basePath+"/takeOutController.do?merchantList");
       	 	} else {
       	 		window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/"+data.orderId+"orderdetail.do");
       	 	}
			//window.location.href=  encodeURI(orderManager.basePath+"/coupons/share.do?score="+data.preScore);
			
		} else if(data.state == "fail") {
			$.WapDialog.close();
    		$.WapDialog.lock({content:data.reason});
    		window.location.href=  encodeURI(orderManager.basePath+"/takeOutController/orderlist.do");
		}
	};
	
	this.askRefund = function(orderId) {
		$.WapDialog.open({
			title:'温馨提示',
			content:'确认退单？',
			ok:{
				name:'确定',
				callBack:function(){ 
					var url = orderManager.basePath+"/takeOutController.do?askRefund";
					$.post(url, {"orderId":orderId, "refundReason":"无"}, 
							function(data) {
								if(data.code == 0) {
									$.WapDialog.tip("您的退款申请已提交"); 
									$('.tuid').addClass('cur_btn');
									$('.tuid').text('退款申请中');
								} else {
									$.WapDialog.tip(data.msg); 
								}
							}, "json");
				}
			},
			cancel:{
				show:true
			}
		});
	};
	
	this.selectDeliveryTime = function() {
		 $(".sel_timeList").empty();
  	   
	       var d = new Date();
	       var hour = d.getHours(); 
	       var minute = d.getMinutes();
	       minute = minute - minute % 5 + 30;
  	   
	       var cal = (24 - hour) > 2 ? (24 - hour) : 2;
	       cal -=1;
	  	   var html = '<li class="selected">请选择配送时间</li>';
	  	   for(var i=0; i<(cal*8); i++) {
	  		   var t_h = (hour + parseInt(minute/60))%24;
	  		   var t_m = minute%60;
	  		   
	  		   var m = "";
	  		   var h = "";
	  		   if(t_m < 10)
	  			   m = "0"+t_m;
	  		   else
	  			   m = ""+t_m;
	  		   
	  		   if(t_h < 10) 
	  			   h = "0"+t_h;
	  		   else
	  			   h = ""+t_h;
	  		   if(i%2 == 0) {
	  		   	   html += '<li class="selTime">'+h+':'+m+'-';
		    		   minute += 15;
	  		   } else {
	  			   html += h+':'+m+'</li>';
	  		   }
	  			   
	  	   }
	  	   
	  	   $(".sel_timeList").append(html);
	       $('.timeListDiv').show();
	         
	       $(".selTime").click(function() {
	      	   var t = $(this).text();
	      	   $(".selectTimes").val(t);
	      	   $('.timeListDiv').hide();
	       });
	};
}