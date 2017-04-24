/**
 * 删除数组指定下标或指定对象
 */
Array.prototype.remove = function(obj) {
	for (var i = 0; i < this.length; i++) {
		var temp = this[i];
		if (!isNaN(obj)) {
			temp = i;
		}
		if (temp == obj) {
			for (var j = i; j < this.length; j++) {
				this[j] = this[j + 1];
			}
			this.length = this.length - 1;
		}
	}
};
// 手机浏览器版本
var browser = {
	versions : function() {
		var u = navigator.userAgent;
		return { // 移动终端浏览器版本信息
			ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
			android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或uc浏览器
			iPhone : u.indexOf('iPhone') > -1, // 是否为iPhone或者QQHD浏览器
			iPad : u.indexOf('iPad') > -1, // 是否iPad
			weixin : u.toLowerCase().indexOf("micromessenger") > -1
		};
	}(),
};
/*
 * 工具对象
 */
var Util = {
	/**
	 * @param max:
	 *            允许输入字符最大长度
	 * @param obj:
	 *            输入内容的textarea 对象: $("#story")
	 * @param tipob:
	 *            提示内容对象 $("#tip")
	 * @param 调用:
	 *            Util.tip(100,$("#story"),$("#tip"));
	 */
	tip : function(max, obj, tipobj) {
		var counter = obj.val().length; // 获取文本域的字符串长度
		tipobj.text(max - counter);
		obj.on('keyup', function() {
			var len = $(this).val().length;
			if (len > max) {
				var desc = $(this).val();
				this.value = desc.substring(0, max);
				obj.val(value); // 超过的部分自动截取
				return;
			}
			tipobj.html(max - len);
		});
	},
	/**
	 * 清空target对象下的input,select,textarea的内容
	 */
	clear : function(target) {
		$('input,select,textarea', target).each(function() {
			var type = this.type, tag = this.tagName.toLowerCase();
			if (type == 'text' || type == 'password' || tag == 'textarea') {
				this.value = '';
			} else if (tag == 'select') { // 清空select选项
				this.selectedIndex = -1
			} else if (type == 'radio' || type == 'checkbox') {
				this.checked = false;
			}
		});
	},
	/**
	 * 是否是email /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	 * /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	 */
	isEmail : function(str) {
		return /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test(str);
	},
	/**
	 * 是否是手机 /^1(3|5|8)[0-9]{9}$/;
	 */
	isMobile : function(str) {
		return /^13[\d]{9}$|14^[0-9]\d{8}|^15[0-9]\d{8}$|^18[0-9]\d{8}$/
				.test(str);
	},
	/**
	 * 检查输入的固定电话号码是否正确
	 * 输入:str  字符串
	 * 返回:true 或 flase; true表示格式正确
	 */
	isTelephone : function (str){
	    if (str.match(/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/) == null) {
	        return false;
	    }
	    else {
	        return true;
	    }
	},
	/**
	 * 是否是qq
	 */
	isQQ : function(str) {
		return /^[1-9]\d{3,10}$/.test(str);
	},
	/**
	 * 是否是身份证 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
	 */
	isCard : function(str) {
		return /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(str);
	},
	/**
	 * 是否是邮编
	 */
	isZcode : function(str) {
		return /^[0-9][0-9]{5}$/.test(str);
	},
	/**
	 * 是否是游戏id
	 */
	isGameId : function(str) {
		return /^[1-9](\d{3,})$/.test(str);
	},
	/**
	 * 去掉字符串头尾空格
	 */
	trim : function(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

};