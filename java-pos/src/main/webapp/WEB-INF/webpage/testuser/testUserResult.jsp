<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>真机测试结果</title>
  <t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${basePath }/web/testUserResultController.do?save">
			<input id="id" name="id" type="hidden" value="${testUserResultPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right" style="width:100px;">
						<label class="Validform_label">
							接口ID:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="exeid" name="exeid" style="width:400px;" value="${testUserResultPage.exeid}" disabled="disabled">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							输入参数:
						</label>
					</td>
					<td class="value">
						<textarea class="inputxt" id="params" name="params" style="width:400px;height:100px;" disabled="disabled">${testUserResultPage.params}</textarea>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							结果:
						</label>
					</td>
					<td class="value">
						<textarea class="inputxt" id="result" name="result" disabled="disabled" style="width:400px;height:100px;">${testUserResultPage.result}</textarea>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							操作时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" style="width: 400px" id="dated" name="dated" disabled="disabled"
							     value="<fmt:formatDate value='${testUserResultPage.dated}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>