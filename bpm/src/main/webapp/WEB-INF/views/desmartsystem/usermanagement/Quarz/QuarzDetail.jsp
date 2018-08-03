<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查看</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<jsp:include page="../common/inc.jsp"></jsp:include>
<style type="text/css">
	.choose_user{position:absolute;right:10px;top:8px;cursor:pointer;z-index:1;}
</style>
</head>
<body>
	<form class="layui-form" style="margin-top: 20px;" method="post" action="">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label" >任务名称</label>
				<div class="layui-input-inline">
					<input type="text" name="jobName" autocomplete="off" class="layui-input" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label" >类型</label>
				<div class="layui-input-inline">
					<input type="text" name="triggerGroupName" autocomplete="off" class="layui-input" />
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" >类路径</label>
			<div class="layui-input-block" style="width: 78%;">
				<input type="text" name="clazz"  maxlength="50" autocomplete="off" class="layui-input" />
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" >表达式</label>
			<div class="layui-input-block" style="width: 28%;" >
				<input type="text" name="cronExpr"  maxlength="50" autocomplete="off" class="layui-input" />
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block" style="float: right;margin-right: 20px;">
				<button class="layui-btn layui-btn-primary" id="close">关闭</button>
			</div>
		</div>
	</form>
</body>
</html>