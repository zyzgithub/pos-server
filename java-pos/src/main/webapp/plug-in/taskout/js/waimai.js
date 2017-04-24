/**
 * 店铺首页管理类
 * 作者：jusnli
 * 邮箱：545987886@qq.com
 * @returns
 */
function ShopCartManage(){
	this.submit=false;	//标识是否在提交
	this.basePath;		//根目录
	this.merchantId;	//店铺id
	this.workState=false;	//店铺上班状态：true表示开启,false表示休息中
	this.deliveryBegin=0.0;	//多少元起送
	this.typeList=[];		//菜单类型列表[{typeId:,typeName}]
	this.menuList=[];		//菜单列表数据[{菜品数据对象,selCount:选购数量},...]
	
	//初始化数据
	this.init= function(merchantId,deliveryBegin,basePath){
		shopCartManage.deliveryBegin=deliveryBegin;
		shopCartManage.merchantId=merchantId;
		shopCartManage.basePath=basePath;
		shopCartManage.checkMerchantState();//判断是否在营业中
		$('#showOrHideType').click(function(){
			shopCartManage.showOrHideType();
		});
	};
	//判断店铺是否在营业中
	this.checkMerchantState=function(){
		$.ajax({
    		type:"post",
    		url:shopCartManage.basePath+"/takeOutController.do?checkMerchantState&time=" + Math.round(new Date().getTime()/1000),
    		data:{merchantId:shopCartManage.merchantId},
    		dataType:'json',
    		success:function(data){
    			if(data.state=='false'){
    				$.WapDialog.open({
    					title:'温馨提示',
    					content:'店铺信息获取错误，是否返回首页',
    					cancel:{show:true},
    					ok:{
    						name:'确定',
    						callBack:function(){ 
    							window.location.href=encodeURI(shopCartManage.basePath+"/takeOutController.do?merchantList&time="+Math.round(new Date().getTime()/1000));
    						}
    					}
    				});
    			}else{
    				if(data.obj == "open"){
    			        $('#submitBtn').click(function(){
    			        	shopCartManage.gotoMyShopCar();
    			        });
    			        shopCartManage.workState=true;
        			}else{
        				$('.bottom_je').text("休息中");
        				shopCartManage.workState=false;
        			}
    			}
    		},
    		error:function(e){console.log(e);}
    	});
	};
	//加载菜单列表
	this.load=function(){
        $.ajax({
            type: "post",
            url: shopCartManage.basePath+"/takeOutController.do?getMenuList&time="+Math.round(new Date().getTime()/1000),
            data:{
            	merchantId:shopCartManage.merchantId
            },
            dataType:'json',
            success: function (data) {
                if (data.obj && data.obj.length>0) {
                	var typeList=data.obj;
                	$.each(typeList,function(k,v){
                		var type={typeId:v.typeId,typeName:v.typeName};
                		$('#m_type_ul').append('<li><a href="javascript:void(0);" id="a_type_id_'+v.typeId+'" onclick="shopCartManage.showType('+v.typeId+')">'+v.typeName+'</a></li>');
                		
                		shopCartManage.typeList.push(type);//存放菜品类型
                		
                		var menuDetail=v.menuDetail;
                		$.each(menuDetail,function(k2,v2){
                			 v2.typeId=v.typeId;
                			 if(v2.salesPromotion=='Y' && v2.residueQuantity>0 && v2.promotionQuantity>0){
                             	v2.salesPromotion ='Y';
                             	v2.price=v2.promotionMoney;
                             }else{
                            	v2.salesPromotion ='N';
                             }
                			 v2.selCount=0;
                			shopCartManage.menuList.push(v2);//存放菜品列表
                		});
                	});
                	var html='';
                	$.each(shopCartManage.menuList,function(k,v){
                		html += ' <div class="body_naem" title="menu_typeId_'+v.typeId+'" >';
                        html += '	<div class="menu_img_div"><img class="top_img" src="'+v.image+'" /></div>';
                        html += '  	<div class="menu_title_div">';
                        html += '		<span class="naem" >'+v.name+'</span>';
                        if(v.salesPromotion=='Y'){
                        	html += '	<em class="chu2" id="chu_'+v.menuId+'">促销</em>';
                        }
                        html += '		<div class="quantity">';
                        if(v.today_repertory==0){
                        	html += '		<span  class="rqtip" >已抢完</span>';
                        }else{
	                        html += '		<a href="javascript:void(0);" class="minus" id="minus_'+v.menuId+'" style="display:none;" onclick="shopCartManage.minus('+v.menuId+')" >　</a>';
	                        html += '		<input type="text" value="0" class="sum"  disabled="disabled" id="sum_'+v.menuId+'" style="display:none;" />';
	                        html += '		<a href="javascript:void(0)" class="add" onclick="shopCartManage.add('+v.menuId+')" >　</a>';
                        }				
                        html += '		</div>';
                        html +=	'	</div>';
                        html += '  <div class="body_naem_size">';
                        html += '	<div class="name">';
                        html += '		<p class="ver_name">'+v.typename+(v.today_repertory<10?' | 库存'+v.today_repertory+'份':'')+(v.buy_count>0?' | 已售'+v.buy_count+'份':'');
                        html += '			<span class="time">￥'+(v.salesPromotion=='Y'?v.promotionMoney:v.price)+'</span>';
                        html += '		</p>';
                        html += '	</div>';
                        
                        html += ' </div>';//body_naem_size结束
                        html += ' <div class="clear"></div>';
                        html += '</div>';
                	});
                	$("#list").append(html);
                	$('#msg').hide();
                }
            },
            error:function(e){
            	$('#msg').text('数据加载失败,点击重试').click(function(){
            		shopCartManage.load();
            	});
            }
        });
	};
	
	//添加到购物车菜品数
	this.add = function(menuId){
		var menuIndex=shopCartManage.getMenuIndex(menuId);
		var menu=shopCartManage.menuList[menuIndex];
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
			
			shopCartManage.menuList[menuIndex].selCount +=1;
			if(selCount==0){
				$('#sum_'+menuId).show();
				$('#minus_'+menuId).show();
			}
			$('#sum_'+menuId).val(shopCartManage.menuList[menuIndex].selCount);
			shopCartManage.reflesShopCart();
		}
	};
	
	//减少购物车菜品数
	this.minus = function(menuId){
		var menuIndex=shopCartManage.getMenuIndex(menuId);
		if(shopCartManage.menuList[menuIndex].selCount>=1){
			shopCartManage.menuList[menuIndex].selCount -=1;
			$('#sum_'+menuId).val(shopCartManage.menuList[menuIndex].selCount);
			
			if(shopCartManage.menuList[menuIndex].selCount==0){
				$('#sum_'+menuId).val(0).hide();
				$('#minus_'+menuId).hide();
			}
			shopCartManage.reflesShopCart();
		}
	};
	
	//根据菜品id获取菜品在购物的索要号
	this.getMenuIndex = function (menuId){
		var index=-1;
		$.each(shopCartManage.menuList,function(k,v){
			if(v.menuId==menuId){
				index=k;
				return false;
			}
		});
		return index;
	};
	//刷新购物车金额数量的显示
	this.reflesShopCart=function(){
		var money=0.0;
		var count=0;
		$.each(shopCartManage.menuList,function(k,v){
			money +=parseFloat(v.price) * parseInt(v.selCount);
			count +=v.selCount;
		});
		$('#sum').text(money.toFixed(2));
		if(count==0){
			$('#suz').text(count).hide();
		}else{
			$('#suz').text(count).show();
		}
	};
	
	//去结算(进入订单确认界面)
	this.gotoMyShopCar=function(){
		
		if(!shopCartManage.workState){
			$.WapDialog.tip('该店铺休息中,暂时不能点餐');
			return;
		}else if(shopCartManage.submit){
			return;
		}else{
			var selMenuList=[];
			var reg=new RegExp("　","g");
			$.each(shopCartManage.menuList,function(k,v){
				if(v.selCount>0){
					selMenuList.push({
						menuId:v.menuId,selCount:v.selCount,menuPromotionId:v.menuPromotionId,
						salesPromotion:v.salesPromotion,price:v.price,name:v.name.replace(reg,' '),typename:v.typename.replace(reg,' '),
						today_repertory:v.today_repertory,salesPromotion:v.salesPromotion,
						promotionQuantity:v.promotionQuantity,residueQuantity:v.residueQuantity
					});
				}
			});
			if(selMenuList.length==0){
				$.WapDialog.tip('您尚未选购任何菜品,快选购快餐吧');
				return;
			}else{
				$.WapDialog.lock({content:'正在提交数据...'});//锁屏
				shopCartManage.submit=true;
				window.location.href=shopCartManage.basePath+"/takeOutController.do?shoppingcar&merchantId="+shopCartManage.merchantId+"&params="+encodeURIComponent(JSON.stringify(selMenuList))+"&time="+Math.round(new Date().getTime()/1000);
				$.WapDialog.close();
				shopCartManage.submit=false;
			}
		}
	};
	
	//显示指定的类型
	this.showType = function(typeId){
		shopCartManage.showOrHideType();
		$("html,body").animate({scrollTop:$('[title=menu_typeId_'+typeId+']').offset().top-50},1000);
	};
	
	//显示或隐藏分类
	this.showOrHideType = function(){
    	if($('#m_type_div').hasClass('typeBtn')){
    		$('#m_type_l').show();
			if($('#m_type_l').height()>$(window).height()-150){
				$('#m_type_div').css({'top':'51px'});
			}
			$('#m_type_div').removeClass('typeBtn');
			$(this).addClass('m_type_s').find('span').text('收起');
    	}else{
    		$('#m_type_div').css({'top':''}).addClass('typeBtn');
			$('#m_type_l').hide();
			$(this).removeClass('m_type_s').find('span').text('分类');
    	}
    };
}