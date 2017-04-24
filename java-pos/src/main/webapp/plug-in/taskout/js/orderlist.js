/**
 * 我的订单页管理类
 * 作者：jusnli
 * 邮箱：545987886@qq.com
 */
function OrderListMng(){
	this.basePath;	//根路径
	this.start=0;	//开始加载位置
	this.pageSize=15;//每次加载条数
	this.subming=false;//是否正在提交请求
	this.page = 2;	//页数，第一页为1，从1开始，第二页为2
	this.rows = 5; //每页加载条数
	this.unpayState = "unpay";//未支付
	this.daiShouhuoState = "custom_receiving";//待确认收货
	this.daiPingJiaState = "confirm";//待评价
	this.refundState = "refund";//退款
	this.deliveryState = "delivery";//待确认收货-1
	this.acceptState = "accept";//待确认收货-2
	this.payState = "pay";//待确认收货-3
	
	//初始化数据
	this.init = function(basePath){
		this.basePath=basePath;
		//首次加载订单列表
		this.load();
		
		//点击加载更多
		$("#loading_div").click(function(){
			orderListMng.load();
		});
		var tempH=0;//标识已经加载过的高度
		//滚动到底部自动加载更多
		$(window).scroll(function(e){
			var t=$(this).scrollTop()+$(this).height()+40;
			if(t>=$(document).height() && t>tempH){
				e.preventDefault();
				if(!orderListMng.subming){
					orderListMng.load();
					tempH=t;
				}
			}
		 });
	};
	
	this.lastedOrder = function(basePath) {
		var url = basePath + '/takeOutController/merchant/lastedorder.do';
		$.get(url, function(data) {
			if(data && data.state == "success") {
				var o = data.obj;
				var lng = o.lng;
				var lat = o.lat;
				var html = '';
				html += '<li class="status product_list clearfix">';
				html += '<a href="'+basePath+'/takeOutController/'+o.orderId+'orderdetail.do">';
				html += '<div class="pic"><img src="'+o.merchantLogo+'" alt="" /></div>';
				html += '<div class="product_info">';
				html += '<p class="shop_name">'+o.merchantName+'</p>';
				html += '<p class="tip">'+o.orderState+'</p>';
				html += '<p>';
				if(o.time)
					html += '<span class="times"><span>'+o.time+'</span></span>';
				html += '<span class="distant"><span id="distant">'+o.dis+'</span></span>';
				if(o.orderNum && o.orderNum!='')
					html += '<span class="paihao">排号:'+o.orderNum+'</span>';
				html += '</p>';
				html += '</div></a>';
				html += '<div class="call"><a href="tel:'+o.mobile+'"></a></div></li>';
				
				$("#orderList").append(html);
			}
		}, "json");
	}
	
	//加载订单列表
	this.load = function (){
		if(this.subming){
			return;
		}else{
			this.subming=true;
		}
		$("#loading_div").text('正在加载更多...');
		$.ajax({
			url:this.basePath+"/takeOutController.do?getOrderList",
			data:{start:this.start,pageSize:this.pageSize},
			dataType:'json',
			success:function(data){
				orderListMng.subming=false;
				$("#loading_div").text('点击加载更多');
				
				if(data.code==0){
					var orderList=data.orderList;
					if(orderList && orderList.length>0){
						orderListMng.showOrderList(orderList);//显示订单列表
					}
					if(orderList.length<orderListMng.pageSize){
						$("#loading_div").text('已加载全部');
					}
				}else{
					$.WapDialog.tip(data.msg);
				}
			},
			error:function(e){
				orderListMng.subming=false;
				$("#loading_div").text('点击加载更多');
				$.WapDialog.tip('请求数据失败');
			}
		});
		
	};
	
	//订单付款
	this.payOrder = function (orderId){
		window.location.href = encodeURI(basePath+"/takeOutController.do?payOrder&orderid=" + orderId +"&time=" +this.getSecond());
	};
	
	//显示订单详情
	this.showOrder = function (orderId){
		$.WapDialog.tip('该功能未开放');
	};
	
	//取消未付款订单
	this.cancelUnpayOrder = function (orderId){
		$.WapDialog.open({
			content:'请确认是否要取消订单?',
			cancel:{show:true},
			ok:{callBack:function(){
				$.WapDialog.close();
				$.WapDialog.lock({content:'正在处理...'});//锁屏
				$.ajax({
					url:orderListMng.basePath+'/takeOutController.do?cancelUnpayOrder&time='+orderListMng.getSecond(),
					data:{orderId:orderId},
					dataType:'json',
					success:function(data){
						$.WapDialog.close();
						if(data.code==-1){
							$.WapDialog.tip('登录已过期，请重新登录');
						}else if(data.code==500){
							$.WapDialog.tip('操作异常,请重试');
						}else if(data.code==0){//操作成功
							$.WapDialog.tip(data.msg);
							$('#order_id_'+orderId).slideUp('normal',function(){
								$(this).remove();
								orderListMng.start--;
							});
						}else{
							$.WapDialog.tip(data.msg);
						}
					},
					error:function(e){
						$.WapDialog.close();
						$.WapDialog.tip('操作失败，请重试');
					}
				});
			}}
		});
	};
	
	//未出单订单退款
	this.orderRefund = function (orderId){
		$('#refundReason').val('');
		$.WapDialog.open({
			content:$('#resason_list').prop("outerHTML"),
			cancel:{show:true},
			ok:{name:'申请退款',callBack:function(){
				$.WapDialog.close();
				var refundReason=$('#refundReason').val();
				$.WapDialog.lock({content:'正在处理...'});//锁屏
				$.ajax({
					url:orderListMng.basePath+'/takeOutController.do?orderRefund&time='+orderListMng.getSecond(),
					data:{orderId:orderId,refundReason:refundReason},
					dataType:'json',
					success:function(data){
						$.WapDialog.close();
						if(data.code==-1){
							$.WapDialog.tip('登录已过期，请重新登录');
						}else if(data.code==500){
							$.WapDialog.tip('操作异常,请重试');
						}else if(data.code==0){//操作成功
							$.WapDialog.tip(data.msg,3000);
							//操作成功后重新加载所有订单数据
							orderListMng.start=0;
							orderListMng.load();
						}else{
							$.WapDialog.tip(data.msg);
						}
					},
					error:function(e){
						$.WapDialog.close();
						$.WapDialog.tip('操作失败，请重试');
					}
				});
			}}
		});
	};
	
	//已出单申请退款
	this.askRefund = function (orderId){
		$('#refundReason').val('');
		$.WapDialog.open({
			content:$('#resason_list').prop("outerHTML"),
			cancel:{show:true},
			ok:{name:'申请退款',callBack:function(){
				$.WapDialog.close();
				var refundReason=$('#refundReason').val();
				$.WapDialog.lock({content:'正在处理...'});//锁屏
				$.ajax({
					url:orderListMng.basePath+'/takeOutController.do?askRefund&time='+orderListMng.getSecond(),
					data:{orderId:orderId,refundReason:refundReason},
					dataType:'json',
					success:function(data){
						$.WapDialog.close();
						if(data.code==-1){
							$.WapDialog.tip('登录已过期，请重新登录');
						}else if(data.code==500){
							$.WapDialog.tip('操作异常,请重试');
						}else if(data.code==0){//操作成功
							$.WapDialog.tip(data.msg,4000);
							//操作成功后重新加载所有订单数据
							orderListMng.start=0;
							orderListMng.load();
						}else{
							$.WapDialog.tip(data.msg);
						}
					},
					error:function(e){
						$.WapDialog.close();
						$.WapDialog.tip('操作失败，请重试');
					}
				});
			}}
		});
	};
	
	//已请求放弃了的提示
	this.hasAskRefund = function(){
		$.WapDialog.open({content:'亲!该订单您正在申请退款中，请耐心等待商家的审核，如商家久久未审核请直接电话联系商家，'
				+'另外你还可以在订单详情里查看订单进度或再次发起申请退款'});
	};
	
	//确认完成
	this.confirmComple = function (orderId){
		//暂时不提供该功能
	};
	
	//获取当前时间戳(自1970/1/1日至今的秒数)
	this.getSecond=function(){
		return Math.round(new Date().getTime()/1000);
	};
	
	//显示订单列表
	this.showOrderList = function(orderList){
		if(this.start==0){//start为0时情况列表
			$('#list').empty();
		}
		this.start +=orderList.length;
		$.each(orderList,function(k,vo){
			var html = '<div class="o_i" id="order_id_'+vo.id+'">';
				//第一行
				html += '     <div class="o_i_m">';
				html += '     	 <div class="o_i_m_l">订单号：'+vo.payId+'</div>';
				html += '     	 <div class="o_i_m_r">';//下单状态
				//unpay','pay','accept','unaccept','done','evaluated','confirm','refund','delivery_done','delivery'
				switch(vo.state){
					case 'unpay':{ 	html += '	<span class="unpay">未支付</span>';break;}
					case 'pay':{ 	html += '	<span class="payed">已支付(等待商家接单)</span>';break;}
					case 'accept':{ html += '	<span class="payed">商家已接单</span>';break;}
					case 'unaccept':{ html += '	<span class="payed">商家拒接单</span>';break;}
					case 'done':{ 	html += '	<span class="unpay">充值完成</span>';break;}
					case 'evaluated':{ html += '<span class="payed">已评价</span>';break;}
					case 'confirm':{ html += '	<span class="payed">已完成</span>';break;}
					case 'refund':{ html += '	<span class="unpay">订单已取消(已退款)</span>';break;}
					case 'delivery_done':{ html +='<span class="payed">配送完成</span>';break;}
					case 'delivery':{ html += '	<span class="payed">配送中</span>';break;}
					default :{ 		html += '	<span class="unpay">未知状态</span>';break;}
				};				
				html += '     	 </div>';
				html += '     	 <div class="clear"></div>';
				html += '    </div>';
				
				html += '     <div class="o_i_m">';
				html += '     	 <div class="o_i_m_l">';
				html += '	 		<a href="'+orderListMng.basePath+'/takeOutController.do?waimai&merchantid='+vo.merchantId+'">'+vo.title+'</a>';
				html += '     	 </div>';
				html += '     	 <div class="o_i_m_r">下单时间:'+(vo.createTime+'').substr(0,16)+'</div>';//下单时间
				html += '     	 <div class="clear"></div>';
				html += '    </div>';
				
				//第二行 
				html += '    <div class="o_i_h">';
				html += '     	 <div class="o_i_h_l">'+(vo.orderNum?('排号：'+vo.orderNum):'')+'</div>';
				html += '     	 <div class="o_i_h_r">'+(vo.saleType==1?'外卖':(vo.saleType==2?'堂食':'未知')) +'</div>';//订单类型
				html += '     	 <div class="clear"></div>';
				html += '    </div>';
				
				//订单食物列表
				html += '    <div class="o_m_l">';
				for(var i=0;i<vo.menuList.length;i++){
					var mvo=vo.menuList[i];
					html += '	<div class="o_m_l_i">';
					html += '			<div class="o_m_l_i_l" onclick="window.location.href=\''+orderListMng.basePath+'/takeOutController.do?menu&menuId='+mvo.menuId+'\';" >';
					html += '			<img src="'+mvo.image+'" />';
					html += '		</div>';
					html += '		<div class="o_m_l_i_c">';
					html += '			<a href="'+orderListMng.basePath+'/takeOutController.do?menu&menuId='+mvo.menuId+'">'+mvo.name+'</a>';
					html += '		</div>';
					//html += '		<div class="o_m_l_i_r">单价￥'+mvo.price+'</div>';
					html += '		<div class="o_m_l_i_r">x'+mvo.quantity+'</div>';
					html += '		<div class="o_m_l_i_p">￥'+mvo.totalPrice +'</div>';
					html += '	</div>';
				}
				html += '		<div class="clear"></div>';
				html += '	  </div>';
	
				//支付方式 -->
				if(vo.state=='unpay'){
					html += '<div class="o_p_l">';
					html += '	<div class="o_p_l_m">总金额<span class="payM">￥'+vo.origin+'</span></div>';
					html += '</div>';
				}else{
					html += '<div class="o_p_l">';
					html += '	<div class="o_p_l_t">';
					switch(vo.payType){
						case 'weixinpay':{html += '微信支付';break;}
						case 'alipay':{html += '支付宝';break;}
						case 'tenpay':{html += '财富通';break;}
						case 'balance':{html += '余额支付';break;}
						case 'unionpay':{html += '银联';break;}
						default:{html += '未知';break;}
					}
					html += '	</div>';
					html += '	<div class="o_p_l_ti">支付时间：'+(vo.payTime+'').substr(0,10)+'</div>';
					html += '	<div class="o_p_l_m">总金额<span class="payM">￥'+vo.origin+'</span></div>';
					html += '	<div class="clear"></div>';
					html += '</div>';
				}
				
				//联系人方式 
				if(vo.saleType==1){
					html +='<fieldset class="o_p_p" >';
					html +='	<legend class="o_p_p_t">联系人信息<em class="btnArrow"></em></legend>';
					html +='	<div class="o_p_p_d">';
					html +='		<div  class="o_p_p_n">姓名：'+vo.realName+'</div>';
					html +='		<div  class="o_p_p_p">电话：'+vo.mobile+'</div>';
					html +='		<div  class="o_p_p_a">送餐地址: '+vo.address+'</div>';
					html +='		<div  class="o_p_p_a">送餐备注: '+vo.timeRemark+'</div>';
					html +='	</div>';
					html +='</fieldset>';
				}
		
				//操作按钮
				html += '<div class="o_i_b_l">';
				html += '		<a href="javascript:void(0);" class="disabled" onclick="orderListMng.showOrder('+vo.id+')" >查看详情</a>';
				
				switch(vo.state){
					case 'unpay':{//状态：未付款 可以"取消订单" 和 "付款"
						html += '<a href="javascript:void(0);" class="normal" onclick="orderListMng.cancelUnpayOrder('+vo.id+')" >取消订单</a>';
						html += '<a href="javascript:void(0);" class="normal" onclick="orderListMng.payOrder('+vo.id+')">立刻支付</a>';
						break;
					}
					case 'pay':{//状态：已付款 可以直接退款
						html += '<a href="javascript:void(0);" class="normal"  onclick="orderListMng.orderRefund('+vo.id+')"  >申请退款</a>';
						break;
					}
					case 'accept':
					case 'cooked':
					case 'delivery':{//状态：已付款、商家接单、制作完成、配送中  可以申请退款
						//'normal','askrefund','berefund','norefund'
						if(vo.payState=='pay'){
							if(vo.rstate=='askrefund'){//已请求放弃了的
								html += '<a href="javascript:void(0);" class="disabled" onclick="orderListMng.hasAskRefund('+vo.id+')"  >已申请退款,等待商家审核</a>';
							}else if(vo.rstate=='norefund'){
								html += '<a href="javascript:void(0);" class="normal"  onclick="orderListMng.askRefund('+vo.id+')"  >商家拒绝退款,重新申请</a>';
							}else if(vo.rstate=='normal'){
								html += '<a href="javascript:void(0);" class="normal"  onclick="orderListMng.askRefund('+vo.id+')"  >申请退款</a>';
							}
						}
						//html += '<a href="javascript:void(0);" class="normal"  onclick="orderListMng.confirmComple('+vo.id+')" >确认收获</a>';
						break;
					}
					default:{break;}
				}
				html +='</div>';
			html +='	<div class="clear"></div>';
			html +='</div>';//class="o_i" 结束
			
			$("#list").append(html);
			$('.btnArrow').toggle(function(){
				$(this).parent().parent().addClass('o_p_p_s');
			},function(){
				$(this).parent().parent().removeClass('o_p_p_s');
			});
		});
	};
	
	//选中请求放弃原因
	this.selectReson = function(obj){
		var temp=$(obj).find('.reason_ico');
		if(temp.hasClass('reason_sel_ico')){
			temp.removeClass('reason_sel_ico');
			$('#refundReason').val('');
		}else{
			$(obj).siblings().find('.reason_sel_ico').removeClass('reason_sel_ico');
			temp.addClass('reason_sel_ico');
			$('#refundReason').val($(obj).text());
		}
	};

	//按订单状态显示订单列表
	this.orderListByState = function(data, orderState){
		if(data){
			
			//循环输出html列表
			$.each(data, function(k,v){
				var html = '';
				
				
				html += '<div  class="tksq">';
				html += '<p class="tksq_sj" merchantId="' + v.merchantId + '">' + v.merchantTitle + "</p>";
				
				
            html += '<div class="tksq_info clearfix">';
				html += '<a href="' + orderListMng.basePath + '/takeOutController/' + v.orderId + 'orderdetail.do" >';

				html += '<div class="tksq_pic"><img src="' + v.merchantLogoUrl + '" alt="" /></div>';
				html += '<div class="tksq_content ">';
				html += '<p><span>' + v.orderCreateTime + '</span></p>';
				html += '<p>应付：<span>￥' + v.oughtPayMoney + '</span></p></div>';
				html += '</a>';
				
				
				html += '<div class="tksq_status">';

				if(orderState){
					switch (orderState) {
					case orderListMng.unpayState: //未支付
						html += '<p style="color:#333">待付款</p>';
						html += '<a href="#" class="qrsh" orderId=' + v.orderId +' >去付款</a></div>';
						break;

					case orderListMng.daiShouhuoState: //待确认收货
					case orderListMng.acceptState: //待确认收货
					case orderListMng.payState: //待确认收货
					case orderListMng.deliveryState: //待确认收货
						html += '<p style="color:#333">待收货</p>';
						//新建display 为 S的评价数据
						html += '<a href="#" class="qrsh" orderId=' + v.orderId + ' merchantId=' + v.merchantId + ' courierId=' + v.courierId +' >确认收货</a></div>';
						break;
						
					case orderListMng.daiPingJiaState: //待评价
						if(v.evaluated == 0){
							html += '<p style="color:#333">待评价</p>';
							//修改 评价的 display 为  Y
							html += '<a href="#" class="qrsh" orderId=' + v.orderId + ' merchantId=' + v.merchantId + ' courierId=' + v.courierId +' >去评价</a></div>';
						}else{
							//已评价，但不显示
							html = '';
						}
						break;

					case orderListMng.refundState: //退款
						if(v.refundState == 'askrefund'){//申请退款，
							html += '<p style="color:#f00">退款申请中</p>';
						}else if(v.refundState == 'norefund'){//退款
							html += '<p style="color:#333">已退款</p>';
						}
						html += '</div>';
						break;

					default:
						break;
					}
				}
				
				$(".page").append(html);
			});
			
			if(orderState){
				switch (orderState) {
				case orderListMng.unpayState: //未支付
					//绑定待付款点击事件
					$(".qrsh").click(function() {
						var orderId = $(this).attr("orderId");
						//var paySuccessUrl = basePath + "/takeOutController/mineWaitingPay.do";
						orderListMng.toPay(orderId);
					});
					break;

				case orderListMng.daiShouhuoState: //待确认收货
				case orderListMng.acceptState: //待确认收货
				case orderListMng.payState: //待确认收货
				case orderListMng.deliveryState: //待确认收货
					//绑定确认收货点击事件
					$(".qrsh").click(function() {
						var orderId = $(this).attr("orderId");
						var merchantId = $(this).attr("merchantId");
						var courierId = $(this).attr("courierId");
						var successUrl = basePath + "/takeOutController.do?mineWaitingReceipt";
						//点击确认收货
						orderListMng.mineOrderConfirmReceipt(orderId, merchantId, courierId, successUrl);
					});
					break;
					
				case orderListMng.daiPingJiaState: //待评价
					//绑定评价点击事件
					$(".qrsh").click(function() {
						var orderId = $(this).attr("orderId");
						var merchantId = $(this).attr("merchantId");
						var courierId = $(this).attr("courierId");
						if(orderId && merchantId && courierId){
							//var successUrl = basePath + "/takeOutController.do?mineWaitingEvaluates";
							//点击评价订单
							orderListMng.wxJump2NewEvaluate(orderId, merchantId, courierId);
						}
					});
					break;

				case orderListMng.refundState: //退款
					break;

				default:
					break;
				}
			}
			
			//绑定去商铺点击事件
			$(".tksq_sj").click(function() {
				var merchantId = $(this).attr("merchantId");
				//点击跳转到商铺页面，传递商铺id过去
				orderListMng.wxJump2Merchant(merchantId);
			});
		}
		
	};

	//订单确认收货 , successUrl: 跳转到成功的url
	this.mineOrderConfirmReceipt = function (orderId, merchantId, courierId, successUrl){
		//如果正在提交订单支付，返回 
		if(orderListMng.subming) return;
		orderListMng.subming = true;
		var commentDisplay = "S";
		$.WapDialog.lock({content:'正在确认收货...'});
		var url = basePath+"/takeOutController.do?mineOrderConfirmReceipt";
		//确认收货 
		$.ajax({
			url: url,
			data:{
				orderId: orderId,
				merchantId: merchantId,
				courierId: courierId,
				commentDisplay: commentDisplay
			},
			type: "POST",
			dataType:'json',
			contentType:"application/x-www-form-urlencoded; charset=utf-8",
			success: function(data){
				$.WapDialog.close();
				if(data.success == true){
					$.WapDialog.open({content:'确认收货成功,正在返回我的待收货',ok:{show:false},times:1000});
	        		orderListMng.subming = false;
					setTimeout(function(){
						window.location.href = successUrl;
					},1000);
				}else{
	        		orderListMng.subming = false;
					$.WapDialog.tip(data.msg);
				}
			},
			error:function(e){
        		orderListMng.subming = false;
				$.WapDialog.close();
				$.WapDialog.tip("操作失败,请重试");
			}
		});
	};
	

	// 跳转到商铺页面
	this.wxJump2Merchant = function (merchantId){
		window.location.href = encodeURI(basePath+"/takeOutController/menu/" + merchantId +".do?time=" +this.getSecond());
	};

	// 跳转用户添加评价页面
	this.wxJump2NewEvaluate = function (orderId, merchantId, courierId){
		window.location.href = encodeURI(basePath+"/takeOutController.do?mineEvaluateNew&time=" +this.getSecond() + "&orderId=" + orderId + "&merchantId=" + merchantId + "&courierId=" + courierId + "&commentDisplay=Y");
	};
	
	/**
	 * 加载更多列表
	 * loadUrl, 需要加载的完全url
	 * 
	 */
	this.loadMore = function (loadUrl, orderState){
		if(this.subming){
			return;
		}else{
			this.subming=true;
		}
		$("#loading_div").text('正在加载更多...');
		$.ajax({
			url: loadUrl,
			data: {page: orderListMng.page, rows: orderListMng.rows, state: orderState},
			dataType: 'json',
			success: function(data){
				orderListMng.subming = false;
				$("#loading_div").text('点击加载更多');
				
				if(data.success){
					var orderList = eval('(' + data.obj + ')');
					if(orderList && orderList.length > 0){
						orderListMng.page ++;
						//if(orderState == orderListMng.unpayState){
							orderListMng.orderListByState( orderList , orderState);//显示订单列表
						//}
					}
					if(orderList.length < orderListMng.rows){
						$("#loading_div").text('已加载全部');
					}
				}else{
					$.WapDialog.tip(data.msg);
				}
			},
			error:function(e){
				orderListMng.subming=false;
				$("#loading_div").text('点击加载更多');
				$.WapDialog.tip('请求数据失败');
			}
		});
		
	};
	

	this.toPay = function(orderId) {
		if(orderListMng.subming)return;
		orderListMng.subming=true;
		$.WapDialog.lock({content:'正在进入支付...'});
		var url = orderListMng.basePath + '/takeOutController/wxpay/'+orderId+'.do';
		$.post(url, {"orderId":orderId}, function(data){
			orderListMng.wxpay(data);
		}, "json");
	};
	
	this.wxpay = function(data) {
		if(data.state == "success") {
			/*
			if(parseInt(data.agent) < 5){  
               	 $.WapDialog.close();//解锁
               	 $.WapDialog.tip("您的微信版本低于5.0无法使用微信支付");  
               	 orderListMng.subming=false;
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
                   	 	/*if(data.saleType == 1) {
                	 		window.location.href=  encodeURI(orderListMng.basePath+"/takeOutController.do?merchantList");
                	 	} else {
                	 		window.location.href=  encodeURI(orderListMng.basePath+"/takeOutController/"+data.orderId+"orderdetail.do");
                	 	}*/
                   	 	window.location.href=  encodeURI(orderManager.basePath+"/coupons/share.do?score="+data.preScore);
                    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                    	$.WapDialog.lock({content:"支付已取消"});
                   	 	window.location.href=  encodeURI(orderListMng.basePath+"/takeOutController/mineWaitingPay.do");
                    }else{
                    	$.WapDialog.lock({content:"支付失败，请重试"});  
                   	 	window.location.href=  encodeURI(orderListMng.basePath+"/takeOutController/mineWaitingPay.do");
                    }
                    orderListMng.subming=false;
                });
			
		} else if(data.state == "fail") {
			$.WapDialog.close();
    		$.WapDialog.lock({content:data.reason});
    		window.location.href=  encodeURI(orderListMng.basePath+"/takeOutController/mineWaitingPay.do");
		}
	};
	
}