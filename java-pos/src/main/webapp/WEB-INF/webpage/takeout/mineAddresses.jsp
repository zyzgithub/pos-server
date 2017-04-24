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
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/common.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/index.css?version=${webVersion}" />
    <link rel="stylesheet" type="text/css" href="${basePath}/plug-in/wxIndex/css/mine.css?version=${webVersion}" />
	
	
</head>

<body style="background:#ececec;">
<div class="wrapper"> 
  <div class="index_head">
    <div class="common_bar">
        <a href="#" class="back"></a><p>管理我的地址</p><a href="#" class="addAddress_btn"></a>
    </div>    
  </div>
  
  <div class="mine_gldz">
	  <c:forEach items="${addressList}" var="add" varStatus="status">
		  	<div class="user_address 
			  	<c:if test="${status.first }">
			  	</c:if>
			  	<c:if test="${add.isDefault == 'Y' }">
			  		 onSelect_gl 
			  	</c:if>
			  	clearfix">
			  	<div class="address_info">
		            <div class="users_info">
		                <span>${add.name}</span>
		                <span>${add.mobile}</span>
		            </div>
		            <p> <c:if test="${not empty add.buildingName}" >${add.buildingName}&nbsp;</c:if>
	            		<c:if test="${not empty add.buildingFloor}" >${add.buildingFloor}楼&nbsp;</c:if>
	            		${add.addressDetail}</p>
		        </div>
		        <a href="#" class="address_edit" addressId="${add.id}">
		        	<input id="addressPrimaryId" type="hidden" value="${add.id}"  isdefault ="${add.isDefault }" >
		        </a>
		  	</div>
		</c:forEach>
	</div>
</div>        

<script type="text/javascript">
	var basePath="${basePath}";
	function changeDefaultAddress(addressId){
		if(addressId){
			
			var defAddrId =  $(".mine_gldz #addressPrimaryId[isdefault='Y']").val()
			//console.log(defAddrId);
			if( defAddrId == addressId){
				$.WapDialog.tip("此地址已经是默认地址。");
				return;
			}else{
				var url = basePath + '/address/change.do';
				$.post(url, {"addressId":addressId}, function(data) {
					if(data.state == "success") {
						$.WapDialog.tip("设置默认地址成功。");
						$(".mine_gldz #addressPrimaryId").each(function(){
							var $this = $(this);
							if(this.value == defAddrId){
								$this.attr("isdefault", "N");
							}
							if(this.value == addressId){
								$this.attr("isdefault", "Y");
							}
						});
					}
				}, "json");
			}
		}
	}
	
	$(function(){
		$(".back").click(function(){
			window.location.href=basePath+"/takeOutController.do?mine";
		});
		$(".addAddress_btn").click(function(){
			window.location.href=basePath+"/takeOutController.do?mineAddressesNewOrEdit";
		});
		$(".address_edit").click(function(index, value){
			var addressId = $(this).attr("addressId");
			window.location.href=basePath+"/takeOutController.do?mineAddressesNewOrEdit&addressId="+addressId;
		});
		$('.address_info').click(function() {
           /* Act on the event */
           var clzSty = "onSelect_gl";
           var $gldz = $(this).parent();
           $gldz.addClass(clzSty).siblings().removeClass(clzSty);
           var addressId = $gldz.find("#addressPrimaryId").val();
           changeDefaultAddress(addressId);
        });
	});
</script>
</body>

</html>