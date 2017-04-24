//﻿var jq = jQuery.noConflict();
/**
 * 增删改工具栏
 */
window.onerror = function() {
	return true;
};
var iframe;// iframe操作对象
var win;//窗口对象
var gridname="";//操作datagrid对象名称
//update-begin--Author:chenxu  Date:20130402 for：父窗口将子窗口遮盖
var windowapi = frameElement.api, W = windowapi.opener;//内容页中调用窗口实例对象接口
//update-end--Author:chenxu  Date:20130402 for：父窗口将子窗口遮盖
function upload(curform) {
	upload();
}
/**
 * 添加事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
function add(title,addurl,gname) {
	gridname=gname;
	createwindow(title, addurl);
}
/**
 * 树列表添加事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
//update-begin--Author:liutao  Date:20130405 for：为树列表增加特殊的添加方法
function addTreeNode(title,addurl,gname) {
	if (rowid != '') {
		addurl += '&id='+rowid;
	}
	gridname=gname;
	createwindow(title, addurl);
}
//update-end--Author:liutao  Date:20130405 for：为树列表增加特殊的添加方法
/**
 * 更新事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function update(title,url, id) {
	//update-begin--Author:tanghong  Date:20130429 for：#75表格选中，跳转tab,无法编辑
	var rowData = $('#'+id).datagrid('getSelected');
	//update-begin--Author:tanghong  Date:20130504 for：#17 有的时候，明细行明选择，点击编辑却提示未选中行
	if (!rowData) {
		tip('请选择编辑项目');
		return;
	}
	//update-end--Author:tanghong  Date:20130504 for：#17 有的时候，明细行明选择，点击编辑却提示未选中行
	url += '&id='+rowData.id;
	//update-end--Author:tanghong  Date:20130429 for：#75表格选中，跳转tab,无法编辑
	createwindow(title,url);
}

//update-begin--Author:sun  Date:20130503 for：增加表单详细查看功能

/**
 * 如果页面是详细查看页面，无效化所有表单元素，只能进行查看
 */
$(function(){
	if(location.href.indexOf("load=detail")!=-1){
		$(":input").attr("disabled","true");
	}
});

/**
 * 查看详细事件打开窗口
 * @param title 查看框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function detail(title,url, id) {
	var rowData = $('#'+id).datagrid('getSelected');
	if (!rowData) {
		tip('请选择查看项目');
		return;
	}
    url += '&load=detail&id='+rowData.id;	 
	createdetailwindow(title,url);
}
/**
 * 查看时的弹出窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function createdetailwindow(title, addurl) {
	if(typeof(windowapi) == 'undefined'){
		$.dialog({
			content: 'url:'+addurl,
			lock : true,
			title:title,
			opacity : 0.3,
			cache:false, 
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}else{
		W.$.dialog({
			content: 'url:'+addurl,
			lock : true,
			parent:windowapi,
			title:title,
			opacity : 0.3,
			cache:false, 
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}
	
}
//update-end--Author:sun  Date:20130503 for：增加表单详细查看功能
/**
 * 全屏编辑
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function editfs(title,url) {
	var name=gridname;
	 if (rowid == '') {
		tip('请选择编辑项目');
		return;
	}
	url += '&id='+rowid;
	openwindow(title,url,name,800,500);
}
// 删除调用函数
function delObj(url,name) {
	createdialog('删除确认 ', '确定删除该记录吗 ?', url,name);
}
// 删除调用函数
function confuploadify(url, id) {
	$.dialog.confirm('确定删除吗', function(){
		deluploadify(url, id);
	}, function(){
	});
}
/**
 * 执行删除附件
 * 
 * @param url
 * @param index
 */
function deluploadify(url, id) {
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
//			var d = $.parseJSON(data);
			var d = data;
			if (d.success) {
				$("#" + id).remove();// 移除SPAN
				m.remove(id);// 移除MAP对象内字符串
			}

		}
	});
}
// 普通询问操作调用函数
function confirm(url, content,name) {
	createdialog('提示信息 ', content, url,name);
}
/**
 * 提示信息

function tip(msg) {
	$.dialog.setting.zIndex = 1980;
	$.dialog.tips(msg, 3);
}
 */

