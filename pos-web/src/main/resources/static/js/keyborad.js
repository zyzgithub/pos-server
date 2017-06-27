(function(window, undefined){
	keyBoard = function(option,job,born){
		return new keyBoard.fn.init(option,job,born);
	};
	// 全局监听
	var CURR_FOCUS = false;
	$("textarea").focus(function(){
		CURR_FOCUS = $(this);
	});
	
	keyBoard.fn = keyBoard.prototype = {
		option : {
			self : '',
			type : 'tel',		// ['tel','abc']
			onBtnEvents : {}, 	// 按钮事件
			onStart : function(){},	//开始加载之前事件
			onEnd : function(){},		//开始加载之后事件
			
		},
		init : function(option){
			op = this.option = $.extend(this.option, option);
			
			op.onStart();
			
			var self = _option[op['type']];
			
			$(window.document.body).append(self.model.join(""));
			
			self.calcWH();
			_tool.onBtnClick()
			$(".keyboard").show();
			
			op.onEnd();
			
			return this;
		},
		hide : function(){
			$(".keyboard").hide();
		},
		show : function(){
			$(".keyboard").show();
		},
		/* 待开发 */
		type : function(type){
			$.isArray(type,this.option) && function(){
	//			var self = _option[op['type']];
	//			$(window.document.body).append(self.model.join(""));
	//			self.calcWH();
				return true;
			}
		}
	}
	
	/* 内部工具组件类 */
	_tool = {
		/* 按钮点击事件 */
		onBtnClick : function(){
			var shift = false, capslock = false, option = keyBoard.prototype.option;
			$('.keyboard li').click(function(){
				var $write = CURR_FOCUS ? CURR_FOCUS : $(option.self);//$(':focus');
				var $this = $(this),character = $this.html();
				$('.keyboard .pay').css({"background":"#ea4a4d","color":"#fff"});
				// 处理空白定义
				if(character.length == 0){
					return true;
				}
				
				// 按钮存在事件 执行事件
				if(option.onBtnEvents[character]){
					option.onBtnEvents[character]();
					return true;
				} 
				
				/* abc Shift keys */
				if ($this.hasClass('left-shift') || $this.hasClass('right-shift')) {
					$('.letter').toggleClass('uppercase');
					$('.symbol span').toggle();
					shift = (shift === true) ? false : true;
					capslock = false;
					return false;
				}
				/* abc Caps lock */
				if ($this.hasClass('capslock')) {
					$('.letter').toggleClass('uppercase');
					capslock = true;
					return false;
				}
				/* Delete */
				if ($this.hasClass('delete')) {
					var html = $write.val();
					var textNum = _tool.getCursortPosition();
					var arr = html.split("");
					arr.splice(textNum-1,1);
					var num = arr.join(",").replace(/,/g, "");
					if(html.length == 1 || html.length == 0) {
						$('.keyboard .pay').css({"background":"#eee","color":"#666"});
					}
					$write.val(num);
					
					// 存在change事件 则执行
					if($write.attr('event-change')){
						eval($write.attr('event-change'))();
					}
					return false;
				}
				if($this.hasClass('down')){
					//$('.keyboard').css({"transform":"scale(0, 1)","opacity":0})
					$('.keyboard').hide();
					return false;
				}
				/* 特殊字符 */
				if ($this.hasClass('symbol')) character = $('span:visible', $this).html();
				if ($this.hasClass('space')) character = ' ';
				if ($this.hasClass('tab')) character = "\t";
				if ($this.hasClass('return')) character = "\n";
				if ($this.hasClass('confirm')) character = "";
				/* 大写字母*/
				if ($this.hasClass('uppercase')) character = character.toUpperCase();
				// Remove shift once a key is clicked.
				if (shift === true) {
					$('.symbol span').toggle();
					if (capslock === false) $('.letter').toggleClass('uppercase');
					shift = false;
				}
				/* 添加字符 */
				if($write.val().length>5) return false;
				var addtextNum = _tool.getCursortPosition();
				var addArr = $write.val().split("");
				addArr.splice(addtextNum,0,character);
				addArr = addArr.join(",").replace(/,/g, "");
				$write.val(addArr);
				//$write.val($write.val() + character);
				
				// 存在change事件 则执行
				if($write.attr('event-change')){
					eval($write.attr('event-change'))();
				}
			});
		},
		loadScript : function(src){
			$('<script />',{type:'text/javascript',src : src ,charset:'utf-8'}).appendTo($('head'));
		},
		getCursortPosition : function(){
			var el = $("#money").get(0); 
	        var pos = 0;
	        if ('selectionStart' in el) {
	            pos = el.selectionStart;
	        } else if ('selection' in document) {
	            el.focus();
	            var Sel = document.selection.createRange();
	            var SelLength = document.selection.createRange().text.length;
	            Sel.moveStart('character', -el.value.length);
	            pos = Sel.text.length - SelLength;
	        }
	       	 return pos;
		}
	}
	
	// 键盘配置
	var _option = {
		tel : {
			calcWH : function(){
				var winW = $(window).width();
				var winH = $(window).height();
				// 计算键盘宽高
				$(".keyboard li").width((winW-3)/4);
				$(".keyboard li").height(winH/2/4);
				$(".keyboard li").css("line-height",winH/2/4+"px");
				
				// 计算 支付 按钮数据
				$(".keyboard .pay").height(winH/2/4*3+2);
				$(".keyboard .pay").css("line-height",winH/2/4*3+2+"px");	
				
				$(".li-2").width((winW-3)/4*2);
			},
			model : [
				 "<ul class='keyboard'>"
				,"<li class='b-r b-t'>1</li>"
				,"<li class='b-r b-t'>2</li>"
				,"<li class='b-r b-t'>3</li>"
				,"<li class='b-t delete'>del</li>"
				,"<li class='b-r b-t clear'>4</li>"
				,"<li class='b-r b-t'>5</li>"
				,"<li class='b-r b-t'>6</li>"
				,"<li class='b-r b-t clear'>7</li>"
				,"<li class='b-r b-t'>8</li>"
				,"<li class='b-r b-t'>9</li>"
				,"<li class='b-r b-t clear down'>hide</li>"
				,"<li class='b-r b-t'>0</li>"
				,"<li class='b-r b-t'>.</li>"
				,"<li class='b-t pay'>确认付款</li></ul>"
			]
		},
		abc : {} //待开发
	}
	
	keyBoard.fn.init.prototype = keyBoard.fn;
	return keyBoard;
})(window)
