/**
 * author:李法强
 * Date:2014-12-29
 * Email:545987886@qq.com
 * 适用手机和浏览器的消息框, 支持按钮回调函数,支持扩展消息框按钮，支持指定消息框标题
 */
jQuery.WapDialog={
	id:null,
	times:null,	//自动关闭时间,毫秒
	open:function(options_){//打开
		var defaults={
			id:'wapDialog_'+(new Date()).getTime(),
			title:null,		//标题，默认无标题不显示
			content:null,		//消息内容
			ok:{id:'btn_ok_'+(new Date()).getTime(),name:'确定',callBack:null,show:true},			//确定按钮,默认显示
			cancel:{id:'btn_cancel_'+(new Date()).getTime(),name:'取消',callBack:null,show:false},	//取消按钮，默认不显示
			buttons:[],		//扩展按钮，格式和ok,cancel一样
			times:null,		//打开后自动关闭时间
			init:null		//初始化后执行
		};
		
		if(options_ && options_.ok){
			$.extend(defaults.ok,options_.ok);
		}
		if(options_ && options_.cancel){
			$.extend(defaults.cancel,options_.cancel);
		}
		
		var options=$.extend({},defaults,options_);
		options.buttons.reverse();
		options.buttons.push(defaults.cancel);
		options.buttons.push(defaults.ok);
		options.buttons.reverse();
		this.id=options.id;
		this.times=options.times;
		this.init=options.init;
		var wapDialogHtml=''
				+'<div class="wapDialog" id="'+options.id+'">'
				+'	<div class="wapDialog_bg"></div>'
				+'	<div class="wapDialog_dialog">'
				+'		<div class="wapDialog_dialog_top">'
				+'			<div class="wapDialog_dialog_top_title"></div>'
				+'		</div>'
				+'		<div class="wapDialog_line"></div>'
				+'		<div class="wapDialog_dialog_content"><div><div></div></div></div>'
				+'		<div class="wapDialog_dialog_bottom">'
				//+'			<a class="wapDialog_dialog_top_button" href="javascript:void(0);" >确定</a>'
				+'		</div>'
				+'	</div>'
				+'</div>';
		
		var wapDialog=$(wapDialogHtml);
		$('body').append(wapDialog);
		if(options.title){
			$(wapDialog).find(".wapDialog_dialog_top_title").html(options.title);
		}else{
			$(wapDialog).find(".wapDialog_dialog_top,.wapDialog_line").remove();
			$(wapDialog).find(".wapDialog_dialog_content").css({'height':'120px'});
		}		
		if(options.content){
			$(wapDialog).find(".wapDialog_dialog_content >div >div").html(options.content);
		}else{
			$(wapDialog).find(".wapDialog_dialog_content").remove();
		}
		var btnSize=0;
		$.each(options.buttons,function(k,v){
			if(false!=v.show){
				btnSize++;
			}
		});
		for(var i=0;i<options.buttons.length;i++){
			var button=options.buttons[i];
			if(false!=button.show){
				if(!button.id){
					button.id='btn_temp_'+(new Date()).getTime()+"_"+i;
				}
				$(wapDialog).find(".wapDialog_dialog_bottom").append('<a class="wapDialog_dialog_top_button border'+'" href="javascript:void(0);" id="'+button.id+'" >'+button.name+'</a>');
				$('#'+button.id).bind('click',{button:button},function(event){
					if(event.data.button.callBack!=null){
						event.data.button.callBack();
					}
					$(wapDialog).remove();
				});
			}
		}
		if(btnSize !=2 || !options.content){
			$(".wapDialog_dialog_top_button").css({'width':'100%','border-right':'none'});
		}else if(btnSize==2){
			$(".wapDialog_dialog_top_button").css({'margin-bottom':'1px'});
			$(".wapDialog_dialog_top_button:eq(0)").css({'margin-left':'1px'});
			$(".wapDialog_dialog_top_button:eq(1)").css({'border-right':'none'});
		}
		if(btnSize>3){
			$('.wapDialog_dialog').css({'top':'5%'});
		}
		$(wapDialog).show();

		if(this.init!=null){
			this.init();
		}

		if(this.times){
			setTimeout(function(){$.WapDialog.close();},this.times);
		}
		return this;
	},
	close:function(id_){//关闭,id_非必须
		if(id_){
			$('#'+id_).remove();
		}else if(this.id){
			$('#'+this.id).remove();
		}else{
			$('.wapDialog').remove();
		}
	},
	tip:function(content,time){
		if(!time){time=2000;}
		$('body').append('<div class="wapDialog_tip"><div class="wapDialog_tip_content">'+content+'</div></div>');
		setTimeout(function(){$('.wapDialog_tip').remove();},time);
	},
	lock:function(options_){
		var defaults={
			id:'wapDialog_'+(new Date()).getTime(),
			content:'',		//消息内容
			time:0			//关闭倒计时，毫秒,0是表示不关闭
		};
		var options=$.extend({},defaults,options_);
		this.id=options.id;
		var wapDialogHtml=
				'<div class="wapDialog" id="'+options.id+'">'
				+'	<div class="wapDialog_bg"></div>'
				+'<div class="wapDialog_tip"><div class="wapDialog_tip_content">'+options.content+'</div></div>'
				+'</div>';
		$('body').append(wapDialogHtml);
		if(options.time>0){
			setTimeout(function(){$('#'+options.id).remove();},2000);
		}
		return this;
	},
	loading:function(){
		var wapDialogHtml=
				'<div class="wapDialog">'
				+'	<div class="wapDialog_bg"></div>'
				+'	<div class="wapDialog_tip"><div class="spinner"></div></div>'
				+'</div>';
		$('body').append(wapDialogHtml);
	}
};
//界面跳转关闭所有弹出框
$(function(){
	$(window).bind('beforeunload',function(){
		$.WapDialog.loading();
	});
	$.WapDialog.close();
});