<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<c:if test="${isframe == true}"> 
<t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
</c:if>
<script type="text/javascript">
	var basePath="${basePath }";
	$(function() {
		var department = $("#department_span");
		$("#userListByDepartstb").find("div[name='searchColums']").append(department);
		$('#demotree').tree({
			animate : true,
			url : basePath+'/departController.do?departgridByUser',
			onClick : function(node) {
				$("#department_id").val(node.id);
				userListByDepartssearch();
			}
		});
		
		
	});
	
</script>
<div class="easyui-layout" fit="true">
 <!-- update-begin--Author:TangHong  Date:20130515 for：[91]demo分类菜单中点击表单验证报错 -->
 <link rel="stylesheet" href="${basePath }/plug-in/Validform/css/divfrom.css" type="text/css" />
 <link rel="stylesheet" href="${basePath }/plug-in/Validform/css/style.css" type="text/css" />
 <link rel="stylesheet" href="${basePath }/plug-in/Validform/css/tablefrom.css" type="text/css" />
 <script type="text/javascript" src="${basePath }/plug-in/Validform/js/Validform_v5.3.1_min.js"></script>
 <script type="text/javascript" src="${basePath }/plug-in/Validform/js/Validform_Datatype.js"></script>
 <script type="text/javascript" src="${basePath }/plug-in/Validform/js/datatype.js"></script>
 <!-- update-end--Author:TangHong  Date:20130515 for：[91]demo分类菜单中点击表单验证报错 -->
 
 <div region="center" style="padding: 3px;" class="easyui-panel" id="demopanle">
 
  <t:datagrid name="userListByDeparts" title="用户"  actionUrl="${basePath }/userController.do?datagridUserByDepart" fit="true" fitColumns="true" idField="id" queryMode="group"  checkbox="${checkbox }">
	<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
	<t:dgCol title="用户名" sortable="false" field="userName" query="true" width="20"></t:dgCol>
	<t:dgCol title="真实姓名" field="realName" query="true" ></t:dgCol>
	<t:dgCol title="状态" sortable="true" field="status" replace="正常_1,禁用_0,超级管理员_-1" ></t:dgCol>
	<t:dgCol title="区域" field="area" query="true" ></t:dgCol>
	
		<t:dgToolBar title="" icon="" url="" funname="" ></t:dgToolBar>
	
	<c:if test="${search != true}">  
	 <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="${basePath }/userController.do?del&id={id}&userName={userName}" />
	<t:dgToolBar title="用户录入" icon="icon-add" url="${basePath }/userController.do?addorupdate" funname="add" ></t:dgToolBar>
   <t:dgToolBar title="用户编辑" icon="icon-edit" url="${basePath }/userController.do?addorupdate" funname="update"></t:dgToolBar>
	</c:if>
</t:datagrid>
 </div>
 <div region="west" style="width: 150px;" title="部门" split="true">
  <div class="easyui-panel" style="padding:1px;" fit="true" border="false">
   <ul id="demotree">
   </ul>
  </div>
 </div>
 
    <span style="display:-moz-inline-box;display:inline-block;" id="department_span">
   		<input type="hidden" name="departid" id="department_id" />
	 </span>
</div>
   
   
