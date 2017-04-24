/**
 * 购物车页管理类
 * 作者：jusnli
 * 邮箱：545987886@qq.com
 * @returns
 */
function CarManager(){
	this.merchantId=0;					//商家id
	this.deliveryBegin=0;				//起送金额
	this.basePath="";					//跟路径
	this.requestsId=['customerName','phone','address','timeRemark'];//表单提交必须项id
	this.menuList=[];					//我的购物车选择菜数据,格式[{菜品数据对象,count:选购数量}]
	this.submit=false;					//是否提交中
	//初始化
	this.init = function(basePath,merchantId,deliveryBegin,menuList){
		carManager.basePath=basePath;
		carManager.merchantId=merchantId;
		carManager.deliveryBegin=deliveryBegin;
		carManager.menuList=menuList;
		carManager.bindEvent();
		carManager.showList();
	};
	//显示菜单列表
	this.showList = function(){
		var totalMoney=0.0;
		var totalNum=0;
		if(carManager.menuList.length>0){
			var html='';
			$.each(carManager.menuList,function(k,v){
				html +='<tr  class="line" style="height:50px;">';
				html +='	<td style="width:35%;" >';
				html +='		<span class="span_menu_type">'+v.typename+'</span><br/>';
				html +='		<span class="span_menu_name">'+v.name+'</span>';
				html +='	</td>';
				html +='	<td style="width:15%;" >单价:￥'+v.price+'</td>';
				html +='	<td style="width:15%;color:red;font-weight:bold;" align="center">￥<span class="span_price" id="item_total_'+v.menuId+'">'+(v.price*v.selCount).toFixed(2)+'</span></td>';
				html +='	<td style="width:35%;" >';
				html +='		<a href="javascript:void(0);" class="minus" id="minus_'+v.menuId+'"  onclick="carManager.minus('+v.menuId+')" >　</a>';
				html +='		<input type="text" class="sum"  disabled="disabled" id="sum_'+v.menuId+'" value="'+v.selCount+'"  />';
				html +='		<a href="javascript:void(0)" class="add" onclick="carManager.add('+v.menuId+')" >　</a>';
				html +='	</td>';
				html +='</tr>';
				totalNum +=v.selCount;
				totalMoney +=v.selCount*v.price;
			});
			$('#menuList').html(html);
			$('#totalNum').text(totalNum);
			$('#totalMoney').text(totalMoney.toFixed(2));
			$("#nullShopCarMsg").hide();
		}else{
			$("#nullShopCarMsg").show();
			$('#menuList').hide();
		}
		carManager.reflesShopCart();
	};
	//绑定控件事件
	this.bindEvent = function(){
		//必填项
		for(var i=0;i<carManager.length;i++){
		  	$('#'+carManager.requestsId[i]).blur(function(){
		  		if(!$(this).val()){
					 $(this).siblings('.error_msg').text('必填');
				 }else{
				 	 $(this).siblings('.error_msg').text('');
				 }
		  	});
		 }
		
		//选择外卖或堂食
		$("input[name=saleType]").click(function(){
			if($(this).val() == 1){
				$('#customerDiv').show();
			}else{
				$('#customerDiv').hide();
			}
			carManager.reflesShopCart();
		});
		$('#submitOrder').click(function(){
			carManager.submitOrder();
		});
		$('#timeRemark').focus(function(){
			carManager.selectTime();
		});
	};
	//添加到购物车菜品数
	this.add = function(menuId){
		var menuIndex=carManager.getMenuIndex(menuId);
		var menu=carManager.menuList[menuIndex];
		var selCount=menu.selCount;
		if(menu.today_repertory<selCount+1){//先判断当前库存是否大于0
			if(menu.today_repertory==0){
				$.WapDialog.tip('抱歉!该菜品今天已售完,明天再来吧');
			}else{
				$.WapDialog.tip('超出今日库存');
			}
			return;
		}else{
			if(menu.salesPromotion=='Y'){//判断是否是否促销
				if(menu.promotionQuantity<selCount+1){//促销数量不足
					if(menu.promotionQuantity==0){
						$.WapDialog.tip('抱歉!该促销已售完');
					}else{
						$.WapDialog.tip('超出促销购量');
					}
					return;
				}else if(menu.residueQuantity<selCount+1){//个人限购量
					$.WapDialog.tip('超出促销个人限购量,你只能抢购'+menu.residueQuantity+"份");
	    			return;
				}
			}
			
			carManager.menuList[menuIndex].selCount +=1;
			if(selCount==0){
				$('#sum_'+menuId).show();
				$('#minus_'+menuId).show();
			}
			$('#item_total_'+menuId).text((carManager.menuList[menuIndex].selCount*menu.price).toFixed(2));
			$('#sum_'+menuId).val(carManager.menuList[menuIndex].selCount);
			carManager.reflesShopCart();
		}
	};
	
	//减少购物车菜品数
	this.minus = function(menuId){
		var menuIndex=carManager.getMenuIndex(menuId);
		if(carManager.menuList[menuIndex].selCount>=1){
			carManager.menuList[menuIndex].selCount -=1;
			$('#sum_'+menuId).val(carManager.menuList[menuIndex].selCount);
			$('#item_total_'+menuId).text((carManager.menuList[menuIndex].selCount*carManager.menuList[menuIndex].price).toFixed(2));
			if(carManager.menuList[menuIndex].selCount==0){
				$('#sum_'+menuId).val(0).hide();
				$('#minus_'+menuId).hide();
			}
			carManager.reflesShopCart();
		}
	};
	
	//根据菜品id获取菜品在购物的索要号
	this.getMenuIndex = function (menuId){
		var index=-1;
		$.each(carManager.menuList,function(k,v){
			if(v.menuId==menuId){
				index=k;
				return false;
			}
		});
		return index;
	};
	//刷新购物车金额的显示
	this.reflesShopCart=function(){
		var totalMoney=0.0;
		var count=0;
		$.each(carManager.menuList,function(k,v){
			totalMoney +=parseFloat(v.price) * parseInt(v.selCount);
			count +=v.selCount;
		});
		$('#totalMoney').text(totalMoney.toFixed(2));
		$('#totalNum').text(count);
		
		if(totalMoney-carManager.deliveryBegin<0 && $("input[name='saleType']:checked").val()==1){
			$('#submitOrder').addClass('ok_btn_disable').text('差'+ (carManager.deliveryBegin-totalMoney).toFixed(2) +'元起送');
		}else{
			$('#submitOrder').removeClass('ok_btn_disable').text('提交订单');
		}
	};
	//提交订单
	this.submitOrder = function(){
		if(carManager.submit)return;
		if(carManager.menuList.length==0){
        	$.WapDialog.tip('亲,您未选择购买任何菜品哦!');
        	return;
        }

        var params=[];
        var totalMoney=0.0;
        var saleType = $("input[name='saleType']:checked").val();
        
        $.each(carManager.menuList,function(k,v){
        	if(v.selCount>0){
        		params.push({menuId:v.menuId,num:v.selCount,menuPromotionId:v.menuPromotionId,salesPromotion:v.salesPromotion,dough:v.price});
        		totalMoney +=v.selCount*v.price;
        	}
        });
        
        if(params.length==0){
        	$.WapDialog.tip('亲,您还没选择购物任何菜品哦!');
        	return;
        }
        if(totalMoney<carManager.deliveryBegin && saleType==1){
			$.WapDialog.tip('亲,还差'+(carManager.deliveryBegin-totalMoney).toFixed(2)+'元我们才配送哦!');
        	return;
		}
        
		
        //如果是外卖，必须填写联系人信息
        var name = "";
        var phone = "";
        var address = "";
        var timeRemark = "";
        
        if(saleType == 1){//外卖表单验证
        	var flag=-1;
        	for(var i=0;i<carManager.requestsId.length;i++){
        		if(!$('#'+carManager.requestsId[i]).val()){
        			$('#'+carManager.requestsId[i]).siblings('.error_msg').text('必填');
        			if(flag==-1){
        				flag=i;
        			}
	        	}else{
	        		$('#'+carManager.requestsId[i]).siblings('.error_msg').text('');
	        	}
        	}
			
 			if(flag!=-1){
 				$('#'+carManager.requestsId[flag]).focus();
 				$.WapDialog.tip('请填好联系人信息');
        		return false;
        	}
        	name = $('#customerName').val();
        	phone = $('#phone').val();
        	address = $('#address').val();
        	timeRemark = $('#timeRemark').val();
        }
        
        carManager.submit=true;
        $.WapDialog.lock({content:'正在提交确认订单...'});
        
        //提交订单
        $.ajax({
            type: "post",
        	url:carManager.basePath+"/takeOutController.do?createOrder&time="+Math.round(new Date().getTime()/1000),
        	data:{
        		realname:name,
        		mobile:phone,
        		address:address,
        		timeRemark:timeRemark,
        		saleType:saleType,
        		params:JSON.stringify(params),
        		merchantId:carManager.merchantId,
        		title:(saleType==1?'外卖':'堂食')
        	},
        	dataType:'json',
        	success:function(d){
        		if(d.stateCode == "01"){
        			$.WapDialog.close();
        			$.WapDialog.tip("提交订单失败："+d.msg);
        			carManager.submit=false;
        		}else{
        			window.location.href = encodeURI(carManager.basePath+"/takeOutController.do?payOrder&orderid="+ d.obj.orderId +"&time=" +Math.round(new Date().getTime()/1000));
        		}
        	},
        	error:function(e){
        		$.WapDialog.close();
        		$.WapDialog.tip('提交订单失败，请重试');
        		carManager.submit=false;
        	}
        });
	};
	//显示时间选择
	this.selectTime = function (){
		$.WapDialog.open({
			title:'选择送餐时间',
			content:'<div id="timeListDiv">'+$('.timeListDiv').html()+'</div>',
			cancel:{show:true},
			ok:{name:'确定选择',callBack:function(){
				var time=$('#timeListDiv .sel_timeList').val();
				if(time){
					$('#timeRemark').val(time);
				}
			}},
			init:function(){
				var v=$('#timeRemark').val();
				if(v){
					$('#timeListDiv .sel_timeList').val(v);
				}else{
					$('#timeListDiv .sel_timeList  >option:first').attr("selected","selected");
				}
			}
		});
	};
}