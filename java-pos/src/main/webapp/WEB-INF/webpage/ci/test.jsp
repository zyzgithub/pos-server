<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 	<head>
  		<title>自定义接口</title>
  		<t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
  		<script type="text/javascript" src="${basePath }/plug-in/tools/json_format/json_format.js"></script>
		<link rel="stylesheet" href="${basePath }/plug-in/tools/json_format/js_format.css" type="text/css"></link>
		<style type="text/css">
			.listTab {
				width: 94%;
				border: #afccea solid;;
				border-width: 1px 1px 1px 1px;
				margin: 10px 25px 15px 20px;
				border-collapse: collapse;
				
			}
			.listTab td {
				border: #cfd3ee solid;;
				border-width: 0px 1px 1px 0px;
				height: 30px;
				line-height: 20px;
				color: #323433;
				padding: 0px 4px;
				font-size: 15px;
			}
			.fn{
				font-weight:bold;
				background-color:#F0F8FF;
			}
		</style>
		
		<script type="text/javascript">
			var basePath="${basePath}";
			//添加请求参数
			function addParam(){
				var tr = "<tr><td><input type='text' id='newParamName' style='width:100%;'></td><td><input style='width:100%;' type='text' id='newParamComment'></td><td><input type='text' id='newParamType' style='width:100%;'></td><td>&nbsp;</td></tr>";
				$('#paramTable tr:eq(-1)').before(tr);
				$('#addBtn').hide();
				$('#saveBtn').show();
				$('#updateBtn').hide();
				$('#cancelBtn').show();
			}
			
			//保存添加请求参数
			function saveReqParam(type){
				var name;
				var comment;
				var dataType;
				var ciId;
				
				name = $('#newParamName').val();
				comment = $('#newParamComment').val();
				dataType = $('#newParamType').val();
					
				name = name.replace(/(^\s*)|(\s*$)/g, "");
				if(name == null || name.length < 1){
					tip("参数名称不能为空");
					return;
				}
				ciId = $("#ciId").val();
				
				$.ajax({
					url:basePath+'/web/customerInterfaceController.do?saveNewParam&date=' + new Date(),
					data:{
						name: name,
						comment: comment,
						dataType: dataType,
						ciId:ciId,
					},
					success:function(data){
						tip(data.msg);
						window.location.reload();
					}
				});
			}
			
			//删除参数 
			function deleteParam(id){
					$.ajax({
					url:basePath+'/web/customerInterfaceController.do?deleteParam&date=' + new Date(),
					data:{
						id: id
					},
					success:function(data){
						tip(data.msg);
						window.location.reload();
					}
				});
			}
			
			
			//更新参数
			function updateComment(id){
				if($('#saveEditBtn').css("display") != "none"){
					tip("正在修改中，请保存后再修改");
					return;
				}
				
				$("#"+id+"_name_text").hide();
				$("#"+id+"_name_input").show();
				$("#"+id+"_descrition_text").hide();
				$("#"+id+"_descrition_input").show();
				$("#"+id+"_type_text").hide();
				$("#"+id+"_type_input").show();
				
				$('#addBtn').hide();
				$('#saveEditBtn').show();
				$('#cancelBtn').show();
				
				$('#editId').val(id);
			}
			
			//保存编辑的参数
			function saveEditParam(){
				var id;
				var name;
				var descrition;
				var dataType;
				
				id = $('#editId').val();
				name = $("#"+id+"_name").val();
				descrition = $("#"+id+"_descrition").val();
				dataType = $("#"+id+"_type").val();

				$.ajax({
					url:basePath+'/web/customerInterfaceController.do?saveEditParam&date='+new Date(),
					data:{
						name : name,
						descrition: descrition,
						dataType: dataType,
						id:id
					},
					success:function(msg){
						$('#editId').val('');
						//var d = $.parseJSON(msg);
						var d = msg;
						tip(d.msg);
						window.location.reload();
					}
				});
			}
			
			//查看结果
			function doSearch(){
				var params = "";
				var paramNames = $('#paramNames').val().split(",");
				if(paramNames != null && paramNames.length > 0){
					for(var i = 0; i < paramNames.length; i++){
						var s = paramNames[i];
						if(s != null && s != ""){
							var val = $('#'+s).val();
							params += ("&"+s+"="+val);
						}
					}
				}
				
				var url = basePath+'/${ci.controllerName}.do?' + '${ci.methodName}' + params+'&register=true';
				$.ajax({
					url: url,
					contentType: "application/x-www-form-urlencoded; charset=utf-8", 
					success:function(data){
						$('#resultTr').show();
						Process(data,"result");
					}
				});
			}
			
			//重置参数
			function reSet(){
				var params = "";
				var paramNames = $('#paramNames').val().split(",");
				if(paramNames != null && paramNames.length > 0){
					for(var i = 0; i < paramNames.length; i++){
						var s = paramNames[i];
						if(s != null && s != ""){
							$('#'+s).val('');
						}
					}
				}
				$('#resultTr').hide();
				$('#result').html('');
			}
			
			function submitTest(){
		        if (event.keyCode==13){   //回车键的键值为13  
		        	doSearch();
		        }  
			}
		</script>
 	</head>
 
 	<body onkeydown="submitTest();">
 		<input type="hidden" id="editId" name="editId">
 		<input type="hidden" id="paramNames" value="${paramNames}"/>
 		<input type="hidden" id="ciId" name="ciId" value="${ci.id}">
 		
		<table cellpadding="0" cellspacing="0" width="70%" border=1 class="listTab">
			<tr align="center">
				<td colspan="6" style="font-size:18px;font-weight:bold;color:#6495ED">${ci.name}</td>
			</tr>
			<tr>
				<td class="fn" style="width:100px;">方法名称：</td>
				<td>${ci.methodName}</td>
				<td class="fn" style="width:100px;">所属控制器：</td>
				<td>${ci.controllerName}</td>
			</tr>
			<tr>
				<td class="fn" style="width:100px;">描述：</td>
				<td colspan="6">${ci.descrition}</td>
			</tr>
			<tr>
				<td class="fn">URL格式：</td>
				<td colspan="6" style="color:#FF6347;font-weight:bold;font-size:16px;">${basePath}/${ci.controllerName}.do?${ci.methodName}&参数列表</td>
			</tr>
			<tr>
				<td class="fn">请求参数</td>
				<td colspan="6">
		           <table style="width:100%" id="paramTable">
		           		<tr>
		           			<td class="fn" style="width:20%" align="center">参数名</td>
		           			<td class="fn" style="width:40%" align="center">描述</td>
		           			<td class="fn" style="width:20%" align="center">参数类型</td>
		           			<td class="fn" style="width:10%" align="center">操作</td>
		           		</tr>
		           		
		           		<c:forEach  items="${paramList}" var="vo" varStatus="status">
			           		<tr>
			           			<td style="color:#1C86EE;font-weight:bold;">
			           				<div id="${vo.id}_name_text" style="font-size:15px;">${vo.paramName}</div>
			           				<div id="${vo.id}_name_input" style="display:none;">
			           					<input type="text" id="${vo.id}_name" value="${vo.paramName}" style="width:100%">
			           				</div>
			           			</td>
			           			<td style="font-weight:bold;" align="center">
			           				<div id="${vo.id}_descrition_text" style="font-size:15px;">${vo.descrition}</div>
			           				<div id="${vo.id}_descrition_input" style="display:none;">
			           					<input type="text" id="${vo.id}_descrition" value="${vo.descrition}" style="width:100%">
			           				</div>
			           			</td>
			           			<td style="font-weight:bold;" align="center">
			           				<div id="${vo.id}_type_text" style="font-size:15px;">${vo.paramType}</div>
			           				<div id="${vo.id}_type_input" style="display:none;">
			           					<input type="text" id="${vo.id}_type" value="${vo.paramType}" style="width:100%">
			           				</div>
			           			</td>
			           			<td align="center">
			           				<a href="#" onclick="updateComment('${vo.id}');">修改</a>&nbsp;
			           				<a href="#" onclick="deleteParam('${vo.id}')">删除</a>
			           			</td>
			           		</tr>
		           		</c:forEach>
		           		
		           		<tr>
		           			<td colspan="7" align="right">
		           				<a id="addBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addParam();">添加</a>
		           				<a id="saveEditBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveEditParam();" style="display:none;">保存</a>
		           				<a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveReqParam();" style="display:none;">保存</a>
		           				<a id="cancelBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="window.location.reload();" style="display:none;">取消</a>
		           			</td>
		           		</tr>
		           </table>
				</td>
			</tr>
			<tr><td colspan="6">&nbsp;</td></tr>
			
			<tr>
				<td class="fn">测试</td>
				<td colspan="6">
					<table style="width:100%">
       					<c:forEach  items="${paramList}" var="vo" varStatus="status">
       						<c:if test="${status.index % 2 == 0}">
       							<tr>
       							<td>${vo.paramName}：</td><td><input type="text" id="${vo.paramName}"></td>
       						</c:if>
       						
       						<c:if test="${status.index % 2 != 0}">
       							<td>${vo.paramName}：</td><td><input type="text" id="${vo.paramName}"></td>
       							</tr>
       						</c:if>
       					</c:forEach>
       				</table>
				</td>
			</tr>
			<tr id="resultTr" style="display:none;">
				<td class="fn">结果:</td>
				<td colspan="6">
					<div id="result"></div>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<a id="serachResult" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch();">查看结果</a>&nbsp;&nbsp;
					<a id="serachResult" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="reSet();">重置</a>
				</td>
			</tr>
 	</body>