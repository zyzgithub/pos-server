<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<title>接口信息</title>
		<t:base type="jquery,easyui,tools" basePath="${basePath }"></t:base>
		<script type="text/javascript" src="${basePath }/plug-in/tools/json_format/json_format.js"></script>
		<link rel="stylesheet" href="${basePath }/plug-in/tools/json_format/js_format.css" type="text/css"></link>
		<script type="text/javascript">
			var basePath="${basePath }";
			$(function(){
				setInterval(refresh,15000);
			});
			
			function refresh(){
		        $('#testUserResultList').datagrid('reload');
			}
		
			function serach(id){
				add('运行信息',basePath+'/web/testUserResultController.do?addorupdate&id='+id);
			}
			
			function serachResult(){
				var row = $('#testUserResultList').datagrid('getSelected');
				$.ajax({
					url: basePath+'/web/testUserResultController.do?getResult&id='+ row.id + '&date=' + new Date(),
					success:function(d){
						var data = JSON.parse(d);
						var obj = eval('(' + d + ')');
						Process(obj,"resultDiv");
					}
				});
			}
		</script>
	</head>
	
<body>
	<div class="easyui-layout" fit="true">
	  <div region="north"  title="接口列表" style="padding:1px;height:300px;" overflow: hidden;" split="true">
		  <t:datagrid pageSize="20" onClick="serachResult();" name="testUserResultList"  actionUrl="${basePath }/web/testUserResultController.do?datagrid&sessionkey=${sessionkey}" idField="id" fit="true">
		   <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
		   <t:dgCol title="接口ID" field="exeid" width="50"></t:dgCol>
		   <t:dgCol title="请求参数" field="params" width="200"></t:dgCol>
		   <t:dgCol title="结果" field="result" hidden="false"></t:dgCol>
		   <t:dgCol title="sessionkey" field="sessionKey" hidden="false"></t:dgCol>
		   <t:dgCol title="运行时间" field="dated" formatter="yyyy-MM-dd hh:mm:ss" width="50"></t:dgCol>
		   <t:dgCol title="操作" field="opt" width="50"></t:dgCol>
		   <t:dgFunOpt title="查看" funname="serach(id)"></t:dgFunOpt>
		 </t:datagrid>
	  </div>
	  
	  <div region="center" title="输出结果" overflow: hidden;" split="true" >
		<div class="easyui-panel" style="padding:1px;" fit="true" border="false" id="resultDiv"></div>
	</div>
 </body>