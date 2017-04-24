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
		<div id="codepay-box" class="co-main-box center-block text-center">
			<h1 class="co-main-text red">1号外卖扫码支付</h1>
			<p class="co-sub-text">向<span class="dark-grey">${merchantTitle}</span>付费</p>
			<div id="co-paper-box" class="center-block">
				<div id="co-paper-bg"><img src="${basePath}/plug-in/taskout/images/icon_kuang.png"/></div>
				<div id="co-paper-cnt" class="center-block">
					<div class="center-block">
						<span class="co-sub-text co-price-de" id="co-price-left">费用</span>
						<span class="co-sub-text co-price-de" id="co-price-right">元</span>
						<input id="co-price-box" class="center-block dark-grey"  autofocus type="number" value="" onKeyUp="amount(this)" onBlur="overFormat(this)" />
					</div>
					<button class="center-block co-button co-pay-button text-center" id="qrzf" onclick="submit()">确认支付</button>
				</div>
			</div>
		</div>
<script type="text/javascript" src="${basePath}/plug-in/taskout/js/qrcode.js" /></script>
<script type="text/javascript">
		$(function(){
			qcCodeManager = new qcCodeManager();
			qcCodeManager.init('${basePath}');
		})

	function submit(){
				 var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,2})?$)/;
				 var money = $("#co-price-box").val().trim();
			        if (money == "") {
			        	$.WapDialog.tip("商品价格不能为空哦~！");
			            return false;
			        }else if(money*1 <=0){
			        	$.WapDialog.tip("价格不能为0或者负数哦~!");
			        } else if(money*1 >= 10000){
			        	$.WapDialog.tip("不能支付超过一万哦~!");
			        }else {
			            if (!reg.test(money)) {
			            	$.WapDialog.tip("必须为合法数字(正数，最多两位小数)哦~！");
			                return false;
			            } else {
			            	qcCodeManager.createOrder(money,'${merchantId}',2,'weixinpay');           
			            }
			        }
		}
		
function amount(th){
    var regStrs = [
        ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
        ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
        ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
        ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
    ];
    for(i=0; i<regStrs.length; i++){
        var reg = new RegExp(regStrs[i][0]);
        th.value = th.value.replace(reg, regStrs[i][1]);
    }
}


function overFormat(th){
    var v = th.value;
    if(v === ''){
        v = '0.00';
    }else if(v === '0'){
        v = '0.00';
    }else if(v === '0.'){
        v = '0.00';
    }else if(/^0+\d+\.?\d*.*$/.test(v)){
        v = v.replace(/^0+(\d+\.?\d*).*$/, '$1');
        v = inp.getRightPriceFormat(v).val;
    }else if(/^0\.\d$/.test(v)){
        v = v + '0';
    }else if(!/^\d+\.\d{2}$/.test(v)){
        if(/^\d+\.\d{2}.+/.test(v)){
            v = v.replace(/^(\d+\.\d{2}).*$/, '$1');
        }else if(/^\d+$/.test(v)){
            v = v + '.00';
        }else if(/^\d+\.$/.test(v)){
            v = v + '00';
        }else if(/^\d+\.\d$/.test(v)){
            v = v + '0';
        }else if(/^[^\d]+\d+\.?\d*$/.test(v)){
            v = v.replace(/^[^\d]+(\d+\.?\d*)$/, '$1');
        }else if(/\d+/.test(v)){
            v = v.replace(/^[^\d]*(\d+\.?\d*).*$/, '$1');
            th = false;
        }else if(/^0+\d+\.?\d*$/.test(v)){
            v = v.replace(/^0+(\d+\.?\d*)$/, '$1');
            th = false;
        }else{
            v = '0.00';
        }
    }
    th.value = v; 
}
</script>
	</body>
</html>