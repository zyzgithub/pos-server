/**
 * 1号外卖首页管理类
 * 作者:lfq
 * 邮箱:545987886@qq.com
 * */
function MerchantManager(){
	this.positionErrorMsg=["你的浏览器不支持获取地理位置信息","位置服务被拒绝","暂时获取不到位置信息","获取位置信息超时","未知错误"];//获取地理位置错误消息
	this.basePath='';	 //跟路径
	this.start=0;	 //开始行
	this.pageSize=50;//每次加载数量
	this.city="";//地址
	this.address = "";
	this.lng=0;//经度
	this.lat=0;//纬度
	this.loading=false;	//是否正在加载中
	this.group=0;	
	this.sort=0;	
	this.promote=0;	
	this.page = 1;
	this.rows = 10;
	
	this.init = function(options_){
		var defaults={//默认参数
				basePath:merchantManager.basePath,		//跟路径
				start:merchantManager.start,			//从第几行开始
				pageSize:merchantManager.pageSize,		//每次加载数量
			};
		var options=$.extend({},defaults,options_);
		
		merchantManager.basePath=options.basePath;
		merchantManager.pageSize=options.pageSize;
		merchantManager.start=options.start;
		//初始化地址
		merchantManager.getInitPosition();
		//绑定控件事件
		merchantManager.bindEvent(); 		
	};
	
	//通过百度API获取位置信息
	this.getInitPosition=function(){
		
		$.WapDialog.lock({content:'定位当前位置...'});
		// 使用百度API浏览器定位功能
		var geolocation = new BMap.Geolocation();
		// 获取当前位置坐标
		geolocation.getCurrentPosition(function(r){	
			//获取坐标成功
	        if(this.getStatus() == BMAP_STATUS_SUCCESS ){
	        	
	        	 var myGeocoder = new BMap.Geocoder();
	        	 
	        	 merchantManager.lng=r.point.lng;//经度
	        	 merchantManager.lat=r.point.lat;//纬度
	        	 // 根据坐标获取当前地址信息
	 	         myGeocoder.getLocation(new BMap.Point(r.point.lng, r.point.lat), function(rs){
		 	         //当前地址（省、市、区、街道、门牌号）
		 	         var addComp = rs.addressComponents;
		 	         var surrounding = rs.surroundingPois;
		 	         
		 	         merchantManager.city = addComp.city;
		 	         
		 	         if(surrounding && surrounding.length > 0) {
		 	        	var surround = surrounding[0];
		 	        	merchantManager.address = surround.title;
		 	         } else {
		 	        	 merchantManager.address = addComp.street+addComp.streetNumber;
		 	         }
		 	         
		 	         
		 	         $('#head_title1').html(merchantManager.address);
		 	         //加载数据
		 	         merchantManager.load();
		 	         
		 	         $.WapDialog.close();
		 	      
		 	     }); 
	        }else {
	        	//获取坐标失败
	        	$.WapDialog.close();
	        	$.WapDialog.tip('获取地理位置失败,请确认是否已打开定位服务');
	        	
	        }
        });
	};
	
	//加载更多商家列表
	this.load= function(){
		//如果没有加载全部则继续加载,否则不加载
		if(merchantManager.start!=0 && merchantManager.start%merchantManager.pageSize<merchantManager.pageSize){
			$("#loading_div").text('已加载附近全部');
			return;
		}else if(merchantManager.loading){
			return;
		}
		
		$("#loading_div").text('正在加载中...');
		merchantManager.loading=true;
		var url = merchantManager.basePath+'/takeOutController/wxhome.do';
		var params = {
				start:merchantManager.start,//分页起始项
				rows:merchantManager.pageSize,//分页页数
				lng:merchantManager.lng,//经度
				lat:merchantManager.lat,//纬度
				city:merchantManager.city,
				address:merchantManager.address,
				group:merchantManager.group,
				sort:merchantManager.sort,
				promote:merchantManager.promote
			}
		merchantManager.postLoadMerchantDataAndDisplay(url, params, "亲!附近暂无可选的外卖店铺");
		merchantManager.loading=false;
	};
	

	//加载用户收藏的商家列表
	this.loadUserFavMerchants = function(){
		$("#loading_div").text('正在加载中...');
		merchantManager.loading=true;
		var url = merchantManager.basePath+'/wxfavoritesController/wxuserFavMerchants.do';
		var params = {
				page: merchantManager.page,
				rows: merchantManager.rows
		}
		merchantManager.postLoadMerchantDataAndDisplay(url, params, "亲!没有收藏的外卖店铺哦", "page");
		merchantManager.loading=false;
	};
	
	//从特定的url中加载数据，然后输出html进行显示
	this.postLoadMerchantDataAndDisplay = function(url, params, noMerchantTipMsg, isPageOrStart){
		$.post(url, params, function(data) {
			if(data && data.state=='success'){
				var size = $("#merchantList > li").size();
				if(size == 0 && data.obj.length == 0){
					$("#loading_div").text(noMerchantTipMsg);
				} else if(data.obj.length == 0){
					$("#loading_div").text('已加载全部');
				} else{
					if("page" == isPageOrStart){
						merchantManager.page ++;
					}else{
						if(merchantManager.start == 0){
							$("#merchantList").empty();
						}
						merchantManager.start += data.obj.length;
					}
					$.each(data.obj,function(k,v){
						var html = '';
						html += '<li class="product_list clearfix">';
						html += '<a href="'+merchantManager.basePath+'/takeOutController/menu/'+v.merchantId+'.do">';
						html += '<div class="pic"><img src="'+v.logo+'"  alt="" /></div>';
						html += '<div class="product_info">';
						html += '<p class="shop_name">'+v.name;
						if(v.sale == 1)
							html += '<span class="sales"></span>';
						if(v.promotion == 1)
							html += '<span class="less"></span>';
						html += '</p>';
						html += '<p class="pingfen">';
						for(var i = 1; i <= 5; i++) {
							if(i <= v.score) 
								html += '<i class="active"></i>';
							else if(i-1 < v.score)
								html += '<i class="half_active"></i>';
							else
								html += '<i></i>';
						}
						html += '</p>';
						html += '<div class="detail_info">';
						html += '<span>'+v.open+'&nbsp;|</span><span>&nbsp;'+v.deliveryPrice+'元起配送&nbsp;|</span><span>&nbsp;'+v.type+'</span></div></div>';
						html += '<div class="sale_detail"><span class="order_sum">已售'+v.buyCount+'份</span>';
						html += '<p>'+v.distance+'米</p></div></a></li>';
						
						$("#merchantList").append(html);
					});
					$("#loading_div").text('点击加载更多');
				}
			}else{
				$("#loading_div").text(noMerchantTipMsg);
			}
		}, "json");
	};
	
	//绑定一开始时一些标签的事件
	this.bindEvent = function (){
		 //滚动到底部自动加载更多
		 $(window).scroll(function(e){
			if(($(this).scrollTop()+$(this).height())>=$("html").height()-30){
				merchantManager.load();
			}
		  });
	};
	
	//获取店铺分类
	this.getMerchantGroup = function() {
		var url = merchantManager.basePath+"/categoryController/group.do";
		$.get(url, function(data) {
			if(data.state == "success") {
				$.each(data.obj,function(k,v){
					var html = '';
					html += '<li class="filter-item ">';
					html += '<a href="#"><i></i><span>'+v.name+'</span></a>';
					html += '</li>';
					
					$("#all_sort").append(html);
				});
			}
		}, "json");
	};
	
	this.selectMerchant = function(type, typeId, obj) {
		var load = false;
		//step 1 选中的样式替换
		$("."+type).each(function() {
			$($(this).children()).removeClass("selected");
		});
		$(obj).addClass("selected");
		//重新初始化分页数据
		merchantManager.start=0;	 //开始行
		merchantManager.pageSize=20;//每次加载数量
		
		//设置参数
		if(type == 'group') {
			if(merchantManager.group != typeId) {
				merchantManager.group = typeId;
				load = true;
			}
			merchantManager.clickSelect('all_sort', $(".all_sort"));
			
		}else if(type == 'sort') {
			if(merchantManager.sort != typeId) {
				merchantManager.sort = typeId;
				load = true;
			}
			merchantManager.clickSelect('sortby', $(".sortby"));
			
		}else if(type == 'promote') {
			if(merchantManager.promote != typeId) {
				merchantManager.promote = typeId;
				load = true;
			}
			merchantManager.clickSelect('promotion', $(".promotion"));
		}
		
		//加载数据
		if(load) {
			$("#merchantList").empty();
			merchantManager.load();
		}
	};
	
	this.clickSelect = function(clickId, obj) {
		 $('.filter-container').show();
		 $('#'+clickId).siblings().hide();
	     $('#'+clickId).toggle();
	     obj.addClass('selected');
	     obj.siblings().removeClass('selected'); 
	     if($('#'+clickId).is(':hidden')){
	        $('.shadow_div').hide();
	        obj.removeClass('selected');    
	     }else{
	        $('.shadow_div').show();
	        obj.siblings().css('color','');
	     }   
	};
}