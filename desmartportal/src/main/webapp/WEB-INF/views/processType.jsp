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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>待办任务</title>
<link href="resources/css/layui.css" rel="stylesheet" />
<link rel="stylesheet"
	href="resources/css/modules/laydate/default/laydate.css" />
<link href="resources/css/my.css" rel="stylesheet" />

</head>
<body>

	<div class="container">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-xs3">
					<div class="layui-form-pane">
						<div class="layui-form-item">
							<label class="layui-form-label" style="cursor: pointer;">刷新</label>
							<div class="layui-input-block">
								<select class="layui-input-block group_select" name="group"
									lay-verify="required">
									<option value="">分组</option>
									<option value="01">不分组</option>
									<option value="02">按类型分组</option>
									<option value="03">按任务创建分组</option>
									<option value="04">按任务创建人所在部门分组</option>
									<option value="05">按优先级分组</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div style="display: none;">
					<input id="proUid" value="${proUid}" style="display: none;">
					<input id="proAppId" value="${proAppId}" style="display: none;">
					<input id="verUid" value="${verUid}" style="display: none;">
					<input id="proName" value="${proName}" style="display: none;">
					<input id="categoryName" value="${categoryName}"
						style="display: none;">
				</div>
				<div class="layui-col-xs2">
					<input type="text" placeholder="标题" class="layui-input">
				</div>
				<div class="layui-col-xs2" style="text-align: right;">
					<button class="layui-btn">查询</button>
					<button class="layui-btn" onclick="startProcess()">发起新流程</button>
				</div>
			</div>
		</div>
		<div>
			<p class="table_list">
				<i class="layui-icon">&#xe61d;</i>共3条任务
			</p>
			<table class="layui-table">
				<colgroup>
					<col width="60">
					<col width="100">
					<col width="130">
					<col width="120">
					<col>
					<col width="150">
					<col width="150">
					<col width="150">
					<col width="150">
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>流程名称</th>
						<th>流程状态</th>
						<th>流程实例编号</th>
						<th>标题</th>
						<th>剩余审批时长</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${processList}" var="process">
						<tr>
							<td class="">1</td>
							<td class="">${process.insTitle}</td>
							<td class="">${process.insStatus}</td>
							<td class="">${process.insId}</td>
							<td class=""><i id="backlog_td" class="layui-icon backlog_img">&#xe63c;</i>
								江西南昌店</td>
							<td class="">6小时</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>

<script type="text/javascript" src="resources/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/js/layui.all.js"></script>
<script>
	function startProcess() {
		var proUid = $('#proUid').val();
		var proAppId = $('#proAppId').val();
		var verUid = $('#verUid').val();
		var proName = $('#proName').val();
		var categoryName = $('#categoryName').val();
		window.location.href = 'menus/startProcess?proUid=' + proUid
				+ '&proAppId=' + proAppId + '&verUid=' + verUid + '&proName='
				+ proName + '&categoryName=' + categoryName;
	}
	
	$(function(){
		$("#backlog_td").click(function(){
			window.location.href="menus/backlogDetail";
		})
		
	})
</script>