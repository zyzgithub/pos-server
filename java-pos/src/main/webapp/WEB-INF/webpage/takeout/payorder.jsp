<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
  <head>
  	<%@ include file="./common.jsp" %>
    <title>${baseTitle }</title>
 	<script type="text/javascript">
 		var basePath="${basePath}";//跟路径
 		//应付金额
		var payMoney = parseFloat("${total}");
		//原选择代金券金额
		var oldCardMoney = 0;
		//现选择代金券金额
		var newCardMoney = 0;
		//优惠券ID
		var cardid = '';
		//代金券选择次数
		var chooseNum = 0;
		//积分金额
		var scoreMoney = 0;
		var isWeixin=false;
		var submit=false;
		$(function(){
			var userAgent=window.navigator.userAgent.toLowerCase();
			if(userAgent.match(/MicroMessenger/i)=="micromessenger"){
				isWeixin=true;
			}
			if(isWeixin){//是微信时需要在微信js接口加载好后才绑定支付按钮单击事件
				document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
					bindPayButtonEvent();
				}, false);
			}else{//不是微信时直接绑定支付按钮单击事件
				bindPayButtonEvent();
			}
		});
		
		//绑定支付按钮事件
		function bindPayButtonEvent(){
			$('#payButton').click(function(){
				if(submit){
					return;
				}
				submit=true;
				if($('#useBalance').attr('checked') == "checked"){   //余额支付
					$.WapDialog.lock({content:'正在付款,付款后如果界面未跳转请手动刷新'});//锁屏
	        		window.location.href = encodeURI(basePath+"/takeOutController.do?balancePay&orderid="+ $('#orderid').val() +"&payMoney="+payMoney+"&cardid="+cardid+"&cardMoney="+newCardMoney+"&scoreMoney="+scoreMoney);
	        	}else{//微信支付
	        		$.WapDialog.lock({content:'正在进入微信支付...'});//锁屏
	        		if(isWeixin){
	        			try{
	        				$.ajax({  
				                url: basePath+"/takeOutController.do?getPayInfo&time=" + Math.round(new Date().getTime()/1000),
				                data:{
				                	code: $('#code').val(),
				                	orderid: $('#orderid').val(),
				                	payMoney:payMoney,
				                	cardMoney:newCardMoney,
				                	cardid:cardid,
				                	newCardMoney:newCardMoney,
				                	scoreMoney:scoreMoney
				                },  
				                success:function(data){
					  				 var d = JSON.parse(data);
				                     if(d.stateCode == "01"){
				                    	$.WapDialog.close();//解锁
				                    	submit=false;
				                    	$.WapDialog.tip('支付失败：'+d.msg);
				                     	return;
				                     }
				                     if(parseInt(d.obj.agent) < 5){  
				                    	 $.WapDialog.close();//解锁
				                    	 submit=false;
				                    	 $.WapDialog.tip("您的微信版本低于5.0无法使用微信支付");  
				                         return;  
				                     }  
				                     WeixinJSBridge.invoke('getBrandWCPayRequest',{  
				                         "appId" : d.obj.appId,                  //公众号名称，由商户传入  
				                         "timeStamp":d.obj.timeStamp,          //时间戳，自 1970 年以来的秒数  
				                         "nonceStr" : d.obj.nonceStr,         //随机串  
				                         "package" : d.obj.packageValue,      //<span style="font-family:微软雅黑;">商品包信息</span>  
				                         "signType" : d.obj.signType,        //微信签名方式:  
				                         "paySign" : d.obj.paySign           //微信签名  
				                         },function(res){
				                        	 $.WapDialog.close();//解锁
				                        	 submit=false;
					                         if(res.err_msg == "get_brand_wcpay_request:ok" ){  
					                        	 $.WapDialog.lock({content:'支付成功，正在跳转...'});
					                             window.location.href=  encodeURI(basePath+"/takeOutController.do?payresult&orderid="+$('#orderid').val()+"&merchantid="+$('#merchantId').val()+"&time=" + Math.round(new Date().getTime()/1000));
					                         }else if(es.err_msg == "get_brand_wcpay_request:cancel"){
					                        	 $.WapDialog.tip("支付已取消");
					                         }else{
					                        	 $.WapDialog.tip("支付失败，请重试");  
					                         }
				                         });
				                },error:function(e){
				                	 $.WapDialog.close();//解锁
		                        	 submit=false;
				                	 $.WapDialog.tip("抱歉,操作失败，请重试");  
				                }
				               });
	        			}catch(e){
	        				$.WapDialog.tip("抱歉,操作异常，请重试");  
	        			}
	    			}else{
	    				$.WapDialog.close();
	    				$.WapDialog.tip("抱歉！非微信请使用余额支付吧!"); 
	    				submit=false;
	    			} 
		        }
			});
		}
		
		//点击显示代金券
		function show(){
			if($("#useGroupon").attr("checked") == "checked"){
				$('#cardDiv').show();
				chooseNum = 0;
			}else{
				$('#cardDiv').hide();
				
				if($('input:radio[name="card"]:checked').val() != undefined){
					payMoney =Number(payMoney) + Number($('input:radio[name="card"]:checked').val());
				}
				
				$("input[name='card']").removeAttr("checked");
				$('#paySpan').html("￥"+payMoney.toFixed(2));
				chexkBalance();
			}
		}
		
		//代金券
		function getCard(){
			//选择的代金券的金额
			++chooseNum;
			if(chooseNum > 1){
				oldCardMoney = newCardMoney;
			}
			newCardMoney = $('input:radio[name="card"]:checked').val(); 
			cardid = $('input:radio[name="card"]:checked').attr("id");
			
			payMoney = Number(payMoney) + Number(oldCardMoney) - Number(newCardMoney);
			$('#paySpan').html("￥"+payMoney.toFixed(2));
			chexkBalance();
		}
		
		//积分
		var oldMoney = 0;//积分抵消的金额
		var userScore=parseInt("${user.score}");//用户积分
		var d_score=parseInt("${d_score}");							//积分规则兑换积分
 		var d_score_money=parseFloat("${d_score_money}");			//积分规则兑换金额
 		var d_score_total_prices=parseFloat("${d_score_total_prices}");//积分规则起兑金额
		function getScore(){
 			if(payMoney<d_score_total_prices)return;
 			if(userScore<d_score)return;
 			
			if($('#useScore').attr("checked") == "checked"){
				if(payMoney < d_score_money){
					oldMoney = payMoney;
					payMoney = 0;
				}else{
					payMoney = payMoney - d_score_money;
					scoreMoney = d_score_money;
				}
			}else{
				if(oldMoney != 0){
					payMoney = oldMoney;
				}else{
					payMoney = payMoney + d_score_money;
				}
				scoreMoney = 0;
			}
			$('#paySpan').html("￥"+payMoney.toFixed(2));
			chexkBalance();
		}
		
		//检查余额是否足够支付
		function chexkBalance(){
			var balance = $('#balance').val();
			if(balance > payMoney){
				$('#useBalance').removeAttr("disabled");
				$('#balanceDiv').css("display","none");
			}else{
				$('#useBalance').attr("disabled","disabled");
			}
		}
		
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');//隐藏右上角菜单
        });
		
 	</script>
  </head>
  
  <body>
  	<input type="hidden" id="code" name="code" value="${sessionScope._WEI_XIN_CODE_}">
  	<input type="hidden" id="orderid" name="orderid" value="${order.id}">
  	<input type="hidden" id="merchantId" name="merchantId" value="${order.merchant.id}">
  	<input type="hidden" id="score" name="score" value="${user.score}">
  	<input type="hidden" id="balance" name="balance" value="${user.money}">
  	<div class="head">
  		<div class="head_back" >
			<a href="${basePath }/takeOutController.do?waimai&merchantid=${order.merchant.id}"  class="head_title" >
				<em class="backBtn_ico"></em>返回
			</a>
		</div>
  		<div class="head_l">
	  		<span class="head_title" >支付</span>
  		</div>
		<div class="head_r">
			<a href="${basePath }/takeOutController.do?orderList" class="head_title">我的订单</a>
		</div>
	</div>
	<div class="main">
	  	<div style="width:100%;">
		    <table style="border-bottom:1px solid #eee;width:100%;">
		    	<c:forEach items="${chooseList}" var="vo">
		    		<tr style="height:50px;">
		    			<td style="width:40%;">
		    				<span class="span_menu_type">${vo.typename }</span><br/>
		    				<span class="span_menu_name">${vo.name }</span>
		    			</td>
		    			<%-- <td style="width:20%;" >单价:￥<fmt:formatNumber value="${vo.price}"  pattern="#0.00"></fmt:formatNumber></td> --%>
		    			<td style="width:20%;color:red;" >
		    				${vo.num }份
		    			</td>
		    			<td style="width:20%;color:red;font-weight:bold;" align="center">
		    				<span id="total_${vo.id}" style="font-size:20px">￥<fmt:formatNumber value="${vo.price}"  pattern="#0.00"></fmt:formatNumber></span>
		    			</td>
		    		</tr>
		    	</c:forEach>
		    </table>
	
		  <table style="width:100%;padding-top:10px;">
	    		<tr>
	    			<td style="padding-left:5px;width:50%;font-size:1.4em;color:#969696;" align="left">食品数量：<span id="allnum">${allnum}</span></td>
	    			<td align="right">
	    				<font style="color:#969696;font-size:1.4em;">订单金额：￥<fmt:formatNumber value="${total}"  pattern="#0.00"></fmt:formatNumber> </font>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="right"  style="font-size:1.4em;color:#969696;">应付金额：<span style="font-weight:bold;color:red;" id="paySpan">￥<fmt:formatNumber value="${total}"  pattern="#0.00"></fmt:formatNumber></span></td>
	    		</tr>
	    	</table>
	    </div>
	    
	    <c:if test="${order.saleType == 1}">
		    <div class="line lable_title">联系方式</div>
		    <div style="background-color:white;padding-top:5px;padding-bottom:5px;padding-left:10px;" align="center">
		    	<table style="width:100%">
		    		<tr style="height:30px;">
		    			<td style="width:30%;font-size:15px;color:7F7F7F;">姓名</td>
		    			<td style="font-size:15px;color:7F7F7F;">${order.realname}</td>
		    		</tr>
		    		
		    		<tr style="height:30px;">
		    			<td style="font-size:15px;color:7F7F7F;">联系电话</td>
		    			<td style="font-size:15px;color:7F7F7F;">${order.mobile}</td>
		    		</tr>
		    		
		    		<tr style="height:30px;">
		    			<td style="font-size:15px;color:7F7F7F;">地址</td>
		    			<td style="font-size:15px;color:7F7F7F;">${order.address}</td>
		    		</tr>
		    		
		    		<tr style="height:30px;">
		    			<td style="font-size:15px;color:7F7F7F;">送餐时间</td>
		    			<td style="font-size:15px;color:7F7F7F;">${order.timeRemark}</td>
		    		</tr>
		    	</table>
		    </div>
	    </c:if>    
	    <c:choose>
	    	<c:when test="${order.payState eq 'unpay'}">
	    		<c:if test="${fn:length(cardList) > 0 || user.score > 0 || user.money > 0}">
			    <div  class="line lable_title">支付方式</div>
			    <div style="background-color:white;padding-top:5px;padding-bottom:5px;">
					   <%--  <c:if test="${fn:length(cardList) > 0}"> --%>
						    <div class="pay_type_li">
						    	<input type="checkbox" id="useGroupon" name="useGroupon" ${fn:length(cardList)>0?'onchange="show()"':'disabled' }  style="margin-left:10px;">&nbsp;&nbsp;
						    	<font style="font-size:1.2em;">使用代金券</font>
						    	<font style="color:#969696;">
							    	<c:choose>
							    		<c:when test="${ fn:length(cardList)>0 }">（${fn:length(cardList)>0?'':''} 张</c:when>
							    		<c:otherwise>（无可用代金券）</c:otherwise>
							    	</c:choose>
						    	</font>
						    	<div id="cardDiv" style="display:none;">
						    		<table style="width:90%;margin-left:30px;">
						    			<c:forEach items="${cardList}" var="vo">
						    				<tr style="height:20px;">
						    					<td>
						    						<input type="radio" id="${vo.id}" name="card" value="${vo.credit}" onchange="getCard();">${vo.title}
						    					</td>
						    					<td><font style="font-weight:bold;color:red;">${vo.credit}&nbsp;￥</font></td>
						    				</tr>
						    			</c:forEach>
						    		</table>
						    	</div>
						    </div>
					    <%-- </c:if> --%>
					    
					    <%-- <c:if test="${user.score > 0 && total > d_score_total_prices}"> --%>
						   	<div  class="pay_type_li">
						   		<input type="checkbox" id="useScore" name="useScore"  style="margin-left:10px;" 
						   		${user.score < d_score or total<d_score_total_prices?'disabled':'onchange="getScore()"'} >&nbsp;&nbsp;
			    				<font style="color:#615E5E;font-weight:bold;font-size:14px;">使用积分</font>
			    				<c:choose>
			    					<c:when test="${user.score < d_score or total<d_score_total_prices}">
			    						<span style="color:#969696">（少于${d_score }积分或购物金额小于${d_score_total_prices }元不能使用）</span>
			    					</c:when>
			    					<c:otherwise>
			    					 	<span style="color:#969696;">（每个订单能使用${d_score }积分抵${d_score_money}元）</span>
			    					</c:otherwise>
			    				</c:choose>
						    	<span style="float:right;">共有 <font style="font-weight:bold;color:red;">${user.score}</font> 积分</span>	
						    </div>
					   <%--  </c:if> --%>
					    
					   <%--  <c:if test="${user.money > 0}"> --%>
						    <div  class="pay_type_li">
						    	<input type="checkbox" id="useBalance" name="useBalance" ${user.money>0?'onchange="show()"':'disabled' } style="margin-left:10px;" <c:if test="${user.money < total}">disabled</c:if>>&nbsp;&nbsp;
			    				<font style="color:#615E5E;font-weight:bold;font-size:14px;">使用余额</font>&nbsp;&nbsp;&nbsp;
			    				<span style="color:#CD0000;<c:if test="${user.money >= total}">display:none;</c:if>" id="balanceDiv" >（当前余额不足）</span>
			    				<span style="float:right;">剩余金额：￥<font style="font-weight:bold;color:red;"> ${user.money} </font></span>
						    </div>
					    <%-- </c:if> --%>
				   </div>
				</c:if>   
			
			    <div style="font-size:13px;margin-top:5px;color:7F7F7F;">
			    	请您在提交订单后20分钟内完成支付，否则订单将自动取消。如支付失败，请到我的订单中继续支付或取消订单。
			    </div>
			    <div style="margin-top:30px;">
					<a id="payButton" href="javascript:void(0);" class="btn_common ok_btn">立即支付</a>
			    </div>
	    	</c:when>
	    	<c:otherwise>
	    		<div  class="payMsg" >
		    		 亲,你已经支付该订单了哦!
		   		 </div>
		   		 <div style="margin-top:30px;">
					<a  href="${basePath }/takeOutController.do?waimai&merchantid=${order.merchant.id}" class="btn_common ok_btn">继续点餐</a>
			    </div>
	    	</c:otherwise>
	    </c:choose>
		
    </div>
  </body>
</html>
