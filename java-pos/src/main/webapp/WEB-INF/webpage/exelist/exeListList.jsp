<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<!DOCTYPE html>
<html>
<head>
  	<t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
  	<script type="text/javascript">
  		function choose(){
  			var rowData = $('#exeList').datagrid('getSelected');
  			tip("已选择");
  			frameElement.api.opener.document.getElementById("sql").innerHTML =  rowData.sql;
  			frameElement.api.close();
  		}
  	</script>
</head>

<body style="overflow-y: hidden" scroll="no">
	<div class="easyui-layout" fit="true">
		  <div region="center" style="padding:1px;">
			<t:datagrid name="exeList"  actionUrl="${basePath }/web/exeListController.do?datagrid&exeid=${exeid}" idField="id" fit="true">
			   <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
			   <t:dgCol title="exeid" field="exeid" hidden="false"></t:dgCol>
			   <t:dgCol title="sql语句" field="sql" width="50"></t:dgCol>
			   <t:dgCol title="参数 " field="params" width="30"></t:dgCol>
			   <t:dgCol title="测试时间" field="dated" width="20"></t:dgCol>
			 
			   <t:dgCol title="操作" field="opt" width="10"></t:dgCol>
			   <t:dgFunOpt title="选择" funname="choose()"></t:dgFunOpt>	
			    <t:dgDelOpt title="删除" url="${basePath }/web/exeListController.do?del&id={id}" />
			   
			  <%-- 
			   <t:dgToolBar title="录入" icon="icon-add" url="../web/exeListController.do?addorupdate" funname="add"></t:dgToolBar>
			   <t:dgToolBar title="编辑" icon="icon-edit" url="../web/exeListController.do?addorupdate" funname="update"></t:dgToolBar>
			   <t:dgToolBar title="查看" icon="icon-search" url="../web/exeListController.do?addorupdate" funname="detail"></t:dgToolBar>--%>
			  </t:datagrid>
		  </div>
	 </div>
 </body>