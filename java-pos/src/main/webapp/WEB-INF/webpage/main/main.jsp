
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
 <head>
  <title>1号外卖-接口管理平台</title>
  <t:base type="jquery,easyui,tools,DatePicker" basePath="${basePath }"></t:base>
  <link rel="shortcut icon" type="image/x-icon" href="${basePath}/plug-in/taskout/images/logo.ico"/>
  <style type="text/css">
a {
	color: Black;
	text-decoration: none;
}

a:hover {
	color: black;
	text-decoration: none;
}
</style>
  <SCRIPT type="text/javascript">
	$(function() {
		$('#layout_east_calendar').calendar({
			fit : true,
			current : new Date(),
			border : false,
			onSelect : function(date) {
				$(this).calendar('moveTo', new Date());
			}
		});

	});
</SCRIPT>
 </head>
 <body class="easyui-layout" style="overflow-y: hidden" scroll="no">
  <!-- 顶部-->
  <div region="north" collapsed="true" title="可视化接口管理平台"  border="false" style="BACKGROUND:#E6E6FA;height: 85px; padding: 1px; overflow: hidden;">
   <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
     <td align="left" style="vertical-align:text-bottom" ><br>
      	  <span  style="margin-left:20px;padding-bottom:5px;font-size:45px;color:#0099cc;font-family: STXingkai">可视化接口管理平台</span>
     </td>
     <td align="right" nowrap>
      <table>
       <tr>
        <td valign="top" height="50">
         <span style="color: #CC33FF">当前用户:</span><span style="color: #666633">(${userName }) <span style="color: #CC33FF">职务</span>:<span style="color: #666633">${roleName }</span>
        </td>
       </tr>
       <td>
        <div style="position: absolute; right: 0px; bottom: 0px;">
         <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">控制面板</a><a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-back">注销</a>
        </div>
        <div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
         <div onclick="openwindow('用户信息','${basePath}/userController.do?userinfo')">
          个人信息
         </div>
         <div class="menu-sep"></div>
         <div onclick="add('修改密码','${basePath}/userController.do?changepassword')">
          修改密码
         </div>
        </div>
        <div id="layout_north_zxMenu" style="width: 100px; display: none;">
         <div class="menu-sep"></div>
         <div onclick="exit('${basePath}/loginController.do?logout','确定退出该系统吗 ?',1);">
          退出系统
         </div>
        </div>
       </td>
       </tr>
      </table>
     </td>
     <td align="right">
      &nbsp;&nbsp;&nbsp;
     </td>
    </tr>
   </table>
  </div>
  <!-- 左侧-->
  <div region="west" split="true" href="${basePath}/loginController.do?left" title="导航菜单" style="width: 150px; padding: 1px;">
  
  
  </div>
  <!-- 中间-->
  <div id="mainPanle" region="center" style="overflow: hidden;">
   <div id="maintabs" class="easyui-tabs" fit="true" border="false">
    <div class="easyui-tab" title="首页" href="${basePath}/loginController.do?home" style="padding:2px; overflow: hidden;">
   				 
    </div>
    <c:if test="${map=='1'}">
     <div class="easyui-tab" title="地图" style="padding: 1px; overflow: hidden;">
      <iframe name="myMap" id="myMap" scrolling="no" frameborder="0" src="${basePath}/mapController.do?map" style="width: 100%; height: 99.5%;"></iframe>
     </div>
    </c:if>
   </div>
  </div>
  <!-- 右侧 
  <div collapsed="true"  region="east" iconCls="icon-reload" title="辅助工具" split="true" style="width: 190px;">
   <div id="tabs"  class="easyui-tabs" border="false" style="height: 240px">
    <div title="日历" style="padding: 0px; overflow: hidden; color: red;">
     <div id="layout_east_calendar"></div>
    </div>
   </div>
   <div id="tabs" class="easyui-tabs" border="false">
    <div title="在线人员" style="padding: 20px; overflow: hidden; color: red;">
    </div>
   </div>
  </div>-->
  <!-- 底部 
  <div region="south" border="false" style="height: 25px; overflow: hidden;">
    <div align="center" style="color: #CC99FF; padding-top: 2px">
    &copy; 版权所有
    <span class="tip"><a href="#" title="广东雄志信息科技有限公司">雄志信息</a> 技术支持:<a href="#" title="广东雄志信息科技有限公司">雄志信息-移动互联网开发组</a></span>

   </div>-->
  </div>
  <div id="mm" class="easyui-menu" style="width:150px;">
        <div id="mm-tabclose">关闭</div>
        <div id="mm-tabcloseall">全部关闭</div>
        <div id="mm-tabcloseother">除此之外全部关闭</div>
        <div class="menu-sep"></div>
        <div id="mm-tabcloseright">当前页右侧全部关闭</div>
        <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
        
</div>

 </body>
</html>