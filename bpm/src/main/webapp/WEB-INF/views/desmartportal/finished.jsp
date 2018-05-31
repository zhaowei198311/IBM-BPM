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
  		<title>已办任务</title>
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
					          	<label class="layui-form-label" style="cursor:pointer;">刷新</label>
						        <div class="layui-input-block">
						             <select class="layui-input-block group_select" name="group" lay-verify="required" id="task-type-search">
									  	<option value="">任务类型</option>
									  	<option value="sign">sign</option>
									  	<option value="normal">normal</option>
									  	<option value="transfer">transfer</option>
									</select>
						        </div>
					       </div>					    	     
					    </div>
					</div>
					<div class="layui-col-xs2">
						<input type="text" placeholder="任务标题"  class="layui-input" id="task-title-search">
					</div>
					<div class="layui-col-xs2">
						<input type="text"  placeholder="开始时间"  class="layui-input" id="test1">
					</div>
					<div class="layui-col-xs2">
					    <input type="text"  placeholder="结束时间"  class="layui-input" id="test2">				    
					</div>
					<div class="layui-col-xs1" style="text-align:right;">
					        <button class="layui-btn" onclick="search()">查询</button>
					</div>
				</div>
			</div>
			<div>
				<p class="table_list"><i class="layui-icon">&#xe61d;</i>共<span id="yiban_icon"></span>条任务</p>
				<table class="layui-table backlog_table" lay-even lay-skin="nob">
					<colgroup>
					    <col>
					    <col>
					    <col>
					    <col>
					    <col> 
					    <col> 
					    <col>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>任务标题</th>
					      <!-- <th>上一环节提交人</th> -->
					      <th>任务类型</th>
					      <th>接收时间</th>
					      <!-- <th>处理时间</th> -->
					      <th>跟踪</th>
					    </tr> 
					</thead>
					<tbody id="proMet_table_tbody" />
				</table>
			</div>
			<div id="lay_page"></div>
		</div>
		<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
		<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
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
	<script>
	var pageConfig = {
			pageNum : 1,
			pageSize : 10,
			taskType : "",
			taskTitle : "",
			taskInitDate : null,
			taskDueDate : null,
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
		
		$(document).ready(function (){
			// 加载数据
			getTaskInstanceInfo();
			// 定数刷新
			getUserTask();
			window.setInterval(getUserTask, 60000);
		})
		
		function getTaskInstanceInfo(){
			$.ajax({
				url : 'taskInstance/queryTaskByClosed',
				type : 'post',
				dataType : 'json',
				data : {
					pageNum : pageConfig.pageNum,
					pageSize : pageConfig.pageSize,
					taskType : pageConfig.taskType,
					taskTitle : pageConfig.taskTitle,
					initTime : pageConfig.taskInitDate,
					dueTime : pageConfig.taskDueDate,
				},
				success : function(result){
					if(result.status == 0){
						drawTable(result.data);
					}
				}
			})
		}
		
		// 获取用户有多少已办
		function getUserTask(){
			$.ajax({
				url : 'user/todoFinshTask',
				type : 'POST',
				dataType : 'text',
				data : {},
				success : function(result){
					// 渲染到已办
					$("#yiban_icon").text(result);
				}
			})
		}
		
		function drawTable(pageInfo,data){
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
			for (var i = 0; i < list.length; i++) {
				var meta = list[i];
				var sortNum = startSort + i;
				var meta = list[i];
				var agentOdate = new Date(meta.taskInitDate);
				var InitDate = agentOdate.getFullYear()+"-"+(agentOdate.getMonth()+1)+"-"+agentOdate.getDate()+"   "+agentOdate.getHours()+":"+agentOdate.getMinutes()+":"+agentOdate.getSeconds();
				trs += '<tr>' + '<td>' + sortNum + '</td>' 
						+ '<td><i class="layui-icon backlog_img" title="查看详情" onclick=openFinishedDetail("'+meta.taskUid+'")>&#xe63c;</i>'
						+ meta.taskTitle 
						+ '</td>' 
						+ '<td>';
					if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
						trs += meta.taskPreviousUsrUsername 
					}
					trs += '</td>' + '<td>' + meta.taskType
						+ '</td>' 
						+ '<td>'
						+ InitDate
						+'</td>' 
						+ '<td>'
						+ "<i class='layui-icon tail' onclick='processView(\""+ meta.insUid +"\")'>&#xe641;</i>"
						+ '</td>'
						+ '</tr>';
			}
			$("#proMet_table_tbody").append(trs);
		}
		
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
		
		//模糊查询
		function search(){
			pageConfig.taskType = $("#task-type-search").val();
			pageConfig.taskTitle = $("#task-title-search").val();
			pageConfig.taskInitDate = $("#test1").val()==""?null:$("#test1").val();
			pageConfig.taskDueDate = $("#test2").val()==""?null:$("#test2").val();
			getTaskInstanceInfo();
		}
		//刷新按钮
		function reload(){
			window.location.reload();	
		}
		//进入已办详情页面
		function openFinishedDetail(taskUid){
			window.location.href = 'menus/finshed_detail?taskUid='+taskUid;
		}
		
		//跟踪流程图
		function processView(insId) {
			$.ajax({
		        url: 'processInstance/viewProcess',
		        type: 'post',
		        dataType: 'text',
		        data: {
		            insId: insId
		        },
		        success: function (result) {
		            layer.open({
		                type: 2,
		                title: '流程图',
		                shadeClose: true,
		                shade: 0.8,
		                area: ['790px', '580px'],
		                content: result
		            });
		        }
		    });
		}
	</script>