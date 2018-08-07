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
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<link href="resources/desmartportal/css/layui.css" rel="stylesheet"/>
  		<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
  		<style>
  			.layui-input-block{margin-left:150px;}
  			.layui-form-label{width:120px;}
  		</style>
  		
	</head>
	<body>
		<div class="search_area top_btn">
			<span style="padding-left:10px;color:#777;font-size:18px;">（人工任务2）新开店流程</span>
			<span style="float:right;padding-right:20px;">
				<button class="layui-btn layui-btn-primary layui-btn-sm">放弃</button> <a href="new_user_detail.html"><button class="layui-btn layui-btn-sm">下一步</button></a><a href="draft.html" style="margin-left:10px;"><button class="layui-btn layui-btn-primary layui-btn-sm">返回</button></a> 
			</span>
		</div>
		<div class="container">			
			<div class="content" style="margin-top:80px;">
				<form class="layui-form" action="">
					<div class="layui-form-item">
					    <label class="layui-form-label">标题（便于检索）</label>
					    <div class="layui-input-block">
					      <input type="text" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
					    </div>
					</div>
					<div class="layui-form-item">
					    <label class="layui-form-label">身份</label>
					    <div class="layui-input-block">
					      来伊份//部门1/<br />如果兼职身份不在列表中，请检查兼职角色是否有当前流程的启动权限。
					    </div>
					</div>				  
				</form>
			</div>
		</div>
	</body>
	
</html>
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
	