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
  		<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
  		<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
		  
	</head>
	<body>
		
		<div class="container">
			<div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-xs3">
					    <div class="layui-form-pane">
					    	<div class="layui-form-item">
					          	<label class="layui-form-label" style="cursor:pointer;" onclick="reload()">刷新</label>
						        <div class="layui-input-block">
						            <select class="layui-input-block group_select" name="group" lay-verify="required" id="task-type-search">
									  	<option value="">任务类型</option>
									  	<option value="normal">一般任务</option>
									  	<option value="sign">会签任务</option>
									  	<option value="transfer">加签任务</option>
									</select>
						        </div>
					       </div>					    	     
					    </div>
					</div>
					<div class="layui-col-xs2">
						<input type="text" placeholder="任务标题"  class="layui-input" id="task-title-search">
					</div>
					<div class="layui-col-xs2">
						<input type="text"  placeholder="接收时间"  class="layui-input" id="init-date-search">
					</div>
					<div class="layui-col-xs1" style="text-align:right;">
					        <button class="layui-btn" onclick="search()">查询</button>
					</div>
				</div>
			</div>
			<div>
				<p class="table_list"><i class="layui-icon">&#xe61d;</i>共<span id="daiban_icon"></span>条任务</p>
				<table class="layui-table" lay-even lay-skin="nob">
					<colgroup>
					    <col width="70">
					    <col width="120">
					    <col width="120">
					    <col width="150">
					    <col width="150">
					    <col width="150">
					    <col width="150">
					    <col width="150">
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>流程标题</th>
					      <th>环节名称</th>
					      <th>任务类型</th>
					      <th>上一环节处理人</th>
					      <th>流程创建人</th>
					      <th>流程创建时间</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id = "proMet_table_tbody"/>
				</table>
			</div>
			<div id="lay_page"></div>
		</div>
	    <!--IE8只能支持jQuery1.9-->
	    <!--[if lte IE 8]>
	    <script src="http://cdn.bootcss.com/jquery/1.9.0/jquery.min.js"></script>
	    <![endif]-->
		<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
		<!--[if lt IE 9]>
		  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
		  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	</body>
	
</html>
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
	<script>
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
		taskType : "",
		taskTitle : "",
		taskInitDate : null,
		total : 0
	}
	
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			count : 50,
			limit : 10,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj) {
				console.log(obj)
			}
		});
	});
	
	$(document).ready(function() {
		// 加载数据
		getTaskInstanceInfo();
		// 定时 去查询我的 代办任务
		getUserTask();
		window.setInterval(getUserTask, 60000); 
	})
	
	// 分页
	function doPage() {
		layui.use([ 'laypage', 'layer' ], function() {
			var laypage = layui.laypage, layer = layui.layer;
			//完整功能
			laypage.render({
				elem : 'lay_page',
				curr : pageConfig.pageNum,
				count : pageConfig.total,
				limit : pageConfig.pageSize,
				layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
				jump : function(obj, first) {
					// obj包含了当前分页的所有参数  
					pageConfig.pageNum = obj.curr;
					pageConfig.pageSize = obj.limit;
					if (!first) {
						getTaskInstanceInfo();
					}
				}
			});
		});
	}
	
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test1'
			});
		});
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test2'
			});
		});
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#init-date-search'
			});
		});
		$(function(){
			$(".backlog_td").click(function(){
				window.location.href="menus/backlogDetail";
			})
			
		})
		
		function getTaskInstanceInfo(){
			$.ajax({
				url : 'taskInstance/queryTaskByReceived',
				type : 'post',
				dataType : 'json',
				data : {
					pageNum : pageConfig.pageNum,
					pageSize : pageConfig.pageSize,
					taskType : pageConfig.taskType,
					taskTitle : pageConfig.taskTitle,
					initTime : pageConfig.taskInitDate
				},
				success : function(result){
					if (result.status == 0) {
						drawTable(result.data);
					}
				}
			})
		}
		
		function drawTable(pageInfo, data) {
			pageConfig.pageNum = pageInfo.pageNum;
			pageConfig.pageSize = pageInfo.pageSize;
			pageConfig.total = pageInfo.total;
			doPage();
			// 渲染数据
			$("#proMet_table_tbody").html('');
			if (pageInfo.total == 0) {
				return;
			}

			var list = pageInfo.list;
			var startSort = pageInfo.startRow;//开始序号
			var trs = "";
			var type = "";
			for (var i = 0; i < list.length; i++) {
				var meta = list[i];
				var sortNum = startSort + i;
				if(meta.taskType=='normal'){
					type = "一般任务";
				}
				var agentOdate = new Date(meta.taskInitDate);
				var InitDate = agentOdate.getFullYear()+"-"+(agentOdate.getMonth()+1)+"-"+agentOdate.getDate()+"   "+agentOdate.getHours()+":"+agentOdate.getMinutes()+":"+agentOdate.getSeconds();
				var agentOdate2 = new Date(meta.dhProcessInstance.insCreateDate);
				var taskDueDate = agentOdate2.getFullYear()+"-"+(agentOdate2.getMonth()+1)+"-"+agentOdate2.getDate()+"   "+agentOdate.getHours()+":"+agentOdate.getMinutes()+":"+agentOdate.getSeconds();;
				trs += '<tr>'
						+'<td>' 
						+ sortNum 
						+ '</td>' 
						+ '<td>'
						+ meta.dhProcessInstance.insTitle
						+ '</td>' 
						+ '<td><i class="layui-icon backlog_img">&#xe63c;</i>'
						+ meta.taskTitle
						+ '</td>'
						+ '<td>'
						+ type
						+ '</td>' 
						+ '<td>' 
						+ meta.taskPreviousUsrUsername
						+ '</td>' 
						+ '<td>' 
						+ meta.sysUser.userName 
						+ '</td>'
						+ '<td>' 
						+ taskDueDate
						+'</td>' 
						+ '<td>'
						+ '<i class="layui-icon" title="查看详情" onclick=openApproval("'+meta.taskUid+'")>&#xe60a;</i>'
						+'</td>' 
						+ '</tr>'
			}
			$("#proMet_table_tbody").append(trs);

		}
		
		// 获取用户有多少代办
		function getUserTask(){
			$.ajax({
				url : 'user/todoTask',
				type : 'POST',
				dataType : 'text',
				data : {},
				success : function(result){
					// 渲染到待办
					$("#daiban_icon").text(result);
				}
			})
		}
		
		// 打开 代办的 详细页面
		function openApproval(taskUid){
			window.location.href = 'menus/approval?taskUid='+taskUid;
		}
		//模糊查询
		function search(){
			pageConfig.taskType = $("#task-type-search").val();
			pageConfig.taskTitle = $("#task-title-search").val();
			pageConfig.taskInitDate = $("#init-date-search").val()==""?null:$("#init-date-search").val();
			getTaskInstanceInfo();
		}
		//刷新按钮
		function reload(){
			window.location.reload();	
		}
	</script>