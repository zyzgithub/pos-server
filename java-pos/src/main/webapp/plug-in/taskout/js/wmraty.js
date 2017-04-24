/**
 * 外卖评价，显示星级
 */

function WaiMaiRaty() {

	this.basePath;

	this.init = function(basePath) {
		this.basePath = basePath;
		$.fn.raty.defaults.path = this.basePath + '/plug-in/wxIndex/images';
	};

	this.displayRate = function(targetElement) {
		$(targetElement).raty({
			number : 5,// 多少个星星设置
			score : 0,// 初始值是设置
			targetType : 'number',// 类型选择，number是数字值，hint，是设置的数组值
			// path : 'images',
			cancelOff : 'cancel-off-big.png',
			cancelOn : 'cancel-on-big.png',
			size : 24,
			starHalf : 'star-half-big.png',
			starOff : 'star-off-big.png',
			starOn : 'star-on-big.png',
			cancel : false,
			targetKeep : true,
			precision : false,// 是否包含小数
			hints : [ '', '', '', '', '' ] // 不显示文字提示
		/*
		 * score为点了多少颗星星 click: function(score, evt) { alert('ID: ' +
		 * $(this).attr('id') + "\nscore: " + score + "\nevent: " + evt.type); }
		 */
		});
	};
	
	//获取评价分数
	this.getRateScore = function(targetElement) {
		return $(targetElement).find("input:hidden[name='score']").attr("value");
	};
	
	
}