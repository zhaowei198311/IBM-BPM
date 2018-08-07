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
  		
	</head>
	<body>
		<div class="search_area top_btn">				
			<span style="padding-left:10px;color:#777;font-size:18px;">（提交）空标题</span>
			<span style="float:right;padding-right:20px;">
				<button class="layui-btn layui-btn-danger layui-btn-sm">作废</button><button class="layui-btn layui-btn-primary layui-btn-sm">保存</button><button class="layui-btn layui-btn-sm">提交</button><button class="layui-btn layui-btn-primary layui-btn-sm">流程图</button><a href="new_user.html" style="margin-left:10px;"><button class="layui-btn layui-btn-sm">退出</button></a>
			</span>
		</div>
		<div class="container">			
			<div class="content">
				<table class="layui-table">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
						<tr>
							<th colspan="4" class="list_title">新员工表单</th>
						</tr>
					    <tr>
					      <td>工号</td>
					      <td><input type="text" name="title" required  lay-verify="required"  autocomplete="off" class="layui-input"></td>
					      <td>姓名</td>
					      <td><input type="text" name="title" required  lay-verify="required"  autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td>年龄</td>
					      <td><input type="text" name="title" required  lay-verify="required"  autocomplete="off" class="layui-input"></td>
					      <td>薪资</td>
					      <td><input type="text" name="title" required  lay-verify="required"  autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td>岗位</td>
					      <td><input type="text" name="title" required  lay-verify="required" autocomplete="off" class="layui-input"></td>
					    </tr>
					</tbody>
				</table>
				<p class="title_p">审批意见</p>
				<div class="layui-form">
					<textarea placeholder="意见留言" class="layui-textarea"></textarea>
				</div>	
				<p class="title_p">审批记录</p>
				<table class="layui-table">
					<colgroup>
					    <col width="60">
					    <col width="150">
					    <col width="250">
					    <col width="150">
					    <col width="150"> 
					    <col>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>环节名称</th>
					      <th>审批人</th>
					      <th>岗位名称</th>
					      <th>审批日期</th>					      
					      <th>留言</th>
					    </tr> 
					</thead>
					<tbody>
					    
					</tbody>
				</table>
				
			</div>
		</div>
	</body>
	
</html>
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>