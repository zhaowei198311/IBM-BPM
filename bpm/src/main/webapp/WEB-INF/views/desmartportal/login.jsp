<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<title>登陆</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="resources/desmartportal/css/layui.css"
	media="all">
<link rel="stylesheet" href="resources/desmartportal/css/admin.css"
	media="all">
<link rel="stylesheet" href="resources/desmartportal/css/login.css"
	media="all">
<style>
body {
	color: #fff;
}

h4 {
	font-weight: 600;
	margin: 0 0 20px
}

.logo {
	width: 60px;
}

.list li .layui-icon {
	font-size: 12px;
	margin-right: 5px;
	font-weight: bold;
}

.list li {
	margin-bottom: 3px;
}

.layadmin-user-login-box {
	height: 180px;
	width: 250px;
	background: rgba(255, 255, 255, 0.2);
	border: 1px solid rgba(255, 255, 255, .3);
	-moz-box-shadow: 0 3px 0 rgba(12, 12, 12, 0.03);
	-webkit-box-shadow: 0 3px 0 rgba(12, 12, 12, 0.03);
	box-shadow: 0 3px 0 rgba(12, 12, 12, 0.03);
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
	padding: 30px;
}

.layadmin-user-login-box h4 {
	color: #fff;
	margin-bottom: 10px;
	font-weight: 600;
}

.layadmin-user-login-body .layui-form-item .layui-input {
	padding-left: 10px;
}
</style>
</head>
<body
	style="background: url(resources/desmartportal/images/login-background.jpg) no-repeat; background-size: 100% 100%;">

	<div class="layadmin-user-login layadmin-user-display-show"
		id="LAY-user-login" style="display: none;">
		<div class="layui-row" style="width: 750px; margin: 10% auto 0;">
			<div class="layui-col-md7">
				<div>
					<img src="resources/desmartportal/images/logo.png" class="logo" />
				</div>
				<h4 style="margin: 10px 0 20px;">
					欢迎使用 <strong>来伊份BPM后台管理系统</strong>
				</h4>
				<ul class="list">
					<li><i class="layui-icon">&#xe702;</i> 平台简介一</li>
					<li><i class="layui-icon">&#xe702;</i> 平台简介二</li>
					<li><i class="layui-icon">&#xe702;</i> 平台简介三</li>
					<li><i class="layui-icon">&#xe702;</i> 平台简介四</li>
					<li><i class="layui-icon">&#xe702;</i> 平台简介五</li>
				</ul>
			</div>
			<div class="layui-col-md5">
				<div class="layadmin-user-login-main">
					<div
						class="layadmin-user-login-box layadmin-user-login-body layui-form">
						<h4>登录</h4>
						<div class="layui-form-item">
							<!--<i class="layadmin-user-login-icon layui-icon" for="LAY-user-login-username">&#xe647;</i>-->
							<input
								style="background: #fff url(images/user.png) no-repeat 95% center; color: #333;"
								type="text" name="username" id="LAY-user-login-username"
								lay-verify="required" placeholder="用户名" class="layui-input">
						</div>
						<div class="layui-form-item">
							<!--<i class="layadmin-user-login-icon layui-icon " for="LAY-user-login-password">&#xe673;</i>-->
							<input
								style="background: #fff url(images/locked.png) no-repeat 95% center; color: #333;"
								type="password" name="password" id="LAY-user-login-password"
								lay-verify="required" placeholder="密码" class="layui-input">
						</div>
						<form class="layui-form">
							<button class="layui-btn layui-btn-fluid" lay-submit
								lay-filter="LAY-user-login-submit">登 陆</button>
						</form>
					</div>
				</div>
			</div>

		</div>
		<div class="layui-trans layadmin-user-login-footer"
			style="width: 750px; margin: 0 auto; margin-top: 20px;">
			<p>Copyright © 来伊份 2018</p>
		</div>
	</div>
</body>
</html>
<script type="text/javascript"
	src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript"
	src="resources/desmartportal/js/layui.all.js"></script>

<script type="text/javascript">
	layui.use(['form'],function() {
		var $ = layui.jquery;
		var form   = layui.form;
		var layer  = layui.layer;
				
		form.render();
		//提交
		form.on('submit(LAY-user-login-submit)', function(obj) {
			$.ajax({
				url : 'user/logins',
				type : 'post',
				dataType : 'text',
				data : {
					
				},
				success : function(data) {
					if (data == 1) {
						layer.msg('登陆成功');
						location.href = "user/menus";
					} else {
						layer.msg('登陆失败');
					}
				}
			})
			return false;
		});

	});
</script>