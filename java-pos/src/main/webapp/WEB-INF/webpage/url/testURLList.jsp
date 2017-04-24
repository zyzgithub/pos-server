<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<!DOCTYPE html>
<html>
<head>
  	<t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
  	<script type="text/javascript">
  		function choose(){
  			var rowData = $('#testURLList').datagrid('getSelected');
  			frameElement.api.opener.document.getElementById("url").innerHTML =  rowData.url;
  			frameElement.api.close();
  		}
  	</script>
</head>

<body style="overflow-y: hidden" scroll="no">
	<div class="easyui-layout" fit="true">
	  <div region="center" style="padding:1px;">
	  <t:datagrid name="testURLList" actionUrl="${basePath }/web/testURLController.do?datagrid&exeid=${exeid}" idField="id" fit="true">
	   <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
	   <t:dgCol title="url" field="url" width="100"></t:dgCol>
	   <t:dgCol title="dated" field="dated" formatter="yyyy-MM-dd hh:mm:ss" width="30"></t:dgCol>
	   <%--<t:dgCol title="exeid" field="exeid" ></t:dgCol>--%>
	   <t:dgCol title="操作" field="opt" width="20"></t:dgCol>
	   <t:dgFunOpt title="选择" funname="choose()"></t:dgFunOpt>	
	   <t:dgDelOpt title="删除" url="${basePath }/web/testURLController.do?del&id={id}" /><%--
	   <t:dgToolBar title="录入" icon="icon-add" url="${basePath }/web/testURLController.do?addorupdate" funname="add"></t:dgToolBar>
	   <t:dgToolBar title="编辑" icon="icon-edit" url="${basePath }/web/testURLController.do?addorupdate" funname="update"></t:dgToolBar>
	   <t:dgToolBar title="查看" icon="icon-search" url="${basePath }/web/testURLController.do?addorupdate" funname="detail"></t:dgToolBar>
	  --%>
	  </t:datagrid>
	  </div>
	 </div>
 </body>