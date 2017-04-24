<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>操作日志</title>
  <t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
  <script type="text/javascript" src="${basePath }/plug-in/tools/json_format/json_format.js"></script>
  <link rel="stylesheet" href="${basePath }/plug-in/tools/json_format/js_format.css" type="text/css"></link>
  
  <script type="text/javascript">
	$(function(){
		Process("${operateLogPage.params}","paramJson");
		Process("${operateLogPage.operateResult}","resultJson");
	});  
  
  </script>
 </head>
 
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${basePath }/web/operateLogController.do?save">
			<input id="id" name="id" type="hidden" value="${operateLogPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right" style="width:80px;">
						<label class="Validform_label">
							接口ID:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="exeid" name="exeid" ignore="ignore" disabled="disabled"
							   value="${operateLogPage.exeid}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							请求参数:
						</label>
					</td>
					<td class="value">
						<div id="paramJson" style="width:500px;overflow: auto;height:100px;"></div>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							返回结果:
						</label>
					</td>
					<td class="value">
						<div id="resultJson" style="overflow: auto;width:500px;height:250px;"></div>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							操作用户:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="userid" name="userid" ignore="ignore" disabled="disabled"
							   value="${operateLogPage.userid}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							日期:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="operateDate" disabled="disabled" name="operateDate" ignore="ignore"
							     value="<fmt:formatDate value='${operateLogPage.operateDate}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							操作类型:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="operateType" name="operateType" ignore="ignore" disabled="disabled"
							   value="${operateLogPage.operateType}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>