/**
 * 菜品管理类
 * 作者：jusnli
 * 邮箱：545987886@qq.com
 * @returns
 */
function MenuManage(){
	this.addclick=false;
	this.submit=false;	//标识是否在提交
	this.basePath;		//根目录
	this.workState=false;	//店铺上班状态：true表示开启,false表示休息中
	this.shopcart = new Array();
	this.totalCount = 0;
	this.totalPrice = 0;
	this.merchantId;
	this.commentStart = 0;
	this.commentRows = 20;
	this.deliveryBegin;
	//初始化数据
	this.init= function(basePath, merchantId, deliveryBegin){
		menuManage.basePath=basePath;
		menuManage.merchantId=merchantId;
		menuManage.deliveryBegin=deliveryBegin*100;
	};

	//添加到购物车菜品数
	this.add = function(menuId, menuName, num, price, repertory, promoting, limitCount) {
    	if(menuManage.addclick)return;
    	
    	menuManage.addclick = true;
		var co = $("#count"+menuId);
    	var count = parseInt(co.val()) + num;
    	//判断是否超出限购数量
    	if(promoting) {
    		if(limitCount == 0) {
    			$.WapDialog.tip("您今天可享受优惠份额已用完");
    			menuManage.addclick = false;
    			return parseInt(co.val());
    		}
    		
    		if(count > limitCount) {
    			$.WapDialog.tip("您今天还能享受优惠"+co.val()+"份");
    			menuManage.addclick = false;
    			return parseInt(co.val());
    		}
    	}
    	
    	//step 1 判断选购数量，不能为负数
    	if(count < 0) {
    		menuManage.addclick = false;
    		return 0;
    	}
    	
    	//step 2 判断选购数量是否超出库存
    	if(count > repertory) {
    		menuManage.addclick = false;
    		return repertory;
    	}
    	
    	//step 3 加入购物车
    	co.val(count);
    	if(count >= 1) {
    		$("#minus"+menuId).show();
    		co.show();
    	} else {
    		$("#minus"+menuId).hide();
    		co.hide();
    	}
    	
    	menuManage.reflesShopCart(num, price);
    	menuManage.reflesShopCartList(menuId, menuName, price, count, repertory, promoting, limitCount);
    	
    	menuManage.addclick = false;
    	return count;
    };

    //清空购物车
    this.cleanCartshop = function() {
    	$("#cartshopList").empty();
    	//step 1 清空菜单列表中选项
    	$.each(menuManage.shopcart, function(i, v) {
    		var menuId = v.menuId;
    		$("#minus"+menuId).hide();
    		$("#count"+menuId).hide();
    		$("#count"+menuId).val(0);
    	});
    	
    	menuManage.shopcart = new Array();
    	//step 2 清空购物车概况
    	menuManage.totalCount = 0;
		menuManage.totalPrice = 0;
		$("#cartCount").text(menuManage.totalCount);
		$("#cartPrice").text('￥'+(menuManage.totalPrice/100).toFixed(2));
		
		$('.shadow_div_2').hide();
        $('.gwc_click').hide();
        $("#cartCount").hide();
    };
    
    //购物车列表
    this.listCartshop = function() {
    	$("#cartshopList").empty();
    	$.each(menuManage.shopcart, function(i, v){
    		if(v.count > 0) {
    			var html = '<div class="food-add clearfix tr'+v.menuId+'">';
    			html += '<p class="words">'+v.menuName+'</p>';
    			html += '<div class="item-add"><button class="gwc_minus -minus sub  m'+v.menuId+'" id="'+v.menuId+'"></button>';
    			html += '<input type="text" class="gwc-count item-count c'+v.menuId+'"  readonly="readonly" value="'+v.count+'" />';
    			html += '<button class="-plus add p'+v.menuId+'" id="'+v.menuId+'"></button></div>';
    			html += '</div>';
    			$("#cartshopList").append(html);
    		}
    	});
    	
    	$(".sub").click(function() {
    		var menuId = $(this).attr('id');
    		var menu = menuManage.getShopCartById(menuId);
    		var count = menuManage.add(menu.menuId, menu.menuName, -1, menu.price, menu.repertory, menu.promoting, menu.limitCount);
    		if(count == 0) {
    			$(".tr"+menuId).remove();
    		} else {
    			$(".c"+menuId).val(count);
    		}
    		
    	});
    	
    	$(".add").click(function() {
    		var menuId = $(this).attr('id');
    		var menu = menuManage.getShopCartById(menuId);
    		var count = menuManage.add(menu.menuId, menu.menuName, 1, menu.price, menu.repertory, menu.promoting, menu.limitCount);
    		$(".c"+menuId).val(count);
    	});
    	
    };
   
    this.getShopCartById = function(menuId) {
    	var singleCart;
    	$.each(menuManage.shopcart, function(i, v) {
    		if(v.menuId == menuId) {
    			singleCart = v;
    			return false;
    		}
    	});
    	return singleCart;
    };
    
    //刷新购物车
    this.reflesShopCartList=function(menuId, menuName, price, count, repertory, promoting, limitCount) {
    	var isExist = false;
    	var singleCart;
    	$.each(menuManage.shopcart, function(i, v) {
    		if(v.menuId == menuId) {
    			singleCart = v;
    			isExist = true;
    			return false;
    		}
    	});
    	
    	if(!isExist) {
    		singleCart = {};
    		singleCart.menuId = menuId;
    		singleCart.menuName = menuName;
    		singleCart.price = price;
    		singleCart.count = 0;
    		singleCart.repertory = repertory;
    		singleCart.promoting = promoting;
    		singleCart.limitCount = limitCount;
    		
    		menuManage.shopcart.push(singleCart);
    	}
    	
    	singleCart.count = count;
    	
    };
	
	//刷新购物车金额数量的显示
	this.reflesShopCart=function(count, price){
		var cartCount = $("#cartCount");
		var cartPrice = $("#cartPrice");
		menuManage.totalCount += count;
		menuManage.totalPrice += price*100*count;
		cartCount.text(menuManage.totalCount);
		cartPrice.text('￥'+(menuManage.totalPrice/100).toFixed(2));
		
		if(menuManage.totalPrice >= menuManage.deliveryBegin) {
			$(".selectOK").removeClass('cb-disable');
			$(".selectOK").addClass('xhl');
			$(".selectOK").text("选好了");
		} else {
			$(".selectOK").removeClass('xhl');
			$(".selectOK").addClass('cb-disable');
			$(".selectOK").text("差￥"+((menuManage.deliveryBegin-menuManage.totalPrice)/100).toFixed(2)+"起送");
		}
		
		if(menuManage.totalCount >=1) {
			cartCount.show();
		}else {
			cartCount.hide();
		}
	};
	
	//去结算(进入订单确认界面)
	this.gotoMyShopCar=function(){
		
		if(!menuManage.workState){
			$.WapDialog.tip('该店铺休息中,暂时不能点餐');
			return;
		}else if(menuManage.submit){
			return;
		}else if(menuManage.menu.selCount<=0){
			$.WapDialog.tip('至少购买一份才能结算哦！');
			return;
		}else{
			var selMenuList=[];
			var v=menuManage.menu;
			var reg=new RegExp("　","g");
			selMenuList.push({
				menuId:v.menuId,selCount:v.selCount,menuPromotionId:v.menuPromotionId,
				salesPromotion:v.salesPromotion,price:v.price,name:v.name.replace(reg,' '),typename:v.typename.replace(reg,' '),
				today_repertory:v.today_repertory,salesPromotion:v.salesPromotion,
				promotionQuantity:v.promotionQuantity,residueQuantity:v.residueQuantity
			});
			$.WapDialog.lock({content:'正在提交数据...'});//锁屏
			menuManage.submit=true;
			window.location.href=menuManage.basePath+"/takeOutController.do?shoppingcar&merchantId="+menuManage.menu.merchant_id+"&params="+encodeURIComponent(JSON.stringify(selMenuList))+"&time="+Math.round(new Date().getTime()/1000);
			$.WapDialog.close();
			menuManage.submit=false;
		}
	};
	//没知道菜品时显示
	this.notMenu = function(){
		$.WapDialog.open({
			content:'亲，你要找的菜品已经下架或未上架哦!'
			,ok:{name:'去逛逛',callBack:function(){
				window.location.href=menuManage.basePath+"/takeOutController.do?merchantList";
			}}
		});
	};
	
	this.listComment = function() {
		if(menuManage.addclick)return;
		
		menuManage.addclick = true;
		
		if(menuManage.commentStart != 0 && menuManage.commentStart%menuManage.commentRows != 0) {
			$("#loading_div").text('已加载全部');
			return;
		}
		$("#loading_div").text('正在加载中...');	
		
		var url = menuManage.basePath + "/takeOutController/comments/"+menuManage.merchantId+".do";
		$.get(url, {"start":menuManage.commentStart, "rows":menuManage.commentRows}, function(data) {
			if(data && data.state == "success") {
				var comments = data.comments;
				$.each(comments, function(i, v) {
					var html = '';
						html += '<li class="clearfix"><div><img src="'+v.headIcon+'" alt="" /></div>';
						html += '<div><p  class="words">'+v.userName+'</p><div>';
						for(var j = 1; j <= 5; j++) {
							if(j <= v.score)
								html += '<i class="active"></i>';
							else
								html += '<i></i>';
						}
						html += '<span>'+v.score+'分</span>';
						html += '</div><p>'+v.content+'</p></div>';
						html += '<p class="evaluate-times"><span>'+v.time+'</span></p></li>';
						
					$(".comment-list-all").append(html);
				});
				
				menuManage.commentStart += comments.length;
			}
		}, "json");
		
		$("#loading_div").text('点击加载更多');
		menuManage.addclick = false;
	};
	
	this.checkShopcart = function() {
		var cart = new Array();
		$.each(menuManage.shopcart, function(i, v){
			if(v.count > 0) 
				cart.push(v);
		});
		if(cart.length == 0) {
			$.WapDialog.tip('您还没选购商品！');
			return;
		}
		if(menuManage.totalPrice < menuManage.deliveryBegin) {
			$.WapDialog.tip('外卖起送价为:'+menuManage.deliveryBegin);
			return;
		}
		var url = menuManage.basePath + "/takeOutController/checkShopcart.do";
		var json = JSON.stringify(cart);
		$.post(url, {"json":json, "merchantId":menuManage.merchantId}, function(data) {
			if(data.state == "success") {
				$("#shopcartJson").val(json);
				$("#addShopcart").submit();
			} else if(data.state == "fail") {
				$.WapDialog.tip(data.reason);
			}
		}, "json").error(function() {
			$.WapDialog.tip('请稍后重试！');
		});
	};

}