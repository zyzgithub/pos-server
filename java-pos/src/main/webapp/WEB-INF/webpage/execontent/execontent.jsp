<%@ page language="java" import="java.util.*"
contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<title>接口信息</title>
		<t:base type="jquery,easyui,tools" basePath="${basePath }"></t:base>
		<script type="text/javascript">
			var basePath="${basePath}";
			$(function() {
				$('#wrapper').css('width','95%');
				$('#steps').css('width','100%');
				
				$('#cc').combotree({
					url : basePath+'/web/execontentController.do?setPExecontent',
				});
				if($('#level').val()=='2'){
					$('#pfun').show();
				}else{
					$('#pfun').hide();
				}
				
				
				$('#level').change(function(){
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
				
				$('#id').change(function(){
					var id = $('#id').val();
					if(id != null && id.length > 0){
						$.ajax({
							url: basePath+'/web/execontentController.do?checkId&date='+new Date(),
							data:{
								'id':id 
							},
							success:function(msg){
								var data = JSON.parse(msg);
								if(data.success == true){
									document.getElementById("codeMsg").innerHTML = "<font style='color:red'>code已存在，请重新输入</font>";
									$('#id').focus();
								}else{
									document.getElementById("codeMsg").innerHTML = "<font style='color:green'>通过信息验证！</font>";
								}
							}
						});
					}
				});
				
				 $("#formobj").Validform({
					 btnSubmit: "#submit",
					 btnReset: "#reset",
					 ajaxPost:true,
					 callback:function(data){
						 tip(data.msg);
						 
						 //刷新页面
						 var tab = window.top.$('#maintabs').tabs('getTab', '接口管理');  
						 tab.panel('refresh');
					 }
				 });
				
			});
		</script>
	 </head>
	 
	<body>
		<t:formvalid formid="formobj"  layout="div" dialog="false" action="${basePath }/web/execontentController.do?save">			
				<div class="form">
					<label class="Validform_label">
						名称:
					</label>
					<input id="name" name="name" class="inputxt" value="${execontentPage.name}" datatype="*" style="width:850px;" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>
				</div>
				
				<div class="form">
					<label class="Validform_label">
						id:
					</label>
					<input id="id" name="id" class="inputxt" value="${execontentPage.id}" style="width:850px;" datatype="*" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>
					<span id="codeMsg" class="Validform_checktip"><font style="color:red">(必须唯一）</font></span>
				</div>
			
			    <div class="form">
					<label class="Validform_label">
						菜单等级:
					</label>
					<select name="level" id="level" datatype="*" style="width:850px;" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>
						<option value="1"
							<c:if test="${execontentPage.level eq 1}">selected="selected"</c:if>>
							一级菜单
						</option>
						<option value="2"
							<c:if test="${execontentPage.level eq 2}">selected="selected"</c:if>>
							二级菜单
						</option>
					</select>
					<span class="Validform_checktip"></span>
				</div>
			    
			    <div class="form" id="pfun" >
					<label class="Validform_label">
						父菜单:
					</label>
					<input id="cc" name="exeContent.code" value="${execontentPage.exeContent.code}" style="width:850px;">					
				</div>
			    
			   <div class="form" id="funurl">
					<label class="Validform_label">
						sql语句:
					</label>
					<textarea id="sqlStatement" name="sqlStatement"  rows="5" style="width:850px;" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>${execontentPage.sqlStatement}</textarea>
					<span class="Validform_checktip"></span>
				</div>
			    
				<div class="form">
					<label class="Validform_label">
						类型: 
					</label>
					<select id="type" name="type" datatype="*" style="width:850px;" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>
						<option value="execute"
							<c:if test="${execontentPage.type eq 'execute'}">selected="selected"</c:if>>
							execute（添加、删除、修改）
						</option>
						<option value="query"
							<c:if test="${execontentPage.type eq 'query'}">selected="selected"</c:if>>
							query（查询）
						</option>
					</select>
				</div>
				
				<div class="form">
						<label class="Validform_label">
							正确提示信息:
						</label>
						<input class="inputxt" id="message" name="message" ignore="ignore" value="${execontentPage.message}" style="width:850px;" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>
				</div>
				
				<div class="form">
						<label class="Validform_label">
							错误提示信息:
						</label>
						<input class="inputxt" id="errorMessage" name="errorMessage" ignore="ignore" value="${execontentPage.errorMessage}" style="width:850px;" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>
				</div>
				
				<div class="form">
						<label class="Validform_label">
							描述:
						</label>
						<textarea id="descrition" name="descrition" datatype="*" style="width:850px;" rows="5" <c:if test="${type eq  'serach'}">disabled="disabled"</c:if>>${execontentPage.descrition}</textarea>
				</div>
				
				<c:if test="${type != 'serach'}">
					<div class="form" >
						<div style="margin-left:-180px;">
							<button id="submit" type="submit">提交</button>
						</div>	
						<div style="margin-top:-40px;margin-left:50px;">
							<button id="reset" type="reset">取消</button>
						</div>
					</div>
				</c:if>
		</t:formvalid>
 </body>
 </html>