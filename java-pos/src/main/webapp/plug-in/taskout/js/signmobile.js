/**
 * 绑定手机号管理类
 * @author jusnli
 * @email 545987886@qq.com
 */
var signMobileMng;//引入界面需要在界面加载完后实例化该对象
function SignMobileMng(){
	this.basePath="";
	this.t = 60;			//定时时长
	this.timeId;			//定时器id
	this.countDown=false;	//是否可以点击发验证码
	
	//初始化
	this.init = function(basePath){
		this.basePath=basePath;
		$('#sendBtn').click(function(){
			signMobileMng.sendCode();
		});
		
		$('#bindBtn').click(function(){
			signMobileMng.bindMobile();
		});
	};
	//发送验证码
	this.sendCode = function (){
		if(this.countDown)return;
		
		var mobile=$('#mobile').val();
		if(!mobile){
			$.WapDialog.tip('请输入手机号');
			$('#mobile').focus();
			return;
		}
		var re = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/;

 		if(!re.test($('#mobile').val())){
 			$.WapDialog.tip('手机号有误，请输入正确的手机号码');
			$('#mobile').focus();
 			return;
		}else{
			//检验号码是否已被使用
			$.ajax({
				url: basePath+'/takeOutController.do?checkMobile&time=' + Math.round(new Date().getTime()/1000),
				data:{mobile:mobile},
				dataType:'json',
				success: function(data){
					if(data.success == true){//手机号码未被使用
						if(signMobileMng.countDown)return;
						signMobileMng.countDown=true;
						//发送验证码
						$.ajax({
							url: basePath+'/ci/smsController.do?sendSms&time=' + Math.round(new Date().getTime()/1000),
							data:{
								phone: $('#mobile').val()
							},
							dataType:'json',
							success: function(data){
								if(data.success == true){
									$.WapDialog.tip("验证码已发送，请检查是否收到");
									signMobileMng.t = 60;
									signMobileMng.timeId=setInterval("signMobileMng.change()",1000);
								}else{
									countDown=false;
									$.WapDialog.tip(data.msg);
								}
							},error:function(e){
								countDown=false;
								$.WapDialog.tip('发送失败，请重新发送');
							}
						});
					}else{
						//提示信息
						$.WapDialog.tip(data.msg);
						$('#mobile').focus();
					}
				},error:function(e){
					$.WapDialog.tip('抱歉!操作失败，请重试');
				}
			});
		}
	};
	
	//绑定手机号
	this.bindMobile = function (){
		var re1 = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/;
		var re2 = /[\w\W]+/;
		var mobile=$('#mobile').val();
		var code=$('#code').val();
		var isMobileBinded = true;
		var bindedMsg = "";
		if(!mobile){
			$.WapDialog.tip('请输入手机号');
			$('#mobile').focus();
			return;
		}else if(!re1.test($('#mobile').val())){
 			$.WapDialog.tip('手机号有误，请输入正确的手机号码');
			$('#mobile').focus();
 			return;
		}else{
			$.WapDialog.lock({content:'正在验证手机号...'});//锁屏
			//查询此手机号是否已经绑定到某个帐号，如果已经绑定，则提示已经绑定到其它帐号
			$.ajax({
				url: basePath+'/takeOutController.do?isMobileBinded&time=' + Math.round(new Date().getTime()/1000),
				data:{
					mobile: $('#mobile').val(),
					userType: "user"
				},
				dataType: 'json',
				async: false,
				success: function(data){
					$.WapDialog.close();
					if(data.success == true){
						isMobileBinded = false;
					}else{
						$('#mobile').focus();
						bindedMsg = data.msg;
					}					
				},
				error:function(e){
					$.WapDialog.close();
					bindedMsg = data.msg;
				}
			});
		}
		if(isMobileBinded){
			$.WapDialog.tip(bindedMsg);
			return;
		}
		if(!code){
			$.WapDialog.tip('请输入验证码');
			$('#code').focus();
			return;
		}else if(!re2.test(code)){
			$.WapDialog.tip('验证码输入有误');
			$('#code').focus();
			return;
		}else{
			$.WapDialog.lock({content:'正在进行绑定...'});//锁屏
			//校验验证码是否正确
			$.ajax({
				url: basePath+'/ci/validateCodeController.do?validateCode&time=' + Math.round(new Date().getTime()/1000),
				data:{
					validateCodeStr: $('#mobile').val()+$('#code').val()
				},
				dataType:'json',
				success: function(data){
					$.WapDialog.close();
					if(data.success == true){
						$.ajax({
							url : basePath+'/takeOutController.do?bindMobile&time=' + Math.round(new Date().getTime()/1000),
							data: {
								mobile : $('#mobile').val()
							},
							dataType:'json',
							success: function(data){
								if(data.success==true){
									$.WapDialog.open({content:'成功绑定手机号,3秒后将自动返回个人中心',ok:{show:false},times:3000});
									setTimeout(function(){
										//window.location.href = basePath+'/takeOutController.do?shoppingcar&goods=' + escape($('#goods').val());
										//window.location.href = basePath+'/takeOutController.do?selfInfo';
										window.location.href = basePath+'/takeOutController.do?mine';
										
									},3000);
								}else{
									$.WapDialog.tip(data.msg);
								}
							}
							,error:function(e){
								$.WapDialog.tip("操作失败,请重试");
							}
						});
					}else{
						$('#code').focus();
						$.WapDialog.tip("验证码错误，请重新输入");
					}					
				},
				error:function(e){
					$.WapDialog.close();
					$.WapDialog.tip('绑定失败，请重试');
				}
			});
		}
	};
	
	//发送验证码后倒计时
	this.change = function(){
		if(this.t >0){
			this.t--;
			$('#sendBtn').html("重发("+ this.t +")");
		}else{
			this.countDown=false;
			$('#sendBtn').html("获取验证码");
			window.clearInterval(this.timeId);
		}
	};
}