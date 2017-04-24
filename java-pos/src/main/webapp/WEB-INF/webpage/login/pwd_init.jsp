<%@ page language="java" pageEncoding="UTF-8"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html>
<head>
<title>admin密码初始化</title>
</head>
<body>
	init pwd....
	<script type="text/javascript">
		var basePath="${basePath}";
		window.setTimeout(function() {
			window.location.replace(basePath+'/loginController.do?pwdInit');
		}, 1000);
	</script>
</body>
</html>
