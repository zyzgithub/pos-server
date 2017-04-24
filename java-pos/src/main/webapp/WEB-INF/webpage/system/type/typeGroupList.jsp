<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:1px;">
<t:datagrid name="typeGroupList" title="类型分组" actionUrl="${basePath }/systemController.do?typeGroupGrid" idField="id">
 <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
 <t:dgCol title="分组名称" field="typegroupname" width="100"></t:dgCol>
 <t:dgCol title="分组编码" field="typegroupcode"></t:dgCol>
 <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
 <t:dgDelOpt url="${basePath }/systemController.do?delTypeGroup&id={id}" title="删除"></t:dgDelOpt>
 <t:dgToolBar title="分组录入" icon="icon-add" url="${basePath }/systemController.do?aouTypeGroup" funname="add"></t:dgToolBar>
 <t:dgToolBar title="分组编辑" icon="icon-edit" url="${basePath }/systemController.do?aouTypeGroup" funname="update"></t:dgToolBar>
</t:datagrid>
</div>
</div>