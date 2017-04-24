<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:1px;">
<t:datagrid name="functionList" title="菜单管理" actionUrl="${basePath }/functionController.do?functionGrid" idField="id" treegrid="true" pagination="false">
 <t:dgCol title="编号" field="id" treefield="id" hidden="false"></t:dgCol>
 <t:dgCol title="菜单名称" field="functionName" width="100" treefield="text"></t:dgCol>
 <t:dgCol title="图标" field="TSIcon_iconPath" treefield="code" image="true"></t:dgCol>
 <t:dgCol title="菜单地址" field="functionUrl" treefield="src"></t:dgCol>
 <t:dgCol title="菜单顺序" field="functionOrder" treefield="order"></t:dgCol>
 <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
 <t:dgDelOpt url="${basePath }/functionController.do?del&id={id}" title="删除"></t:dgDelOpt>
 <%--   update-start--Author:anchao  Date:20130415 for：按钮权限控制--%>
 <t:dgFunOpt funname="operationDetail(id)" title="按钮设置"></t:dgFunOpt>
 <%--   update-end--Author:anchao  Date:20130415 for：按钮权限控制--%>
 <t:dgToolBar title="菜单录入" icon="icon-add" url="${basePath }/functionController.do?addorupdate" funname="add"></t:dgToolBar>
 <t:dgToolBar title="菜单编辑" icon="icon-edit" url="${basePath }/functionController.do?addorupdate" funname="update"></t:dgToolBar>
</t:datagrid>
</div>
<div region="east" style="width:500px; overflow: hidden;" split="true" border="false">
<div class="easyui-panel" title="操作按钮" style="padding:1px;" fit="true" border="false" id="operationDetailpanel">
  </div>
</div>
</div>
<%--   update-start--Author:anchao  Date:20130415 for：按钮权限控制--%>
<script type="text/javascript">
function operationDetail(functionId)
{
	$('#operationDetailpanel').panel("refresh", "${basePath }/functionController.do?operation&functionId=" +functionId);
}
</script>
<%--   update-end--Author:anchao  Date:20130415 for：按钮权限控制--%>
