<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!-- context path -->
<t:base type="jquery,easyui" basePath="${basePath }"></t:base>
<script type="text/javascript" src="${basePath }/plug-in/Highcharts-2.2.5/js/highcharts.src.js"></script>
<script type="text/javascript" src="${basePath }/plug-in/Highcharts-2.2.5/js/modules/exporting.src.js"></script>
 <t:tabs id="tt" iframe="false">
  <t:tab href="${basePath }/logController.do?userBroswer&reportType=line" icon="icon-search" title="用户分析line" id="pnode"></t:tab>
  <t:tab href="${basePath }/logController.do?userBroswer&reportType=pie" icon="icon-search" title="用户分析pie" id="pnode"></t:tab>
  <t:tab href="${basePath }/logController.do?userBroswer&reportType=column" icon="icon-search" title="用户分析column" id="pnode"></t:tab>
 </t:tabs>