function tip(msg) {
	$.dialog.setting.zIndex = 1980;
	$.messager.show({
		title : '提示信息',
		msg : msg,
		timeout : 1000 * 6
	});
}

/**
 * 创建添加或编辑窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
 //update-begin--Author:chenxu  Date:20130402 for：父窗口将子窗口遮盖
function createwindow(title, addurl) {
	if(typeof(windowapi) == 'undefined'){
		$.dialog({
			content: 'url:'+addurl,
			lock : true,
			title:title,
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    	iframe = this.iframe.contentWindow;
				saveObj();
				return false;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}else{
		W.$.dialog({
			content: 'url:'+addurl,
			lock : true,
			parent:windowapi,
			title:title,
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    	iframe = this.iframe.contentWindow;
				saveObj();
				return false;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}
	
}
//update-end--Author:chenxu  Date:20130402 for：父窗口将子窗口遮盖
/**
 * 创建上传页面窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function openuploadwin(title, url,name,width, height) {
	gridname=name;
	$.dialog({
	    content: 'url:'+url,
	    cache:false,
	    button: [
	        {
	            name: '开始上传',
	            callback: function(){
	            	iframe = this.iframe.contentWindow;
					iframe.upload();
					return false;
	            },
	            focus: true
	        },
	        {
	            name: '取消上传',
	            callback: function(){
	            	iframe = this.iframe.contentWindow;
					iframe.cancel();
	            }
	        }
	    ]
	});
}
/**
 * 创建查询页面窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function opensearchdwin(title, url, width, height) {
	$.dialog({
		content: 'url:'+url,
		title : title,
		lock : true,
		height : height,
		cache:false,
		width : width,
		opacity : 0.3,
		button : [ {
			name : '查询',
			callback : function() {
				iframe = this.iframe.contentWindow;
				iframe.searchs();
			},
			focus : true
		}, {
			name : '取消',
			callback : function() {

			}
		} ]
	});
}
/**
 * 创建不带按钮的窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function openwindow(title, url,name, width, height) {
	gridname=name;
	if (typeof (width) == 'undefined'&&typeof (height) != 'undefined')
	{
		$.dialog({
			content: 'url:'+url,
			title : title,
			cache:false,
			lock : true,
			width: 'auto',
		    height: height
		});
	}
	if (typeof (height) == 'undefined'&&typeof (width) != 'undefined')
	{
		$.dialog({
			content: 'url:'+url,
			title : title,
			lock : true,
			width: width,
			cache:false,
		    height: 'auto'
		});
	}
	if (typeof (width) == 'undefined'&&typeof (height) == 'undefined')
	{
	$.dialog({
		content: 'url:'+url,
		title : title,
		lock : true,
		width: 'auto',
		cache:false,
	    height: 'auto'
	});
	}
	
	if (typeof (width) != 'undefined'&&typeof (height) != 'undefined')
	{
	$.dialog({
		width: width,
	    height:height,
		content: 'url:'+url,
		title : title,
		cache:false,
		lock : true
	});
	}
}

/**
 * 创建询问窗口
 * 
 * @param title
 * @param content
 * @param url
 */
function createdialog(title, content, url,name) {
	$.dialog.confirm(content, function(){
		doSubmit(url,name);
		//update-begin--Author:zhangdaihao  Date:20130413 for：删除数据rowid未清空
		rowid = '';
		//update-begin--Author:zhangdaihao  Date:20130413 for：删除数据rowid未清空
	}, function(){
	});
}
/**
 * 执行保存
 * 
 * @param url
 * @param gridname
 */
function saveObj() {
	$('#btn_sub', iframe.document).click();
}

/**
 * 执行AJAX提交FORM
 * 
 * @param url
 * @param gridname
 */
function ajaxSubForm(url) {
	$('#myform', iframe.document).form('submit', {
		url : url,
		onSubmit : function() {
			iframe.editor.sync();
		},
		success : function(r) {
			tip('操作成功');
			reloadTable();
		}
	});
}
/**
 * 执行查询
 * 
 * @param url
 * @param gridname
 */
