<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript">
	var basePath="${basePath}";
	$(function(){
		$("input[name='operateDate_begin']").attr("class","easyui-datebox");
		$("input[name='operateDate_end']").attr("class","easyui-datebox");
	});
	
	function serachResult(){
		var row = $('#operateLogList').datagrid('getSelected');
		var addurl = basePath+"/web/operateLogController.do?addorupdate&id="+row.id+"&date="+new Date();
		$.dialog({
			content: 'url:'+addurl,
			title: '日志信息',
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}
</script>


<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="operateLogList" title="操作日志" actionUrl="${basePath}/web/operateLogController.do?datagrid" onClick="serachResult();" idField="id" fit="true" queryMode="group" pageSize="20">
   <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
   <t:dgCol title="操作用户" field="userid" query="true" width="1"></t:dgCol>
   <t:dgCol title="接口ID" field="exeid" query="true" width="1"></t:dgCol>
   <t:dgCol title="请求参数" field="params" width="3"></t:dgCol>
   <t:dgCol title="返回结果" field="operateResult" width="4"></t:dgCol>
   <t:dgCol title="日期" field="operateDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" width="2" queryMode="group"></t:dgCol>
   <t:dgCol title="操作类型" field="operateType" replace="query_query,execute_execute,login_login" query="true" width="1"></t:dgCol>
 <%--
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="operateLogController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="operateLogController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="operateLogController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="operateLogController.do?addorupdate" funname="detail"></t:dgToolBar>
   --%>
  </t:datagrid>
  </div>
 </div>