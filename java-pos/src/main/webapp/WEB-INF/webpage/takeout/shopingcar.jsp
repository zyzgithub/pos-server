<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
  	<%@ include file="./common.jsp" %>
    <title>${baseTitle }</title>
    <style type="text/css">
    .sel_timeList:focus {outline: none;}
    </style>
  	<script type="text/javascript" src="${basePath}/plug-in/taskout/js/shopingcar.js?id=4"></script>
  	<script type="text/javascript">
  		var basePath="${basePath}";
  		var carManager;		//购物车管理
  		$(function(){
  			carManager=new CarManager();
  			carManager.init(basePath,"${merchant.id}","${merchant.deliveryBegin}",$.parseJSON('${params}'));
  			$(".head_back").click(function(){
  				if(document.referrer){
  					window.history.back();
  				}else{
  					window.location.href=basePath+"/takeOutController.do?waimai&merchantid="+carManager.merchantId;
  				}
  			});
  		});
  		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
  	</script>
  </head>
  
  <body>
  	<div class="head">
		<div class="head_back" >
			<a href="javascript:void(0);"  class="head_title" >
				<em class="backBtn_ico"></em>返回
			</a>
		</div>
  		<div class="head_l">
	  		<span class="head_title" >购物车</span>
  		</div>
	</div>
	<div class="main">
	  	<div style="padding-top:8px;padding-bottom:8px;margin-left:5px;border-bottom: 1px dotted #eee;" align="center">
	  		<div id="nullShopCarMsg" class="nullShopCarMsg">
		  				<h3>购物车还是空的</h3>
		  				<a href="${basePath }/takeOutController.do?waimai&merchantid=${merchant.id}">去逛逛</a>&nbsp;&nbsp;&nbsp;
		  				<a href="${basePath }/takeOutController.do?orderList">我的订单</a>
		  	</div>
		  	<table style="width:100%;" id="menuList">
		    </table>
	    </div>
	    
	    <div  class="line lable_title">就餐方式</div>
	    <div style="margin-left:5px;background-color:white;padding-top:8px;padding-bottom:8px;padding-left:10px;">
	    	<table style="width:100%;">
	    		<tr>
	    			<td><label class="lable_selectType"><input type="radio" name="saleType" value="1"  checked="checked"> 外卖</label></td>
	    			<td><label class="lable_selectType"><input type="radio" name="saleType" value="2" > 堂食</label></td>
	    		</tr>
	    	</table>
	    </div>
	    
	   	<div id="customerDiv">
		    <div class="line lable_title">联系人信息</div>
		    <div style="background-color:white;padding-top:5px;padding-bottom:5px;" align="center">
		    	<table style="width:100%">
		    		<tr style="height:30px;">
		    			<td style="width:100px;font-size:1.3em;text-align: right;">联系人：</td>
		    			<td>
		    				<input type="text" id="customerName" name="customerName"  value="${ad.name}" placeholder="您的姓名" class="input_text" />
		    				<span class="error_msg"></span>
		    			</td>
		    		</tr>
		    		
		    		<tr style="height:30px;">
		    			<td style="width:100px;font-size:1.3em;text-align: right;">联系电话：</td>
		    			<td>
		    				<input type="text" id="phone" name="phone" value="${ad.mobile}"  placeholder="您的电话" class="input_text" />
		    				<span class="error_msg"></span>
		    			</td>
		    		</tr>
		    		
		    		<tr style="height:30px;">
		    			<td style="width:100px;font-size:1.3em;text-align: right;">送餐地址：</td>
		    			<td>
		    				<input type="text" id="address" name="address"  value="${ad.addressDetail}"  placeholder="请尽量详细" class="input_text" />
		    				<span class="error_msg"></span>
		    			</td>
		    		</tr>
		    		<tr style="height:30px;">
	    			<td style="width:100px;font-size:1.3em;text-align: right;">送餐时间：</td>
	    			<td>
	    				<input type="text" id="timeRemark" name="timeRemark" value="12:00-12:15" placeholder="选择送餐时间" class="input_text" />
	    				<span class="error_msg"></span>
	    			</td>
	    		</tr>
	    	</table>
	    </div>
    </div>
    
    <div style="padding-top:8px;padding-bottom:8px;padding-bottom:5px;border-top:solid 1px #eee;">
    	<table>
    		<tr>
    			<td style="width:50%;font-size:17px;">食品数量：<span id="totalNum">0</span></td>
    			<td style="font-size:17px;">支付金额：￥<span style="font-weight:bold;color:red;font-size:18px;" id="totalMoney">0.00</span></td>
    		</tr>
    	</table>
    </div>
    
    <div id="errMsg" style="margin-top:20px;font-weight:bold;color:red;display:none;" align="center">请先点餐</div>
	    <div style="margin-top:10px;">
	    	<a id="submitOrder" href="javascript:void(0);" class="btn_common ok_btn" >提交订单</a>
	    </div>
    </div>
    
    <div class="timeListDiv" style="display: none;">
    	<div class="currentDay" style="line-height: 1.5em;float:left;font-size:1.2em;width:30%;text-align: right;">${currentDay }</div>
		<select class="sel_timeList" style="line-height: 1.5em;font-size:1.2em;margin-left:5%;width:60%;border:solid 1px #eee;float:right;" size="7" >
			<c:forEach var="time" items="${timeList }" varStatus="i">
				<option value="${time }" <%-- ${i.index==0?'selected="selected"':'' } --%> >${time }</option>
			</c:forEach>
		</select>
	</div>
    </body>
</html>
