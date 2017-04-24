<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>自定义接口</title>
  <t:base type="jquery,easyui,tools" basePath="${basePath }"></t:base>
	<script type="text/javascript">
		var basePath="${basePath}";
		$(function() {
			$('#cc').combotree({
				url : basePath+'/web/customerInterfaceController.do?setPInterface',
			});
			if($('#methodLevel').val()=='2'){
				$('#pfun').show();
			}else{
				$('#pfun').hide();
			}
			
			$('#methodLevel').change(function(){
				if($(this).val()=='2'){
					$('#pfun').show();
					var t = $('#cc').combotree('tree');
					var nodes = t.tree('getRoots');
					for(var i=0;i<nodes.length;i++){
						$('#cc').combotree('setValue', nodes[i].id);
						break;
					}
				}else{
					var t = $('#cc').combotree('tree');
					var node = t.tree('getSelected');
					if(node){
						$('#cc').combotree('setValue', null);
					}
					$('#pfun').hide();
				}
			});
			
			$('#methodName').change(function(){
				var methodName = $('#methodName').val();
				if(methodName != null && methodName.length > 0){
					$.ajax({
						url: basePath+'/web/customerInterfaceController.do?checkMethodName&date='+new Date(),
						data:{
							'methodName':methodName 
						},
						success:function(msg){
							var data = JSON.parse(msg);
							if(data.success == true){
								document.getElementById("codeMsg").innerHTML = "<font style='color:red'>code已存在，请重新输入</font>";
								$('#id').focus();
							}else{
								document.getElementById("codeMsg").innerHTML = "通过信息验证!";
							}
						}
					});
				}
			});
		});
	</script>
 </head>
 
 <body style="overflow-y: hidden;height:250px;" scroll="no">
     <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${basePath }/web/customerInterfaceController.do?save">
			<input id="id" name="id" type="hidden" value="${customerInterfacePage.id }">
			
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right" style="width:80px;">
						<label class="Validform_label">
							名称:
						</label>
					</td>
					<td class="value">
						<input id="name" name="name" class="inputxt" value="${customerInterfacePage.name}" datatype="*" style="width:300px;">
					</td>
				</tr>
				
				<tr>
					<td align="right" style="width:80px;">
						<label class="Validform_label">
							所属控制器:
						</label>
					</td>
					<td class="value">
						<input id="controllerName" name="controllerName" class="inputxt" value="${customerInterfacePage.controllerName}" datatype="*" style="width:300px;">
					</td>
				</tr>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							方法名:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="methodName" name="methodName" datatype="*" value="${customerInterfacePage.methodName}" style="width:300px;panelHeight:auto;">
						<span id="codeMsg" class="Validform_checktip"></span>
					</td>
				</tr>

			<tr>
				<td align="right">
					<label class="Validform_label">
						接口等级:
					</label>
				</td>
				<td class="value">
					<select name="methodLevel" id="methodLevel" datatype="*" style="width:300px;">
						<option value="1"
							<c:if test="${customerInterfacePage.methodLevel eq 1}">selected="selected"</c:if>>
							一级菜单
						</option>
						<option value="2"
							<c:if test="${customerInterfacePage.methodLevel eq 2}">selected="selected"</c:if>>
							二级菜单
						</option>
					</select>
				</td>
			</tr>

			<tr id="pfun">
				<td align="right">
					<label class="Validform_label">
						父接口:
					</label>
				</td>
				<td class="value">
					<input id="cc" name="customerInterface.id" value="${customerInterfacePage.customerInterface.id}" style="width:300px;">
				</td>
			</tr>
			
			<tr>
				<td align="right">
					<label class="Validform_label">
						描述:
					</label>
				</td>
				<td class="value">
					<textarea id="descrition" name="descrition" datatype="*" style="width:300px;" rows="5">${customerInterfacePage.descrition}</textarea>
				</td>
			</tr>
		</table>
	</t:formvalid>
 </body>