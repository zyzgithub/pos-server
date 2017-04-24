<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
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
</head>
<style type="text/css">
    html{font-size:100px;}
</style>
<body>
<div class="wrapper" style="max-width:640px;margin:0 auto;">
  <div class="cont_pic">
    <!-- 领取红包前的图片-->
    <img src="${basePath}/plug-in/wxIndex/images/voucher_before_bg.png" alt="" />  
    <!--<img src="images/voucher_after_bg" alt="" />  领取红包后的图片  -->
  </div>
  <div class="cont_main">
<!--  领取红包前  -->
    <div class="cont_top_before">
       <div class="inputFrame"><input type="text" id="usermobile" placeholder="请输入您的手机号" /></div>
       <div class="unpickpackets"><img src="${basePath}/plug-in/wxIndex/images/unpickpackets.png" alt="" /></div>
    </div>
<!--  领取红包后   -->
    <div class="cont_top_after">
      <%-- <h3>20<span>元</span></h3>
      <div class="cont_tip">
        <p>红包已到账！赶快订餐吧，不要过期噢~</p>
        <p class="phones">18824508976<!-- <span>修改 > </span> --></p>       
      </div>
      <div class="useBtn">
        <a href="${basePath}/takeOutController.do?merchantList" class="usePacket">快去使用红包</a>
      </div> --%>      
    </div>

    <div class="cont_bott_list">
      <%-- <h3><img src="${basePath}/plug-in/wxIndex/images/title.png" alt="" /></h3>
      <div class="userPacketInfo">
        <ul>
          <li>
            <div class="userPacket-icon"><img src="${basePath}/plug-in/wxIndex/images/mine_default-photo.png" alt="" /></div>
            <div class="userPacket-cont">
              <div class="userPacket-call">
                <p>12345678900</p><span>2015-09-15&nbsp;12:40</span>
              </div>
            </div>
            <p class="userPacket-money">20元</p>
          </li>
        </ul>
      </div> --%>
    </div>
<!-- 点击查看规则弹出 
	<div class="rulesDiv">
       <div class="rulesShadowDiv"></div>
       <div class="ruleList">
         <p>快去使用红包</p>
         <p>快去使用红包</p>
         <p>快去使用红包</p>
         <p>快去使用红包</p>
         <p>快去使用红包</p>
         <p>快去使用红包</p>
         <span class="rulesClose"></span>
       </div>
    </div> -->    
    
  </div>
</div>

<script type="text/javascript">
 
  $(function(){
	var serial = "${serial}";
	
	/* 加载领取人 */
	$.post('${basePath}/coupons/getreceiver.do', {"serial":serial}, function(data) {
		if(data.status == 'success') {
			$('.cont_bott_list').empty();
			
			var receiver = data.receiver;
			var html = '';
			/* html += '<span class="couponRules">查看规则</span>'; */
			html += '<h3><img src="${basePath}/plug-in/wxIndex/images/title.png" alt="" /></h3>';
			html += '<div class="userPacketInfo">';
			html += '<ul>';
			
			$.each(receiver, function(i, v) {
				html += '<li>';
				if(v.header == '')
					html += '<div class="userPacket-icon"><img src="http://oss.0085.com/user/20151010.png" alt="" /></div>';
				else
					html += '<div class="userPacket-icon"><img src="'+v.header+'" alt="" /></div>';
				
				html += '<div class="userPacket-cont">';
				html += '<div class="userPacket-call">';
				html += '<p>'+v.mobile+'</p><span>'+v.time+'</span>';
				html += '</div>';
				html += '</div>';
				html += '<p class="userPacket-money">'+(v.money/100)+'元</p>';
				html += '</li>';
			});
			
			html += '</ul>';
			html += '</div>';
			
			$('.cont_bott_list').append(html);
		}
	}, "json");
	
	/* 适配屏幕  */
	load_sizing();
    $(window).resize(function(){
      load_sizing();
    });
    var pic_Height = $(window).height();
    $('.cont_pic').css('height',pic_Height);
    /* 点拆红包 */
    $('.unpickpackets').click(function() {
    	var p = /^1\d{10}$/;
    	var mobile = $("#usermobile").val();
    	if(mobile == null || mobile == '') {
			$.WapDialog.tip("手机号不能为空");
			return;
		} else if(!p.test(mobile)) {
			$.WapDialog.tip("手机号有误");
			return;
		}
    	$.WapDialog.lock({content:'正在拆...'});
    	
    	var url = "${basePath}/coupons/receive.do";
    	$.post(url, {"serial":serial, "mobile":mobile}, function(data) {
    		  var html = '';
    		  if(data.status == 'success') {
    			  var c = data.coupons;
    			  html += '<h3>'+(c.couponsMoney/100)+'<span>元</span></h3>';
    			  html += '<div class="cont_tip">';
    			  html += '<p>赶快订餐吧，不要过期噢~</p>';
    			  html += '<p class="phones">'+c.userMobile+'</p></div>';
    			  html += '<div class="useBtn">';
    			  html += '<a href="${basePath}/takeOutController.do?merchantList" class="usePacket">快去使用</a>';
    			  html += '</div>';
    		  } else {
    			  html += '<div class="cont_tip">';
    			  html += '<p>'+data.msg+'</p>';
    			  html += '<p class="phones">'+mobile+'</p></div>';
    			  html += '<div class="useBtn">';
    			  html += '<a href="${basePath}/takeOutController.do?merchantList" class="usePacket">1号外卖</a>';
    			  html += '</div>';
    		  }
    		  $('.cont_top_after').append(html);
		      $('.cont_top_before').hide();
		      $('.cont_pic').children('img').attr('src','${basePath}/plug-in/wxIndex/images/voucher_after_bg.png');
		      $('.cont_top_after').fadeIn();
		      $.WapDialog.close();
    	}, "json");
    });
  })
  
   function load_sizing(){
	  var windowWidth =$(window).width();
	  if(windowWidth>=640){
	      windowWidth =640;
	      $("html").css("font-size",100+"px");
	  }else{
	      $("html").css("font-size",50 * (windowWidth / 320) +"px");
      }
   }
</script>
</body>
</html>