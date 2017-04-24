<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
    var basePath="${basePath}";
    function newTab(url,title,icon){
   	    if(icon != 'icon-add'){
   	    	var rowData = $('#execontentList').datagrid('getSelected');
    	    if (rowData.id == '') { 
    	    	tip('请选择接口'); 
    	    	return; 
    	    } 
   		    url = url + rowData.id;
   	    }
    	$('#maintabs').tabs('add', { 
    		title : title, 
    		content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>', 
    		closable : true, 
    		icon:icon,
    	}); 
    }
    
    function test(id){
    	var url = basePath+"/web/execontentController.do?test&id="+id;
    	var title = "调试接口";
    	var icon = "icon-search";
    	$('#maintabs').tabs('add', { 
    		title : title, 
    		content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>', 
    		closable : true, 
    		icon:icon,
    	}); 
    }
    
    function refresh(){
    	   $('#execontentList').treegrid('reload');
    }
</script>

<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="execontentList" title="接口" actionUrl="${basePath }/web/execontentController.do?datagrid"  idField="id"   fit="true"  treegrid="true"  pagination="false">
   <t:dgCol title="名称" field="name" treefield="text" width="150"></t:dgCol>
   <t:dgCol title="ID" field="id" treefield="code" width="100"></t:dgCol>
   <t:dgCol title="code" field="code" treefield="id" hidden="false" ></t:dgCol>
   <t:dgCol title="sql" field="sqlStatement" treefield="src" width="400"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="50"></t:dgCol>
   
  	<t:dgFunOpt title="调试" funname="test(code)"></t:dgFunOpt>
  	<t:dgDelOpt title="删除"  url="${basePath }/web/execontentController.do?del&id={code}"></t:dgDelOpt>	
 
   <t:dgToolBar  title="添加" icon="icon-add"  onclick="newTab('${basePath }/web/execontentController.do?addorupdate&type=add','新建接口','icon-add');"></t:dgToolBar>
   <t:dgToolBar  title="编辑" icon="icon-edit" onclick="newTab('${basePath }/web/execontentController.do?addorupdate&type=edit&id=','编辑接口','icon-edit');"></t:dgToolBar>
   <t:dgToolBar  title="查看"  icon="icon-search" onclick="newTab('${basePath }/web/execontentController.do?addorupdate&type=search&id=','查看接口','icon-search');"></t:dgToolBar>
   <t:dgToolBar  title="刷新"  icon="icon-reload" onclick="refresh();"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>