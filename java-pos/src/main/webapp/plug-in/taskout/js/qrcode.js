function qcCodeManager(){
	this.submit=false;
	this.merchantId;
	this.money;
	this.saleType;
	this.payType;
	
	this.init = function(basePath) {
		qcCodeManager.basePath = basePath;
	};
	
	this.createOrder = function (money,merchantId,saleType,payType){
		if(qcCodeManager.submit){
			return;
		} 
		$.WapDialog.lock({content:'正在进入微信支付...'});//锁屏
		qcCodeManager.submit=true;
		qcCodeManager.merchantId=merchantId;
		qcCodeManager.money=money;
		qcCodeManager.saleType=saleType;
		qcCodeManager.payType=payType;
		var url = qcCodeManager.basePath + '/weixin/store/createOrder.do';
		if(saleType==2){
			$.post(url, {
				"merchantId":qcCodeManager.merchantId,
				"money":qcCodeManager.money,
				"saleType":qcCodeManager.saleType,
				"payType":qcCodeManager.payType
			}, function(data){
				qcCodeManager.wxpay(data);
			}, "json");
		}
	};
	
	this.wxpay = function(data) {
		if(data.state == "success" && data.payType == "wx") {
			 $.WapDialog.close();//解锁
			WeixinJSBridge.invoke('getBrandWCPayRequest',{  
                "appId" : data.appId,                  //公众号名称，由商户传入  
                "timeStamp":data.timeStamp,          //时间戳，自 1970 年以来的秒数  
                "nonceStr" : data.nonceStr,         //随机串  
                "package" : data.packageValue,      //<span style="font-family:微软雅黑;">商品包信息</span>  
                "signType" : data.signType,        //微信签名方式:  
                "paySign" : data.paySign           //微信签名  
                },function(res){
                    if(res.err_msg == "get_brand_wcpay_request:ok" ){  
                    	if(data.saleType == 2) {                   	 		  
                   	 		window.location.href=  encodeURI(qcCodeManager.basePath+"/weixin/store/qrCodeSuccess.do?qcCodeMoney="+data.qcCodeMoney);	
                   	 	}                   	 	
                    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                    	$.WapDialog.tip("支付已取消");
                    	//支付失败 删除订单
//                    	$.post(qcCodeManager.basePath+"/weixin/store/deleteQcCodeOrder.do",{"orderId":data.orderId},function(map){
//        					if(map.success == "success"){
//        						//跳转支付失败(取消)页面
//        					}else if(map.success == "fail"){
//        						$.WapDialog.tip("操作失败!请稍后再试!");
//        						//跳转支付失败(取消)页面
//        					}
//        				});
                    	
                    }else{
                    	$.WapDialog.tip("支付失败请重试");
                    	//支付失败 删除订单
                    	/*$.post(qcCodeManager.basePath+"/weixin/store/deleteQcCodeOrder.do",{"orderId":data.orderId},function(map){
        					if(map.success == "success"){
        						//跳转支付失败(取消)页面
        					}else if(map.success == "fail"){
        						$.WapDialog.tip("操作失败!请稍后再试!");
        						//跳转支付失败(取消)页面
        					}
        				});*/
                    }
                    qcCodeManager.submit=false;
                });
			
		} else if(data.state == "fail") {
			$.WapDialog.close();//解锁
			$.WapDialog.tip(data.reason);
			//支付失败 删除订单
			/*$.post(qcCodeManager.basePath+"/weixin/store/deleteQcCodeOrder.do",{"orderId":data.orderId},function(map){
				if(map.success == "success"){
					//跳转支付失败(取消)页面
				}else if(map.success == "fail"){
					$.WapDialog.tip("操作失败!请稍后再试!");
					//跳转支付失败(取消)页面
				}
			});*/
			qcCodeManager.submit=false;
		}
	};
}