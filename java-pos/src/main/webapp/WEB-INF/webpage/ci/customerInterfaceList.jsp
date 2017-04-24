<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript">
	function testCI(id, name){
		var url = "${basePath }/web/customerInterfaceController.do?test&id="+id;
		var title = "调试-"+name;
		var icon = "icon-search";
		$('#maintabs').tabs('add', { 
			title : title, 
			content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>', 
			closable : true, 
			icon:icon,
		}); 
	}
</script>


<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
	  <t:datagrid name="customerInterfaceList" title="自定义接口" actionUrl="${basePath }/web/customerInterfaceController.do?datagrid" idField="id" fit="true" treegrid="true"  pagination="false">
		   <t:dgCol title="编号" field="id" hidden="false" treefield="id"></t:dgCol>
		   <t:dgCol title="名称" field="name" treefield="text" width="4"></t:dgCol>
		   <t:dgCol title="方法名" field="methodName" treefield="src" width="2"></t:dgCol>
		   <t:dgCol title="描述" field="descrition" treefield="code" width="4"></t:dgCol>
		   <t:dgCol title="操作" field="opt" width="2"></t:dgCol>
		   <t:dgFunOpt title="调试" funname="testCI(id, text)"></t:dgFunOpt>
		   <t:dgDelOpt title="删除" url="${basePath }/web/customerInterfaceController.do?del&id={id}" />
		   <t:dgToolBar title="录入" icon="icon-add" url="${basePath }/web/customerInterfaceController.do?addorupdate" funname="add"></t:dgToolBar>
		   <t:dgToolBar title="编辑" icon="icon-edit" url="${basePath }/web/customerInterfaceController.do?addorupdate" funname="update"></t:dgToolBar>
		   <t:dgToolBar title="查看" icon="icon-search" url="${basePath }/web/customerInterfaceController.do?addorupdate" funname="detail"></t:dgToolBar>
	  </t:datagrid>
  </div>
 </div>