function search() {

	$('#btn_sub', iframe.document).click();
	iframe.search();
}

/**
 * 执行操作
 * 
 * @param url
 * @param index
 */
function doSubmit(url,name) {
	gridname=name;
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
//			var d = $.parseJSON(data);
			var d = data;
			if (d.success) {
				var msg = d.msg;
				tip(msg);
				reloadTable();
			}
		}
	});
}
/**
 * 退出确认框
 * 
 * @param url
 * @param content
 * @param index
 */
function exit(url, content) {
	$.dialog.confirm(content, function(){
		window.location = url;
	}, function(){
	});
}
/**
 * 模板页面ajax提交
 * 
 * @param url
 * @param gridname
 */
function ajaxdoSub(url, formname) {
	$('#' + formname).form('submit', {
		url : url,
		onSubmit : function() {
			editor.sync();
		},
		success : function(r) {
			tip('操作成功');
		}
	});
}
/**
 * ajax提交FORM
 * 
 * @param url
 * @param gridname
 */
function ajaxdoForm(url, formname) {
	$('#' + formname).form('submit', {
		url : url,
		onSubmit : function() {
		},
		success : function(r) {
			tip('操作成功');
		}
	});
}

function opensubwin(title, url, saveurl, okbutton, closebutton) {
	$.dialog({
		content: 'url:'+url,
		title : title,
		lock : true,
		opacity : 0.3,
		button : [ {
			name : okbutton,
			callback : function() {
				iframe = this.iframe.contentWindow;
				win = frameElement.api.opener;// 来源页面
				$('#btn_sub', iframe.document).click();
				return false;
			}
		}, {
			name : closebutton,
			callback : function() {
			}
		} ]

	});
}

function openauditwin(title, url, saveurl, okbutton, backbutton, closebutton) {
	$.dialog({
		content: 'url:'+url,
		title : title,
		lock : true,
		opacity : 0.3,
		button : [ {
			name : okbutton,
			callback : function() {
				iframe = this.iframe.contentWindow;
				win = $.dialog.open.origin;// 来源页面
				$('#btn_sub', iframe.document).click();
				return false;
			}
		}, {
			name : backbutton,
			callback : function() {
				iframe = this.iframe.contentWindow;
				win = frameElement.api.opener;// 来源页面
				$('#formobj', iframe.document).form('submit', {
					url : saveurl + "&code=exit",
					onSubmit : function() {
						$('#code').val('exit');
					},
					success : function(r) {
						$.dialog.tips('操作成功', 2);
						win.location.reload();
					}
				});

			}
		}, {
			name : closebutton,
			callback : function() {
			}
		} ]

	});
}
// 添加标签
function addOneTab(subtitle, url, icon) {
	if (icon == '') {
		icon = 'icon folder';
	}
	window.top.$.messager.progress({
		text : '页面加载中....',
		interval : 300
	});
	window.top.$('#maintabs').tabs({
		onClose : function(subtitle, index) {
			window.top.$.messager.progress('close');
		}
	});
	if (window.top.$('#maintabs').tabs('exists', subtitle)) {
		window.top.$('#maintabs').tabs('select', subtitle);
		window.top.$('#maintabs').tabs('update', {
			tab : window.top.$('#maintabs').tabs('getSelected'),
			options : {
				title : subtitle,
				href:url,
				//content : '<iframe name="tabiframe"  scrolling="no" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>',
				closable : true,
				icon : icon
			}
		});
	} else {
		if (url.indexOf('isIframe') != -1) {
			window.top.$('#maintabs').tabs('add', {
				title : subtitle,
				content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
				closable : true,
				icon : icon
			});
		}else {
			window.top.$('#maintabs').tabs('add', {
				title : subtitle,
				href:url,
				closable : true,
				icon : icon
			});
		}
	}
}
// 关闭自身TAB刷新父TABgrid
function closetab(title) {
	//暂时先不刷新
	//window.top.document.getElementById('tabiframe').contentWindow.reloadTable();
	//window.top.document.getElementById('maintabs').contentWindow.reloadTable();
	window.top.$('#maintabs').tabs('close', title);
	//tip("添加成功");
}
