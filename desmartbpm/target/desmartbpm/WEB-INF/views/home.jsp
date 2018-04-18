<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>主页面</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
</head>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/layui.css" media="all">
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">来 伊 份</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<li class="layui-nav-item"><a href="javascript:;">首页</a></li>
				<li class="layui-nav-item"><a href="javascript:;">权限管理</a></li>
				<li class="layui-nav-item"><a href="javascript:;">用户管理</a></li>
				<li class="layui-nav-item"><a href="javascript:;">其它</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="javascript:;">邮件管理</a>
						</dd>
						<dd>
							<a href="javascript:;">消息管理</a>
						</dd>
						<dd>
							<a href="javascript:;">授权管理</a>
						</dd>
					</dl></li>
			</ul>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item"><a href="javascript:;"> <img
						src="${pageContext.request.contextPath}/images/ibm.jpg"
						class="layui-nav-img"> zhaowei
				</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="javascript:;">基本资料</a>
						</dd>
						<dd>
							<a href="javascript:;">安全设置</a>
						</dd>
					</dl></li>
				<li class="layui-nav-item"><a href="${pageContext.request.contextPath}/User/logout">退 出</a></li>
			</ul>
		</div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<ul class="layui-nav layui-nav-tree" lay-filter="test">
					<li class="layui-nav-item layui-nav-itemed"><a class=""
						href="javascript:;">代办任务</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="javascript:;">我的代办</a>
							</dd>
							<dd>
								<a href="javascript:;">委托代办</a>
							</dd>
						</dl></li>
					<li class="layui-nav-item"><a href="javascript:;">任务中心</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="javascript:leave();">请假任务</a>
							</dd>
							<dd>
								<a href="javascript:reimbursement();">报销任务</a>
							</dd>
						</dl></li>
					<li class="layui-nav-item"><a href="javascript:drafts();">草稿箱</a></li>
					<li class="layui-nav-item"><a href="javascript:;">委托设置</a></li>
				</ul>
			</div>
		</div>

		<div id="box" class="layui-body">
			   <!-- ifream 内容区域 -->
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			© baidu.com - 底部固定区域
		</div>
	</div>
	<script src="${pageContext.request.contextPath}resources/js/layui.js"></script>
	<script type="text/javascript">
		//JavaScript代码区域
		layui.use('element', function() {
			var element = layui.element;

		});
		
		function leave() {
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.msg('请假任务');
			});
			document.getElementById("box").innerHTML ='<iFrame src="${pageContext.request.contextPath}/leaveTask.jsp" width="100%" height="100%" scrolling="no" frameborder="0"/>'
		}
		
		function drafts(){
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.msg('草稿箱');
			});
			document.getElementById("box").innerHTML ='<iFrame src="${pageContext.request.contextPath}/drafts.jsp" width="100%" height="100%" scrolling="no" frameborder="0"/>'
		}

		function reimbursement() {
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.msg('报销任务');
			});
			document.getElementById("box").innerHTML ='<iFrame src="${pageContext.request.contextPath}/reimburstTask.jsp" width="100%" height="100%" scrolling="no" frameborder="0"/>'
			/* document.getElementById("box").innerHTML = '<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">'
					+ '<legend>请 假 页 面</legend>'
					+ '</fieldset>'
					+ '<form class="layui-form" action="${pageContext.request.contextPath}/User/leavel" method="post">'
					+ '<div class="layui-form-item">'
					+ '<label class="layui-form-label">请假人:</label>'
					+ '<div class="layui-input-block">'
					+ '<input type="text" name="leavelName" lay-verify="title" autocomplete="off" placeholder="请输入昵称" class="layui-input"/>'
					+ '</div>'
					+ '</div>'
					+ '<div class="layui-form-item">'
					+ '<label class="layui-form-label">请假时间:</label>'
					+ '<div class="layui-input-block">'
					+ '<input type="text" name="leavelDate" id="date1" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input"/>'
					+ '</div>'
					+ '</div>'
					+ '<div class="layui-form-item">'
					+ '<label class="layui-form-label">请假事由:</label>'
					+ '<div class="layui-input-block">'
					+ '<textarea name="leavelReason" class="layui-textarea" placeholder="请输入请假事由"></textarea>'
					+ '</div>'
					+ '</div>'
					+ '<div class="layui-form-item">'
					+ '<div class="layui-input-block">'
					+ '<button class="layui-btn" type="submit">立即提交</button>'
					+ '<button class="layui-btn layui-btn-primary" type="reset">重置</button>'
					+ '</div>' + '</div>' + '</form>' */
		}
		
		function submit(){
			alert('测试提交表单');
		}
		
		//常规用法----(动态写的js页面 通过id引入不到时间插件，静态页面可以)
		layui.use('laydate', function() {
			var laydate = layui.laydate;
			laydate.render({
				elem : '#date1'
			})
		});

		function add() {
			document.getElementById("box").innerHTML = '<h1>This is Test hitm hello</h1>';
		}
	</script>
</body>
</html>