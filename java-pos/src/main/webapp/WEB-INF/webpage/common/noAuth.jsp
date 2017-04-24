<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 
<html>
 <head>
 <t:base type="jquery,tools" basePath="${basePath }"></t:base>
 
 </head> 
 <body>
 <SCRIPT type="text/javascript">
 // begin---author:邢双阳   date：2013-5-25 for：菜单权限拦截器
   var _sun_selectedTab=  $('#maintabs').tabs('getSelected') ; 
   var _sun_selectedTab_title=_sun_selectedTab.panel('options').title;
   $.dialog.alert("提醒：用户权限不足，请联系管理员!");
   $('#maintabs').tabs('close', _sun_selectedTab_title); 
// end---author:邢双阳   date：2013-5-25 for：菜单权限拦截器
 </SCRIPT>
 </body>
</html>
 
