/**
 * 1号外卖地址列表管理类
 * 功能：通过定位到用户所在的经纬度，加载用户附近的大厦，以及大厦的楼层
 * */
function AddressesManager(){
	this.positionErrorMsg=["你的浏览器不支持获取地理位置信息","位置服务被拒绝","暂时获取不到位置信息","获取位置信息超时","未知错误"];//获取地理位置错误消息
	this.basePath='';	 //跟路径
	this.start=0;	 //开始行
	this.pageSize=20;//每次加载数量
	this.city="";//地址
	this.address = "";
	this.lng=0;//经度
	this.lat=0;//纬度
	this.loading=false;	//是否正在加载中
	this.group=0;	//是否正在加载中
	this.sort=0;	//是否正在加载中
	this.promote=0;	//是否正在加载中
	this.selectedBuildingName="";	//选中的大厦名字
	this.selectedBuildingId=0;	//选中的大厦id
	this.selectedBuildingFloor=null;	//选中的楼层
	
	this.init = function(options_){
		var defaults={//默认参数
				basePath:addressesManager.basePath,		//跟路径
				start:addressesManager.start,			//从第几行开始
				pageSize:addressesManager.pageSize,		//每次加载数量
			};
		var options=$.extend({},defaults,options_);
		
		addressesManager.basePath=options.basePath;
		addressesManager.second=options.second;
		addressesManager.pageSize=options.pageSize;
		addressesManager.start=options.start;
		//初始化地址
		//addressesManager.getInitPosition();
		//绑定控件事件
		//addressesManager.bindEvent(); 		
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
	        	 
	        	 addressesManager.lng=r.point.lng;//经度
	        	 addressesManager.lat=r.point.lat;//纬度
	        	 // 根据坐标获取当前地址信息
	 	         myGeocoder.getLocation(new BMap.Point(r.point.lng, r.point.lat), function(rs){
		 	         
		 	         var addComp = rs.addressComponents;
		 	         addressesManager.city = addComp.city;
		 	         addressesManager.address = addComp.street+addComp.streetNumber;
		 	         //$('#head_title1').html(addressesManager.address);
		 	         $.WapDialog.tip(addressesManager.address);
		 	         //加载数据
		 	         //addressesManager.load();
		 	         
		 	         $.WapDialog.close();
		 	      
		 	     }); 
	        }else {
	        	//获取坐标失败
	        	$.WapDialog.open({title:'温馨提示', content:'获取地理位置失败，点击确认重新获取', ok:{
	    				name:'确定',
	    				callBack:function(){ 
	    					addressesManager.getInitPosition();
	    				}
	    			}
	    		});
	        }
        });
	};
	
	//加载更多商家列表
	this.load= function(){
		//如果没有加载全部则继续加载,否则不加载
		if(addressesManager.start!=0 && addressesManager.start%addressesManager.pageSize<addressesManager.pageSize){
			$("#loading_div").text('已加载附近全部');
			return;
		}else if(addressesManager.loading){
			return;
		}
		
		$("#loading_div").text('正在加载中...');
		addressesManager.loading=true;
		var url = addressesManager.basePath+'/takeOutController/wxhome.do';
		var params = {
				start:addressesManager.start,//分页起始项
				rows:addressesManager.pageSize,//分页页数
				lng:addressesManager.lng,//经度
				lat:addressesManager.lat,//纬度
				city:addressesManager.city,
				address:addressesManager.address,
				group:addressesManager.group,
				sort:addressesManager.sort,
				promote:addressesManager.promote
			}
		$.post(url, params, function(data) {
			if(data && data.state=='success'){
				if(addressesManager.start==0){
					$("#merchantList").empty();
				}
				if(addressesManager.start==0 && data.obj.length==0){
					$("#loading_div").text('亲!附近暂无可选的外卖店铺');
				}else{
					$.each(data.obj,function(k,v){
						var html = '';
						html += '<li class="product_list clearfix">';
						html += '<a href="'+addressesManager.basePath+'/takeOutController/menu/'+v.merchantId+'.do">';
						html += '<div class="pic"><img src="'+v.logo+'"  alt="" /></div>';
						html += '<div class="product_info">';
						html += '<p class="shop_name">'+v.name;
						if(v.sale == 1)
							html += '<span class="sales"></span>';
						if(v.promotion == 1)
							html += '<span class="less"></span>';
						html += '</p>';
						html += '<p class="pingfen">';
						for(var i = 0; i < 5; i++) {
							if(i < v.score) 
								html += '<i class="active"></i>';
							else
								html += '<i></i>';
						}
						html += '</p>';
						html += '<div class="detail_info">';
						html += '<span>'+v.open+'&nbsp;|</span><span>&nbsp;'+v.send+'起配送&nbsp;|</span><span>&nbsp;'+v.type+'</span></div></div>';
						html += '<div class="sale_detail"><span class="order_sum">已售'+v.buyCount+'份</span>';
						html += '<p>'+v.distance+'米</p></div></a></li>';
						
						$("#merchantList").append(html);
					});
					addressesManager.start +=data.obj.length;
					$("#loading_div").text('点击加载更多');
				}
			}else{
				$("#loading_div").text('亲!附近暂无可选的外卖店铺');
			}
		}, "json");
		
		addressesManager.loading=false;
	};
	
	//绑定一开始时一些标签的事件
	this.bindEvent = function (){
		 //滚动到底部自动加载更多
		 $(window).scroll(function(e){
			if(($(this).scrollTop()+$(this).height())>=$("html").height()-30){
				addressesManager.load();
			}
		  });
		  //点击加载更多
		  $("#loading_div").click(function(){
			  addressesManager.load();
		  });
		  //重新定位
		  $('#head_title1').click(function(){
			  addressesManager.start=0;	 //开始行
			  addressesManager.pageSize=20;//每次加载数量
			  addressesManager.getInitPosition();
		  });
	};
	
	
	this.selectAddress = function(type, typeId, obj) {
		var load = false;
		//step 1 选中的样式替换
		$("."+type).each(function() {
			$($(this).children()).removeClass("selected");
		});
		$(obj).addClass("selected");
		//重新初始化分页数据
		addressesManager.start=0;	 //开始行
		addressesManager.pageSize=20;//每次加载数量
		
		//设置参数
		if(type == 'group') {
			if(addressesManager.group != typeId) {
				addressesManager.group = typeId;
				load = true;
			}
			addressesManager.clickSelect('all_sort', $(".all_sort"));
			
		}else if(type == 'sort') {
			if(addressesManager.sort != typeId) {
				addressesManager.sort = typeId;
				load = true;
			}
			addressesManager.clickSelect('sortby', $(".sortby"));
			
		}else if(type == 'promote') {
			if(addressesManager.promote != typeId) {
				addressesManager.promote = typeId;
				load = true;
			}
			addressesManager.clickSelect('promotion', $(".promotion"));
		}
		
		//加载数据
		if(load)
			addressesManager.load();
	};
	
	this.clickSelect = function(clickId, obj) {
		 $('#'+clickId).siblings().hide();
	     $('#'+clickId).toggle();
	     obj.css('color','#f00')  
	     if($('#'+clickId).is(':hidden')){
	        $('.shadow_div').hide();
	        obj.css('color','')  
	     }else{
	        $('.shadow_div').show();
	        obj.siblings().css('color','');
	     }   
	};

	//保存地址
	this.saveAddress = function(){
		var name = $(".lxrname").val();
		var sex = $(".selectGender > input:checked").val();
		var floor = addressesManager.selectedBuildingFloor;
		var buildPrimaryId = $("#buildPrimaryId").val();
		var buildName = addressesManager.selectedBuildingName;
		var mobile = $("#mobileId").val();
		var addressDetail = $("#addressDetailId").val();
		var addressPrimaryId = $("#addressPrimaryId").val();
		if( !name ){
			$.WapDialog.tip("请输入联系人");
			return ;
		}
		var p = /^1\d{10}$/;
		var t = /^0\d{2,3}-?\d{7,8}$/;
		if(mobile == null || mobile == '') {
			$.WapDialog.tip("联系电话不能为空");
			return;
		} else if(!p.test(mobile) && !t.test(mobile)) {
			$.WapDialog.tip("联系电话有误");
			return;
		}
		
		if( !buildName){
			$.WapDialog.tip("请选择送餐地址");
			return ;
		}
		if( !addressDetail){
			$.WapDialog.tip("请输入详细地址");
			return ;
		}
		$.WapDialog.lock({content:'正在保存地址...'});//锁屏
		//保存
		$.ajax({
			url: basePath+'/takeOutController.do?mineAddressesSave&time=' + Math.round(new Date().getTime()/1000),
			data:{
				name: name,
				sex: sex,
				floor: floor,
				buildPrimaryId: buildPrimaryId,
				buildName: buildName,
				mobile: mobile,
				addressDetail: addressDetail,
				addressId: addressPrimaryId
			},
			type: "POST",
			dataType:'json',
			contentType:"application/x-www-form-urlencoded; charset=utf-8",
			success: function(data){
				$.WapDialog.close();
				if(data.success == true){
					$.WapDialog.open({content:'保存成功,1秒后将自动返回地址列表',ok:{show:false},times:1000});
					setTimeout(function(){
						window.location.href = basePath+'/takeOutController.do?mineAddresses';
					},3000);
				}else{
					$.WapDialog.tip(data.msg);
				}
			},
			error:function(e){
				$.WapDialog.close();
				$.WapDialog.tip("操作失败,请重试");
			}
		});
	}
	
	//删除地址
	this.delAddress = function(){
		var addressPrimaryId = $("#addressPrimaryId").val();
		if( !addressPrimaryId ){
			$.WapDialog.tip("无法删除此地址，请确认此地址存在。");
			return ;
		}
		$.WapDialog.lock({content:'正在删除地址...'});//锁屏
		//删除地址
		$.ajax({
			url: basePath+'/takeOutController.do?mineAddressesDelete&time=' + Math.round(new Date().getTime()/1000),
			data:{
				addressId: addressPrimaryId
			},
			type: "POST",
			dataType:'json',
			contentType:"application/x-www-form-urlencoded; charset=utf-8",
			success: function(data){
				$.WapDialog.close();
				if(data.success == true){
					$.WapDialog.open({content:'删除成功,3秒后将自动返回地址列表',ok:{show:false},times:3000});
					setTimeout(function(){
						window.location.href = basePath+'/takeOutController.do?mineAddresses';
					},3000);
				}else{
					$.WapDialog.tip(data.msg);
				}
			},
			error:function(e){
				$.WapDialog.close();
				$.WapDialog.tip("操作失败,请重试");
			}
		});
	
	}
	
	//选楼层
	this.selectFloor = function(firstFloor, lastFloor){
		
		
		var f = firstFloor;
		var l = lastFloor;
		if(isNaN(f) || isNaN(l) || (f == 0 && l ==0)) {
			
			$("#buildPrimaryId").val(addressesManager.selectedBuildingId);
			$("#buildNameId").val(addressesManager.selectedBuildingName);
			
			//隐藏选楼层，隐藏选大厦
			$('.selectFloor').hide();
			$('.selectAddress').hide();
			
			$('.editBtn').show();
			$('.addBtn').show();
			return;
		}
		
		$('.floorlist').empty();
		var html = '';
		//循环输出大厦楼层html列表
		for(var i = firstFloor; i <= lastFloor ; i++){
			html += ' <li class="clickfloor" selFlr="' + i +'">' + i +'</li>';
		}
		$('.addresslist').hide();
		$('.floorlist').append(html);
		//绑定点击事件
		$(".clickfloor").click(function() {
			addressesManager.selectedBuildingFloor = $(this).attr("selFlr");	//选中的楼层
			//$(".xsFloor").val(addressesManager.selectedBuildingFloor+"楼");
			$("#buildPrimaryId").val(addressesManager.selectedBuildingId);
			$("#buildNameId").val(addressesManager.selectedBuildingName + addressesManager.selectedBuildingFloor + "楼");
			//隐藏选楼层，隐藏选大厦
			$('.selectFloor').hide();
			$('.selectAddress').hide();

			$('.editBtn').show();
			$('.addBtn').show();
		});
		$('.selectFloor').show();
	}
	
	//让用户选择大厦
	this.selectBuilding = function(data){
		//循环输出大厦html列表
		$.each(data,function(k,v){
			var html = '';
			html += '<li firstFloor="' +v.firstFloor + '" lastFloor="'+v.lastFloor + '" id="'+v.id + '" name="'+v.name +  '"';
			if(k == 0){
				html += ' class="active clickbuild"';
			} else {
				html += ' class="clickbuild"';
			}
			html += '> ';
			html += '<p>'+v.name;
			html += '</p>';
			html += '<span>'+v.address;
			html += '</span></li>';
			
			$(".searchAddress").append(html);
		});
		
		//绑定大厦点击事件
		$(".clickbuild").click(function() {
			var firstFloor = $(this).attr("firstFloor");
			var lastFloor = $(this).attr("lastFloor");
			var name = $(this).attr("name");
			var id = $(this).attr("id");

			addressesManager.selectedBuildingName=name;	//选中的大厦名字
			addressesManager.selectedBuildingId=id;	//选中的大厦id
			
			addressesManager.selectFloor(firstFloor, lastFloor);
		});
	}
}