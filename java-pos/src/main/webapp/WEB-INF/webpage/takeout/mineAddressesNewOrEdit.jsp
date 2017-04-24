<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="./common.jsp" %>
	<title>${baseTitle }</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" name="viewport">
	<meta content="private,must-revalidate" http-equiv="Cache-Control">
	<meta content="telephone=no, address=no" name="format-detection">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<script src="http://api.map.baidu.com/api?v=2.0&ak=U8yYl7OgxAoAiDxvGZkG4wFk" type="text/javascript"></script>
	<script src="${basePath}/plug-in/taskout/js/Util.js?version=${webVersion}" type="text/javascript"></script>
	<script src="${basePath}/plug-in/taskout/js/addresseslist.js?version=${webVersion}" type="text/javascript"></script>

    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
</head>

<body>
<div class="wrapper">   
  <div class="index_head">
    <div class="common_bar">
        <a href="#" class="back"></a>
        <p>
        	<c:choose>
  				<c:when test="${empty addressEntity.id }">新增地址</c:when>
  				<c:otherwise>修改地址</c:otherwise>
  			</c:choose>
        </p>
    </div>    
  </div>
    <div class="add_users">
      <div>
        <span>联系人&nbsp;&nbsp;</span>
        <div>
	        <input type="text" placeholder="您的姓名" class="lxrname" id="nameId" value="${addressEntity.name}"/>
	        <p class="selectGender">
				<input type="radio" checked="checked" name="sex" value="m" class="radioclass on">男<input type="radio" name="sex" value="f" class="radioclass">女
	        </p>
        </div>
      </div> 
      	<div>
      		<span>联系电话</span>
      		<div><input id="mobileId" type="text" placeholder="电话" value="${addressEntity.mobile}"/></div>
      	</div> 
      <div>
        <span>配送范围</span>
        <div class="scAddress">
          <i></i>
          <input type="hidden" id="buildPrimaryId" value="${addressEntity.buildingId}"/>  
          <input type="hidden" id="addressPrimaryId" value="${addressEntity.id}"/>  
          
		<input type="text" id="buildNameId" placeholder="请选择" readonly="readonly" class="words xsAddress"  />
          <input type="hidden" readonly="readonly" class="xsFloor" />
          
        </div>
      </div>
      	<div style="border-bottom:0;">
      		<span>详细地址</span>
      		<div><input id="addressDetailId" type="text" placeholder="地址" value="${addressEntity.addressDetail}"/></div>
      	</div>  
    </div>
    <div class="selectAddress">
      <div class="addresslist">
      	  <p>请选择配送范围之内的地址</p>
	 	  <ul class="searchAddress">
	          <li class="active">
	              <p>[当前]蓝色康园</p>
	              <span>广州海珠区滨江东路556号</span>
	          </li>
	          <li>
	              <p>蓝色康园</p>
	              <span>广州海珠区滨江东路556号</span>
	          </li>
	          <li>
	              <p>蓝色康园</p>
	              <span>广州海珠区滨江东路556号</span>
	          </li>          
	      </ul>     	
      </div>
      
      <div class="selectFloor">
      	<p>请选择楼层</p>
        <ul class="clearfix floorlist">
            <li>1</li>
            <li>2</li>
            <li>3</li>
        </ul>
      </div>          
    </div>
    <c:choose>
		<c:when test="${empty addressEntity.id }">
			<div class="addBtn clearfix" >
		       <a href="#" class="addressSave" id="saveAddressId">保存</a>
		       <a href="#" class="addressCancle" >取消</a>
		    </div>    
		</c:when>
		<c:otherwise>
			<div class="editBtn clearfix" >
		       <a href="#" class="editSave" id="saveAddressId">保存</a>
		       <a href="#" class="editDel" id="delAddressId">删除</a>
		    </div>  
		</c:otherwise>
	</c:choose>
   	
    
</div> 
<script type="text/javascript">
	//初始化数据
	var basePath="${basePath}";
	var buildingList = ${buildingList};
		//初始化数据
	var	addressesManager=new AddressesManager();
		addressesManager.init({basePath:"${basePath}"});
	$(function(){
		
		$('.radioclass').click(function(event) {
		  /* 点击按钮新增我的地址背景图的切换 */
		   $(this).addClass('on');
		   $(this).siblings().removeClass('on');
		});
        $('.scAddress').click(function(event) {
          /* 点击送餐地址区域出现  */
         
          $('.searchAddress').empty();
          addressesManager.selectBuilding(buildingList);
           $('.selectAddress').show();    
          
          $('.addresslist').show();   /* 改  */
          $('.searchAddress').show();
          $('.selectFloor').hide();
          $('.editBtn').hide();
          $('.addBtn').hide();
          
        });
		$(".back").click(function(){
			window.location.href=basePath+"/takeOutController.do?mineAddresses";
		});
		/* 保存地址 */
		$("#saveAddressId").click(function(){	addressesManager.saveAddress();	});

		/* 删除地址 */
		$("#delAddressId").click(function(){	addressesManager.delAddress();	});
	});
	//如果是编辑的话，显示保存及删除按钮
	var addressIdValue = "${addressEntity.id}";
	if(addressIdValue){
		$('.editBtn').show();
	}
	//如果是编辑的话，显示用户已经选择的性别
	var addressSexValue = "${addressEntity.sex}";
	if(addressSexValue){
		var $gender = $('.selectGender > input');
		$gender.removeClass("on").removeAttr("checked");
		if($gender[0].value == addressSexValue){
			$($gender[0]).addClass("on").attr("checked", "checked");
		}
		if($gender[1].value == addressSexValue){
			$($gender[1]).addClass("on").attr("checked", "checked");
		}
	}

	var buildingNameValue = "${addressEntity.buildingName}";
	if(buildingNameValue){
		//console.log(addressesManager);
		addressesManager.selectedBuildingName = "${addressEntity.buildingName}";
		addressesManager.selectedBuildingFloor = "${addressEntity.buildingFloor}";
		if(buildingNameValue == "其他地址"){
			$("#buildNameId").val(buildingNameValue);
		}else{
			if(addressesManager.selectedBuildingFloor){
				$("#buildNameId").val(buildingNameValue + addressesManager.selectedBuildingFloor + '楼');
			}else{
				$("#buildNameId").val(buildingNameValue);
			}
		}
	}
</script>
</body>

</html>