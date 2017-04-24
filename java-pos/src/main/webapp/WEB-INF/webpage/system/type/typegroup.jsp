<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>部门信息</title>
  <t:base type="jquery,easyui,tools" basePath="${basePath }"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" layout="div" dialog="true" action="${basePath }/systemController.do?saveTypeGroup">
   <input name="id" type="hidden" value="${typegroup.id }">
   <fieldset class="step">
     <div class="form">
    <label class="Validform_label">
      分组编码:
     </label>
     <input name="typegroupcode" class="inputxt" ajaxurl="${basePath }/systemController.do?checkTypeGroup&code=${typegroup.typegroupcode }"  value="${typegroup.typegroupcode }" datatype="s3-8">
     <span class="Validform_checktip">编码范围在3~8位字符,且不为空</span>
    </div>
    <div class="form">
     <label class="Validform_label">
      分组名称:
     </label>
     <input name="typegroupname" class="inputxt"  value="${typegroup.typegroupname }" datatype="s3-10">
     <span class="Validform_checktip">名称范围在3~10位字符,且不为空</span>
    </div>
   </fieldset>
  </t:formvalid>
 </body>
</html>
