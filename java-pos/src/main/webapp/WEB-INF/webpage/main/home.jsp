<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="margin-left:20px">
	<p style="font-size:23px;font-weight:bold">欢迎使用可视化接口管理平台</p>
	<p style="font-size:20px;">使用说明：</p>
	<p style="font-size:18px;">
		1、通用url的编写格式：
		<div style="font-size:18px;margin-bottom:5px;">
		  方式一：http://IP:端口号/VCIS/controller.do?execute&ids=接口ID(可多个,用逗号隔开)& params={'key':'value'} (多个用,逗号隔开)&sessionkey=
		</div>
		<div style="font-size:18px;">
		  方式二：http://IP:端口号/VCIS/controller.do?execute&ids=接口ID(可多个,用逗号隔开)&sessionkey=&key=value&key1=value1(直接拼接参数)
		</div>
	</p>		
	<p style="font-size:18px;">
		2、登录接口是独立的一个方法，其url编写格式<font style="color:red;weight:bold;font-size:18px;">，用户标识的名称必须是 login_name</font>：
		<div style="font-size:18px;margin-bottom:5px;">
			方式一：http://IP:端口号/VCIS/controller.do?login&id=接口ID(只有1个)& params={'login_name':'value','key':'value'} (多个用逗号隔开)
		</div>
		<div style="font-size:18px;">
			方式二：http://IP:端口号/VCIS/controller.do?login&id=接口ID(只有1个)&login_name=xx&key=value (直接拼接参数)
		</div>
	</p>
	<p style="font-size:18px;">
		3、进入真机调试模式
		<div style="margin-top:-10px;font-size:18px;">
			登录的时候，在用户标识的值后面加上 :debug 即可
		</div>
	</p>
	<p style="font-size:18px;">
		4、不需要sessionkey验证的操作(如注册)
		<div style="margin-top:-10px;font-size:18px;">
			在url后面加上参数register=true 即可
		</div>
	</p>
	<p style="font-size:18px;">
		5、SQL中 where 语句的条件参数可能为空的情况
		<div style="margin-top:-10px;font-size:18px;">
			编写SQL的时候，加上$NotNull{ and xxx=:xxx},表示如果参数不为空，就把条件加到where条件中，否则就不加上该条件
		</div>
	</p>
</div>
