<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var basePath="${basePath }";
	$(function(){
		setInterval(refresh,15000);
	});
	
	function refresh(){
        $('#testUserList').datagrid('reload');
	}
	
	function details(id){
		var row = $('#testUserList').datagrid('getSelected');
		var url = basePath+"/web/testUserResultController.do?testUserResult&sessionkey="+row.id+"&date="+new Date();
    	$('#maintabs').tabs('add', { 
    		title : '结果详情', 
    		content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>', 
    		closable : true, 
    		icon: 'icon-search',
    	}); 
	}
</script>


<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="testUserList" onClick="details();" title="当前进入调试模式用户" actionUrl="${basePath }/web/testUserController.do?datagrid" idField="id" fit="true">
    <t:dgCol title="id" field="id" hidden="false"></t:dgCol>
    <t:dgCol title="用户标识" field="userId" ></t:dgCol>
    <t:dgCol title="开始时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
    <t:dgCol title="结束时间" field="timeOut" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
   
   <%--
   <t:dgCol title="操作" field="opt"></t:dgCol>
   <t:dgFunOpt title="详细" funname="details(id)"></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="${basePath }/web/testUserController.do?del&sessionKey={sessionKey}" />
   <t:dgToolBar title="录入" icon="icon-add" url="${basePath }/web/testUserController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="${basePath }/web/testUserController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="${basePath }/web/testUserController.do?addorupdate" funname="detail"></t:dgToolBar>
  --%>
  </t:datagrid>
  </div>
 </div>