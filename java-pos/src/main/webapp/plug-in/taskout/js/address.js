function AddressMng() {
	this.data = {};
	
	this.basePath;
	this.click=false;
	
	this.init = function(basePath) {
		address.basePath = basePath;
	};
	
	//新增地址
	this.add = function() {
		if(address.click)return;
		
		address.click = true;
		var p = /^1\d{10}$/;
		var t = /^0\d{2,3}-?\d{7,8}$/;
		
		var name = $("#atten").val();
		if(name == null || $.trim(name) == '') {
			$.WapDialog.tip("联系人不能为空");
			address.click = false;
			return;
		}
		var tel = $("#tel").val();
		if(tel == null || tel == '') {
			$.WapDialog.tip("联系电话不能为空");
			address.click = false;
			return;
		} else if(!p.test(tel) && !t.test(tel)) {
			$.WapDialog.tip("联系电话有误");
			address.click = false;
			return;
		}
		var addr = $("#addr").val();
		if(addr == null || addr == '') {
			$.WapDialog.tip("公司或门牌号不能为空");
			address.click = false;
			return;
		}
		var sex = $("input[name='sex']:checked").val();
		address.data.name = name;
		address.data.tel = tel;
		address.data.addr = addr;
		address.data.sex = sex;
		var url = address.basePath + '/address/add.do';
		$.post(url, address.data, function(data) {
			if(data.state == "success") {
				$(".tj_names").empty();
				var html = '';
				html += '<a href="javascript:void(0)" class="editAddr">';
				html += '<p><span>'+address.data.name+'</span><span>'+address.data.tel+'</span></p>';
				html += '<p>地址:';
				if(address.data.buildId && address.data.buildId > 0)
					html += address.data.buildName;
				if(address.data.floor && address.data.floor != 0)
					html += ' '+address.data.floor + '楼  ' 
				html += address.data.addr;
				html += '</p><i></i></a>';
				
				$(".tj_names").append(html);
				
				$(".writeAddress").hide();
				$(".editAddr").click(function() {
			    	   address.load();
			    });
				address.data = {};
				$('.xsAddress').val('');
		    	$("#atten").val('');
		    	$("#tel").val('');
		    	$("#addr").val('');
			} else {
				$.WapDialog.tip("提交地址失败");
			}
			address.click = false;
		}, "json");
	};
	
	//加载我的地址
	this.load = function() {
		
		if(address.click)return;
		
		address.click = true;
		
		$(".mine_gldz").empty();
		var url = address.basePath + '/address/list.do';
		$.get(url, function(data) {
			$('.enterAddress').show();
			if(data.state == "success") {
				var list = data.list;
				$.each(list, function(i, v) {
					var html = '';
					html += ' <div id="'+v.id+'" class="user_address';
					if(v.isDefault == "Y")
						html += ' onSelect_gl ';
					html +=' clearfix selMyAddr">';
					html +='<div class="address_info"><div class="users_info">';
					html +='<span>'+v.name+'</span>';
					html +='<span>'+v.mobile+'</span></div>';
					html +='<p>';
					if(v.buildingName)
						html += v.buildingName + ' ';
					if(v.buildingFloor)
						html += v.buildingFloor +'楼 ';
					html += v.addressDetail + '</p></div></div>';
					
					$(".mine_gldz").append(html);
				});
				
				$(".selMyAddr").click(function() {
					var addrId = $(this).attr("id");
					address.change(addrId);
				});
				
			} else {
				$.WapDialog.tip("没有收货地址");
			}
			address.click = false;
		}, "json")
	};
	
	//加载可选地址
	this.select = function() {
		if(address.click)return;
		
		var postData = {};
		
		if(isNaN(lng) || isNaN(lat) || lng == "") {
			$.WapDialog.lock({content:'正在定位地址...'});
    		// 使用百度API浏览器定位功能
    		var geolocation = new BMap.Geolocation();
    		// 获取当前位置坐标
    		geolocation.getCurrentPosition(function(r){	
    			//获取坐标成功
    	        if(this.getStatus() == BMAP_STATUS_SUCCESS ){
    	        	 postData.lng=r.point.lng;//经度
    	        	 postData.lat=r.point.lat;//纬度
    	        	 $.WapDialog.close();//解锁
    	        	 
    	        	 address.selectLoction(postData);
    	        }
            });
    		
		}else {
			address.selectLoction(postData);
		}
		
	};
	
	this.selectLoction = function(postData) {
		$.WapDialog.lock({content:'加载附近地址...'});
		address.click = true;
		var url = address.basePath + "/address/near.do";
		$(".searchAddress").empty();
		$.get(url, postData, function(data) {
			if(data.state == "success") {
				var b = data.building;
				
				var tHtml = '<li style="background:none; font-size:16px; color:#333; padding-left:0;"><p>请选择配送范围之内的地址</p></li>';
				$(".searchAddress").append(tHtml);
				
				$.each(b, function(i, v) {
					var html = '';
					html += '<li id="'+v.id+'_'+v.firstFloor+'_'+v.lastFloor+'" class="buildings">';
					html += '<p>'+v.name+'</p>';
					html += '<span>'+v.address+'</span></li>';
					
					$(".searchAddress").append(html);

				});
				
				$(".buildings").click(function() {
					address.clickBuilding($(this));
				});
			}
			
			$.WapDialog.close();//解锁
		}, "json");
		//隐藏操作按钮
		$('.addBtn').hide();
		//隐藏楼层选择
		$('.selectFloor').hide();
		//显示大楼
		$('.selectAddress').show();
        $('.selectAddress').children('.searchAddress').show();
        
        address.click = false;
	}
	
	/* 点击区域大厦出现 */
	this.clickBuilding = function(obj) {
		if(address.click)return;
		address.click = true;
		$("#showFloor").empty();
		$(".searchAddress").empty();
		$('.searchAddress').hide();
		var arr = obj.attr("id").split("_");
		var t = "";
        
		address.data.buildId = arr[0];
		var flag = false;
        if(!isNaN(arr[1]) && !isNaN(arr[2])) {
        	if(arr[1] != 0 || arr[2] !=0)
        		flag = true;
        }
		
		if(0 == arr[0]) {
			t = $(obj.children()[0]).text();
			$('.selectAddress').hide();
			$('.addBtn').show();
			
       	} else if(flag) {
       		//判断是否有楼层
       		t = $(obj.children()[0]).text();
       		
        	var h = '';
        	for(var i = arr[1]; i <= arr[2]; i++) {
        		if(i != 0)
        			h += '<li class="floors">'+i+'</li>';
        	}
        	
        	if(h != '') {
        		$("#showFloor").append(h);
        		$('.selectFloor').show();
        		
        		$(".floors").click(function() {
        			address.clickFloors($(this));
        		});
        	} else {
        		$('.selectAddress').hide();
        		$('.addBtn').show();
        	}
        } else {
        	t = $(obj.children()[0]).text();
        	$('.selectAddress').hide();
			$('.addBtn').show();
        }
		
		
		address.data.buildName = t;
		$('.xsAddress').val(address.data.buildName);
		address.click = false;
	};
	
	/* 点击楼层 */
	this.clickFloors = function(obj) {
		if(address.click)return;
		
		address.click = true;
		
		var tt = obj.text();
		address.data.floor = tt;
		
		$('.xsAddress').val(address.data.buildName + address.data.floor + "楼");
		
		$('.selectAddress').hide();
		$('.selectFloor').hide();
        $('.addBtn').show();
        
        $("#showFloor").empty();
        
        address.click = false;
	};
	
	this.change = function(addressId) {
		if(address.click)return;
		
		address.click = true;
		
		var url = address.basePath + '/address/change.do';
		$.post(url, {"addressId":addressId}, function(data) {
			if(data.state == "success") {
				var addr = data.addr;
				$(".editAddr").empty();
				
				var html = '';
				html += '<p><span>'+addr.name+'</span><span>'+addr.mobile+'</span></p>';
				html += '<p>地址:';
				if(addr.buildingName)
					html += addr.buildingName;
				if(addr.buildingFloor)
					html += addr.buildingFloor + '楼  ' ;
				
				html += addr.addressDetail;
				html += '</p><i></i>';
				$(".editAddr").append(html);
				
				$('.enterAddress').hide();
			}
		}, "json");
		address.click = false;
	};
}