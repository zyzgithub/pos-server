<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<title>接口调试</title>
		<t:base type="jquery,easyui,tools" basePath="${basePath }"></t:base>
		<script type="text/javascript" src="${basePath }/plug-in/tools/json_format/json_format.js"></script>
		<link rel="stylesheet" href="${basePath }/plug-in/tools/json_format/js_format.css" type="text/css"></link>
		
		<script type="text/javascript">
			var basePath="${basePath}";
			//组装参数
			function assembleParams(){
				var paramName = $('#paramName').val().split(",");
				var str = "";
		
				for(var i = 0; i < paramName.length; i++){
					var id = paramName[i];
					id =  id.replace(/(^\s*)|(\s*$)/g, "");
					
					if(id != null && id.length > 0){
						if($("#"+id).val() != null && $("#"+id).val().length > 0){
							str = str + "\'"+ id + "\':\'"+$("#"+id).val() + "\',";
						}
					}
				}
				if(str.length > 0){
					str = str.substring(0,str.length - 1);
				}
				return str;
			}
		
			//参数测试提交 
			function dosubmit(){
				var ids = $("#ids").val();
				var str = assembleParams();
				
				$.ajax({
					url:basePath+'/web/execontentController.do?executeTest&ids='+ ids +'&params='+ str + '&date=' + new Date(),						
					success:function(data){
						var msg = JSON.parse(data);
					
						tip(msg.msg);
						$('#paramresultTr').show();
						Process(data,"paramResult");
						
						$('#paramSqlTr').show();
						document.getElementById('executeSql').innerHTML = msg.sql;
					}
				});
			}
			
			//重置参数
			function reSet(){
				var paramName = $('#paramName').val();
				var array;
				if(paramName != null && paramName.length > 0){
					array = paramName.split(",");
					if(array != null){
						for(var i = 0; i < array.length; i++){
							$("#"+array[i]).val('');
						}
					}
				}
				
				document.getElementById('paramResult').innerHTML = "";
				$('#paramResultTr').hide();
				
				document.getElementById('executeSql').innerHTML = "";
				$('#paramSqlTr').hide();
			}
			
			//打开sql历史记录对话框
			function openDialog(){
				var id = $("#ids").val();
				$.dialog({
					content: 'url:'+ basePath + "/VCIS/web/exeListController.do?exeList&exeid="+ id, 
					lock : true, 
					title:'sql历史记录',
					opacity : 0.3,
					width:800,
					height:400,
					cache:false
				}); 
			}
			
			//更新参数
			function updateComment(id,type){
				if(type == 0){
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
					$("#"+id+"_cannull_text").hide();
					$("#"+id+"_cannull_select").show();
					
					$('#addBtn').hide();
					$('#saveEditBtn').show();
					$('#cancelBtn').show();
					
					$('#editId').val(id);
				}
				if(type == 1){
					if($('#saveEditBtn1').css("display") != "none"){
						tip("正在修改中，请保存后再修改");
						return;
					}
					
					$("#"+id+"_name_text1").hide();
					$("#"+id+"_name_input1").show();
					$("#"+id+"_descrition_text1").hide();
					$("#"+id+"_descrition_input1").show();
					$("#"+id+"_type_text1").hide();
					$("#"+id+"_type_input1").show();
					$("#"+id+"_cannull_text1").hide();
					$("#"+id+"_cannull_select1").show();
					
					$('#addBtn1').hide();
					$('#saveEditBtn1').show();
					$('#cancelBtn1').show();
					
					$('#editId1').val(id);
				}
			}
			
			//保存编辑的参数
			function saveEditParam(type){
				var id;
				var name;
				var descrition;
				var dataType;
				var cannull;
				
				if(type == 0){
					id = $('#editId').val();
					name = $("#"+id+"_name").val();
					descrition = $("#"+id+"_descrition").val();
					dataType = $("#"+id+"_type").val();
					cannull = $("#"+id+"_cannull").val();
				}
				
				if(type == 1){
					id = $('#editId1').val();
					name = $("#"+id+"_name1").val();
					descrition = $("#"+id+"_descrition1").val();
					dataType = $("#"+id+"_type1").val();
					cannull = $("#"+id+"_cannull1").val();
				}

				$.ajax({
					url:basePath+'/web/execontentController.do?saveEditParam&date='+new Date(),
					data:{
						name : name,
						descrition: descrition,
						dataType: dataType,
						cannull: cannull,
						id:id
					},
					success:function(msg){
						$('#editId').val('');
						$('#editId1').val('');
						//var d = $.parseJSON(msg);
						var d = msg;
						tip(d.msg);
						window.location.reload();
					}
				});
			}
			
			//保存sql语句
			function saveSql(){
				var sql = document.getElementById('executeSql').innerHTML;
				sql = sql.replace(new RegExp('(["\"])', 'g'),"\\\'"); 
				var params = assembleParams();
				var id = $("#ids").val();

				$.ajax({
					url:basePath+'/web/exeListController.do?save&date='+new Date(),
					data:{
						'sql': sql,
						'params': params,
						'exeid':id
					},
					success:function(msg){
						//var d = $.parseJSON(msg);
						var d = msg;
						tip(d.msg);
					}
				});
			}
			
			//添加请求参数
			function addParam(type){
				if(type == 0){
					var tr = "<tr><td><input type='text' id='newParamName' style='width:100%;'></td><td><input style='width:100%;' type='text' id='newParamComment'></td><td><select id='cannull'><option value='0'>可空</option><option value='1'>非空</option></select></td><td><input type='text' id='newParamType' style='width:100%;'></td><td>&nbsp;</td></tr>";
					$('#paramTable tr:eq(-1)').before(tr);
					$('#addBtn').hide();
					$('#saveBtn').show();
					$('#updateBtn').hide();
					$('#cancelBtn').show();
				}
				if(type == 1){
					var tr = "<tr><td><input type='text' id='newParamName1' style='width:100%;'></td><td><input style='width:100%;' type='text' id='newParamComment1'></td><td><select id='cannull1'><option value='0'>可空</option><option value='1'>非空</option></select></td><td><input type='text' id='newParamType1' style='width:100%;'></td><td>&nbsp;</td></tr>";
					$('#responParamTable tr:eq(-1)').before(tr);
					$('#addBtn1').hide();
					$('#saveBtn1').show();
					$('#updateBtn1').hide();
					$('#cancelBtn1').show();
				}
			}
			
			//保存添加请求参数
			function saveReqParam(type){
				var name;
				var comment;
				var cannull;
				var dataType;
				var exeid;
				var paramType;
				
				if(type == 0){
					name = $('#newParamName').val();
					comment = $('#newParamComment').val();
					cannull = $('#cannull').val();
					dataType = $('#newParamType').val();
					paramType = 0;
				}
				
				if(type == 1){
					name = $('#newParamName1').val();
					comment = $('#newParamComment1').val();
					cannull = $('#cannull1').val();
					dataType = $('#newParamType1').val();
					paramType = 1; 
				}
				
				name = name.replace(/(^\s*)|(\s*$)/g, "");
				if(name == null || name.length < 1){
					tip("参数名称不能为空");
					return;
				}
				exeid = $("#ids").val();
				
				$.ajax({
					url:basePath+'/web/execontentController.do?saveNewParam&date=' + new Date(),
					data:{
						name: name,
						comment: comment,
						dataType: dataType,
						cannull:cannull,
						exeid:exeid,
						paramType: paramType
					},
					success:function(msg){
						var data = JSON.parse(msg);
						tip(data.msg);
						window.location.reload();
					}
				});
			}
			
			//删除参数 
			function deleteParam(id){
					$.ajax({
					url:basePath+'/web/execontentController.do?deleteParam&date=' + new Date(),
					data:{
						id: id
					},
					success:function(msg){
						var data = JSON.parse(msg);
						tip(data.msg);
						window.location.reload();
					}
				});
			}
			
			//选择测试的类型
			function chooseTestType(){
				var type = document.getElementsByName("testType");
				for (var i = 0; i < type.length; i++){
					if(type[i].checked){
						if(type[i].value == "paramtest"){
							$('tr[name=paramtest]').show();
							$('#paramresultTr').show();
						}
						if(type[i].value == "sqledit"){
							$('tr[name=editSql]').show();
							$('#sqlResult').show();
						}
					}
					
					if(!type[i].checked){
						if(type[i].value == "paramtest"){
							$('tr[name=paramtest]').hide();
							$('#paramresultTr').hide();  
							$('#paramSqlTr').hide();	
						}
						if(type[i].value == "sqledit"){
							$('tr[name=editSql]').hide();  
							$('tr[name=sqlResultTr]').hide();
							$('#sqlResult').hide();
						}
					}
				}
			}
			
			//sql测试提交 
			function sqlTestSubmit(){
				var sql = $("#sql").val();
				sql = sql.replace(/(^\s*)|(\s*$)/g, "");
				if(sql == null || sql.length < 1){
					tip("请编写sql后再提交");
					return;
				}
				
				$.ajax({
					url:basePath+'/web/execontentController.do?sqlTest&sql='+ sql + '&date=' + new Date(),
					success:function(data){
						var msg = JSON.parse(data);
						if(msg.success == true){
							$('#sqlResultTr').show();
							$('#sqlResult').show();
							Process(data,"sqlResult");
						//	document.getElementById('sqlResult').innerHTML = data;
						}
						tip(msg.msg);
					}
				});
			}
			
			//重置参数
			function sqlTestReSet(){
				$('#sql').val('');
				document.getElementById('sqlResultTr').innerHTML = "";
				$('#sqlResultTr').hide();
			}
			
			//保存编写的sql语句
			function saveEditSql(){
				var sql = $('#sql').val();
				var id = $("#ids").val();
				
				sql = sql.replace(/(^\s*)|(\s*$)/g, "");
				if(sql == null || sql.length < 1){
					tip("请编写sql后再提交");
					return;
				}
				
				$.ajax({
					url:basePath+'/web/exeListController.do?save&date='+new Date(),
					data:{
						'sql': sql,
						'exeid':id
					},
					success:function(msg){
						//var d = $.parseJSON(msg);
						var d = msg;
						tip(d.msg);
					}
				});
			}
			
			//提交url测试语句
			function testUrl(){
				var url = $("#url").val();
				url = url.replace(/(^\s*)|(\s*$)/g, "");
				if(url == null || url.length < 1){
					tip("请编写url后再提交");
					return;
				}

				$.ajax({
					url:basePath+'/web/execontentController.do?urlTest&date='+new Date(),
					data:{
						'url': url,
					},
					success:function(msg){
						var d = msg;
						tip(d.msg);
						if(d.success == true){
							$('#urlResultTr').show();  
						//	document.getElementById('urlResult').innerHTML = msg;
							Process(msg,"urlResult");
						}
					}
				});
			}
			
			function resetUrl(){
				$('#url').val('');
				document.getElementById('urlResult').innerHTML = "";
				$('#urlResultTr').hide();
			}
			
			//打开url历史记录对话框
			function openUrlDialog(){
				var id = $("#ids").val();
				
				document.getElementById('urlResult').innerHTML = "";
				$('#urlResultTr').hide();
				
				$.dialog({
					content: 'url:'+ basePath+"/VCIS/web/testURLController.do?testURL&exeid="+ id, 
					lock : true, 
					title:'url历史记录',
					opacity : 0.3,
					width:800,
					height:400,
					cache:false
				}); 
			}
			
			//保存编写的url语句
			function saveUrl(){
				var url = $('#url').val();
				var id = $("#ids").val();
				
				url = url.replace(/(^\s*)|(\s*$)/g, "");
				if(url == null || url.length < 1){
					tip("请编写url后再提交");
					return;
				}

				$.ajax({
					url:basePath+'/web/testURLController.do?save&date='+new Date(),
					data:{
						'url': url,
						'exeid':id
					},
					success:function(msg){
						var d = msg;
						tip(d.msg);
					}
				});
			}
			
			function urlChange(){
				var url = $('#url').val();
				if(url != null && url.length > 0){
					$('#saveUrlBtn').show();
					$('#exeUrlBtn').show();
				}
			}
		</script>
		
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
	 </head>
	 
	<body>
		<input type="hidden" id="paramName" name="paramName" value="${sqlparamstr}">
		<input type="hidden" id="ids" name="ids" value="${execontent.id}">
		<input type="hidden" id="tableName" name="tableName" value="${tableName}">
		<input type="hidden" id="editId" name="editId">
		<input type="hidden" id="editId1" name="editId1">
		
		<table cellpadding="0" cellspacing="0" width="100%" border=1 class="listTab">
			<tr align="center">
				<td colspan="6" style="font-size:18px;font-weight:bold;color:#6495ED">${execontent.name}</td>
			</tr>
			<tr>
				<td class="fn" style="width:120px;">id：</td>
				<td id="id" style="width:220px">${execontent.id}</td>
				<td class="fn" style="width:120px;">正确提示信息：</td>
				<td style="width:220px">${execontent.message}</td>
				<td class="fn" style="width:120px">错误提示信息：</td>
				<td style="width:auto" ><div style="color:red;font-weight:bold;font-size: 15px;">${execontent.errorMessage}</div></td>
			</tr>
			
			<tr>
				<td class="fn" >类型：</td>
				<td>${execontent.type}</td>
				<td class="fn">描述：</td>
				<td colspan="3">${execontent.descrition}</td>
			</tr>
			
			<tr>
				<td class="fn">SQL语句</td>
				<td colspan="6"><div style="width:930px;overflow:auto;padding-top:8px;padding-bottom:8px;font-size:18px;font-weight:bold;color:#FF8C00;">${execontent.sqlStatement}</div></td>
			</tr>
			
			<c:if test="${gap != '0'}">
				<tr>
					<td class="fn">系统提示</td>
					<td colspan="5">接口共需有  <span style="color:#00CD00;font-weight:bold;font-size:16px;">${paramListsize}</span> 个参数，请求参数共有  <span style="color:#00CED1;font-weight:bold;font-size:16px;">${fn:length(reqParmList)}</span> 个参数，相差  <span style="color:red;font-weight:bold;font-size:16px;">${gap}</span> 个参数</td>			
				</tr>  
			</c:if>
			
			<tr>
				<td class="fn">请求参数(${fn:length(reqParmList)}个)</td>
				<td colspan="6">
		           <table style="width:100%" id="paramTable">
		           		<tr>
		           			<td class="fn" style="width:20%" align="center">参数名</td>
		           			<td class="fn" style="width:40%" align="center">描述</td>
		           			<td class="fn" style="width:10%" align="center">是否可空</td>
		           			<td class="fn" style="width:20%" align="center">参数类型</td>
		           			<td class="fn" style="width:10%" align="center">操作</td>
		           		</tr>
		           		
		           		<c:forEach  items="${reqParmList}" var="vo" varStatus="status">
			           		<tr>
			           			<td style="color:#1C86EE;font-weight:bold;">
			           				<div id="${vo.id}_name_text" style="font-size:15px;">${vo.name}</div>
			           				<div id="${vo.id}_name_input" style="display:none;">
			           					<input type="text" id="${vo.id}_name" value="${vo.name}" style="width:100%">
			           				</div>
			           			</td>
			           			<td style="font-weight:bold;" align="center">
			           				<div id="${vo.id}_descrition_text" style="font-size:15px;">${vo.descrition}</div>
			           				<div id="${vo.id}_descrition_input" style="display:none;">
			           					<input type="text" id="${vo.id}_descrition" value="${vo.descrition}" style="width:100%">
			           				</div>
			           			</td>
			           			<td align="center">
			           				<div id="${vo.id}_cannull_text" style="font-size:15px;"><c:if test="${vo.cannull eq '0'}">可空</c:if><c:if test="${vo.cannull eq '1'}"><div style="color:red;font-weight:bold;font-size:15px;">非空</div></c:if></div>
									<div id="${vo.id}_cannull_select" style="display:none;width:100%">
										<select id="${vo.id}_cannull">
											<option value="1" <c:if test="${vo.cannull eq '1'}">selected="selected"</c:if>>非空</option>
											<option value="0" <c:if test="${vo.cannull eq '0'}">selected="selected"</c:if>>可空</option>
										</select>
									</div>
			           			</td>
			           			<td style="font-weight:bold;" align="center">
			           				<div id="${vo.id}_type_text" style="font-size:15px;">${vo.dataType}</div>
			           				<div id="${vo.id}_type_input" style="display:none;">
			           					<input type="text" id="${vo.id}_type" value="${vo.dataType}" style="width:100%">
			           				</div>
			           			</td>
			           			<td align="center">
			           				<a href="#" onclick="updateComment('${vo.id}',0);">修改</a>&nbsp;
			           				<a href="#" onclick="deleteParam('${vo.id}')">删除</a>
			           			</td>
			           		</tr>
		           		</c:forEach>
		           		
		           		<tr>
		           			<td colspan="7" align="right">
		           				<a id="addBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addParam(0);">添加</a>
		           				<a id="saveEditBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveEditParam(0);" style="display:none;">保存</a>
		           				<a id="saveBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveReqParam(0);" style="display:none;">保存</a>
		           				<a id="cancelBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="window.location.reload();" style="display:none;">取消</a>
		           			</td>
		           		</tr>
		           </table>
				</td>
			</tr>
			
			<c:if test="${execontent.type eq 'query'}">
				<tr>
					<td class="fn">返回参数</td>
					<td colspan="6">
			           <table style="width:100%" id="responParamTable">
			           		<tr>
			           			<td class="fn" style="width:20%" align="center">参数名</td>
			           			<td class="fn" style="width:40%" align="center">描述</td>
			           			<td class="fn" style="width:10%" align="center">是否可空</td>
			           			<td class="fn" style="width:20%" align="center">参数类型</td>
			           			<td class="fn" style="width:10%" align="center">操作</td>
			           		</tr>
			           		
			           		<c:forEach  items="${responseParamList}" var="vo" varStatus="status">
				           		<tr>
				           			<td style="color:#1C86EE;font-weight:bold;">
				           				<div id="${vo.id}_name_text1" style="font-size:15px;">${vo.name}</div>
				           				<div id="${vo.id}_name_input1" style="display:none;">
				           					<input type="text" id="${vo.id}_name1" value="${vo.name}" style="width:100%">
				           				</div>
				           			</td>
				           			<td style="font-weight:bold;" align="center">
				           				<div id="${vo.id}_descrition_text1" style="font-size:15px;">${vo.descrition}</div>
				           				<div id="${vo.id}_descrition_input1" style="display:none;">
				           					<input type="text" id="${vo.id}_descrition1" value="${vo.descrition}" style="width:100%">
				           				</div>
				           			</td>
				           			<td align="center">
				           				<div id="${vo.id}_cannull_text1" style="font-size:15px;"><c:if test="${vo.cannull eq '0'}">可空</c:if><c:if test="${vo.cannull eq '1'}"><div style="color:red;font-weight:bold;font-size:15px;">非空</div></c:if></div>
										<div id="${vo.id}_cannull_select1" style="display:none;width:100%">
											<select id="${vo.id}_cannull1">
												<option value="0">可空</option>
												<option value="1">非空</option>
											</select>
										</div>
				           			</td>
				           			<td style="font-weight:bold;" align="center">
				           				<div id="${vo.id}_type_text1" style="font-size:15px;">${vo.dataType}</div>
				           				<div id="${vo.id}_type_input1" style="display:none;">
				           					<input type="text" id="${vo.id}_type1" value="${vo.dataType}" style="width:100%">
				           				</div>
				           			</td>
				           			<td align="center">
				           				<a href="#" onclick="updateComment('${vo.id}',1);">修改</a>&nbsp;
				           				<a href="#" onclick="deleteParam('${vo.id}')">删除</a>
				           			</td>
				           		</tr>
			           		</c:forEach>
			           		
			           		<tr>
			           			<td colspan="7" align="right">
			           				<a id="addBtn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addParam(1);">添加</a>
			           				<a id="saveEditBtn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveEditParam(1);" style="display:none;">保存</a>
			           				<a id="saveBtn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveReqParam(1);" style="display:none;">保存</a>
			           				<a id="cancelBtn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="window.location.reload();" style="display:none;">取消</a>
			           			</td>
			           		</tr>
			           </table>
					</td>
				</tr>
			</c:if>
			
			<tr><td colspan="6">&nbsp;</td></tr>
			
			<tr>
				<td class="fn">
					 测试<br><br>
					<input type="radio" name="testType" value ="paramtest" checked="checked" onclick="chooseTestType();">参数测试<br>
					<input type="radio" name="testType" value ="sqledit" onclick="chooseTestType();">编写SQL<br>
				</td>				
				
				<td colspan="6">
		          	<table style="width:100%">
		          		<tr name="paramtest">
		          			<td style="width:100px">参数测试：</td>
		          			<td>
		          				<table style="width:100%">
		          					<c:forEach  items="${reqParmList}" var="vo" varStatus="status">
		          						<c:if test="${status.index % 2 == 0}">
		          							<tr>
		          							<td>${vo.name}：</td><td><input type="text" id="${vo.name}"></td>
		          						</c:if>
		          						
		          						<c:if test="${status.index % 2 != 0}">
		          							<td>${vo.name}：</td><td><input type="text" id="${vo.name}"></td>
		          							</tr>
		          						</c:if>
		          					</c:forEach>
		          				</table>
		          			</td>
		          		</tr>
		          		
		          		<tr id="paramSqlTr" style="display:none;">
		          			<td>执行的SQL</td>
		          			<td>
		          				<div id="executeSql" style="width:800px;overflow:auto;"></div>
		          			</td>
		          		</tr>
		          		
		          		<tr id="paramresultTr" style="display:none">
		          			<td>执行结果：</td>
		          			<td><div id="paramResult" style="width:800px;overflow:auto;"></div></td>
		          		</tr>
		          		
		          		<tr name="paramtest">
		          			<td colspan="2" align="right">
		          				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-mini-edit'" onclick="dosubmit();">执行</a>
		          				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="reSet();">重置</a>
		          			</td>
		          		</tr>
		          		
			          	<tr name="editSql" style="display:none;">
		          			<td style="width:100px;">SQL语句：</td>
		          			<td style="padding-top:5px;padding-bottom:5px;">
		          				<textarea type="text" id="sql" style="width:100%;font-size:15px;height:50px;"></textarea>
		          			</td>
		          		</tr>
		          		
		          		<tr id="sqlResultTr" style="display:none">
		          			<td>执行结果：</td>
		          			<td><div id="sqlResult" style="width:950px;overflow:auto;"></div></td>
		          		</tr>
		          		
		          		<tr name="editSql" style="display:none;">
		          			<td colspan="2" align="right">
		          				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="openDialog();">选择</a>
		          				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-mini-edit'" onclick="sqlTestSubmit();">执行</a>
		          				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveEditSql();">保存</a>
		          				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="sqlTestReSet();">重置</a>
		          			</td>
		          		</tr>
		          	</table>
				</td>
			</tr>
			
			<tr>
				<td colspan="6">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="fn" style="width:100">URL测试</td>
				<td colspan="6">
					<table style="width:100%">
						<tr>
							<td style="width:100px;">URL</td>
							<td style="padding-top:5px;padding-bottom:5px;"><textarea style="width:100%;font-size:15px;height:50px;" id="url" onchange="urlChange();"></textarea></td>
						</tr>
						
						<tr id="urlResultTr" style="display:none">
		          			<td>返回结果：</td>
		          			<td>
		          				<div id="urlResult" style="width:950px;overflow:auto;height:200px;margin-bottom:0px;"></div>
		          			</td>
		          		</tr>
						
						<tr>
		          			<td colspan="2" align="right">
		          				<a id="selectUrlBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="openUrlDialog();">选择</a>
		          				<a id="exeUrlBtn"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-mini-edit'" onclick="testUrl();">执行</a>
		          				<a id="saveUrlBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveUrl();">保存</a>
		          				<a id="cancelUrlBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="resetUrl();">重置</a>
		          			</td>
		          		</tr>
		          	</table>
				</td>
			</tr>	
		</table>
		
		<div id="listDialog"></div>
		<br><br>
 	</body>
 </html>