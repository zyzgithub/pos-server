<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>测试记录</title>
  <t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${basePath }/web/exeListController.do?save">
			<input id="id" name="id" type="hidden" value="${exeListPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							exeid:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="exeid" name="exeid" 
							   value="${exeListPage.exeid}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							sql:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="sql" name="sql" 
							   value="${exeListPage.sql}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>