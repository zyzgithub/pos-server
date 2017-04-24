<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>test的URL</title>
  <t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${basePath }/web/testURLController.do?save">
			<input id="id" name="id" type="hidden" value="${testURLPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							url:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="url" name="url" ignore="ignore"
							   value="${testURLPage.url}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							dated:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="dated" name="dated" ignore="ignore"
							   value="<fmt:formatDate value='${testURLPage.dated}' type="date" pattern="yyyy-MM-dd"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							exeid:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="exeid" name="exeid" ignore="ignore"
							   value="${testURLPage.exeid}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>