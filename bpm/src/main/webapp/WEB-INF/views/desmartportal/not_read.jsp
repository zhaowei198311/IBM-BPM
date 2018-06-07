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
  		<title>抄送任务</title>
  		<link href="resources/desmartportal/css/layui.css" rel="stylesheet"/>
  		<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
  		<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
  		
	</head>
	<body>
		<div class="container">
			<!-- 搜索区 -->
			<div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-xs1">
					    <div class="layui-form-pane">
					    	<div class="layui-form-item">
					          	<label class="layui-form-label" style="cursor:pointer;" onclick="refresh()">刷新</label>    
					       </div>					    	     
					    </div>
					</div>
					<div class="layui-col-xs2">
						<input type="text" placeholder="流程创建人姓名"  class="layui-input" id="task-createProcessUserName-search">
					</div>
					<div class="layui-col-xs2">
						<input type="text" placeholder="上一环节处理人姓名"  class="layui-input" id="task-taskPreviousUsrUsername-search">
					</div>
					<div class="layui-col-xs2">
						<input type="text" placeholder="流程实例标题"  class="layui-input" id="task-insTitle-search">
					</div>
					<div class="layui-col-xs2">
						<input type="text"  placeholder="开始时间"  class="layui-input" id="init-startTime-search">
					</div>
					<div class="layui-col-xs2">
						<input type="text"  placeholder="结束时间"  class="layui-input" id="init-endTime-search">
					</div>
					<div class="layui-col-xs1">
					    <div class="layui-form-pane"  style="float: right;">
					    	<div class="layui-form-item">
					    	<div class="layui-col-xs1" >
					        <button class="layui-btn" onclick="search()">查询</button>
							</div>
					        </div>					    	     
					    </div>
					</div>
				</div>
			</div>
			
			<!-- 表格数据区 -->
			<div>
				<div class="layui-tab">
				  	<ul class="layui-tab-title">
					    <li class="unread">未读通知</li>
					    <li class="read">通知查询</li>
				  	</ul>
				  	<div class="layui-tab-content">
					    <div class="layui-tab-item layui-show">
					    	<p class="table_list"><i class="layui-icon">&#xe61d;</i>共<span id="daiban_icon"></span>条任务</p>
							<table class="layui-table backlog_table" lay-even lay-skin="nob">
								<colgroup>
								    <col>
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
								      <th>流程标题</th>
								      <th>环节名称</th>
								      <th>是否代理</th>
								      <th>任务状态</th>
								      <th>上一环节处理人</th>
								      <th>流程创建人</th>
								      <th>任务接收时间</th>
								    </tr> 
								</thead>
								<tbody id="transferBody" />
							</table>
					    </div>
<!-- 					    <div class="layui-tab-item"> -->
<!-- 					    	<p class="table_list"><i class="layui-icon">&#xe61d;</i>共3条任务</p> -->
<!-- 							<table class="layui-table backlog_table"> -->
<%-- 								<colgroup> --%>
<%-- 								    <col width="60"> --%>
<%-- 								    <col width="100"> --%>
<%-- 								    <col> --%>
<%-- 								    <col width="150"> --%>
<%-- 								    <col width="150">  --%>
<%-- 								    <col width="150"> --%>
<%-- 								</colgroup> --%>
<!-- 								<thead> -->
<!-- 								    <tr> -->
<!-- 								      <th>序号</th> -->
<!-- 								      <th>来自</th> -->
<!-- 								      <th>标题</th> -->
<!-- 								      <th>类型</th> -->
<!-- 								      <th>接收时间</th> -->
<!-- 								      <th>期限</th> -->
<!-- 								    </tr>  -->
<!-- 								</thead> -->
<!-- 								<tbody id="transferedBody"> -->
<!-- 								    <tr> -->
<!-- 								      <td>1</td> -->
<!-- 								      <td>管理员</td> -->
<!-- 								      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生</td> -->
<!-- 								      <td>日常报销</td> -->
<!-- 								      <td>2018-03-12</td> -->
<!-- 								      <td></td> -->
<!-- 								    </tr> -->
<!-- 								</tbody> -->
<!-- 							</table> -->
<!-- 					    </div> -->
				  	</div>
				</div>			
			</div>
			
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
//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 10,
	createProcessUserName : "",
	taskPreviousUsrUsername: "",
	insTitle : "",
	startTime : null,
	endTime: null,
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

// 未读/已读标识
var taskStatus = '12';
$(document).ready(function() {
	// 初始化进入未读
	getTaskInstanceInfo(taskStatus);
	// 查询我的 抄送任务
	getUserTask(taskStatus);
	// 未读
	$('.unread').click(function(){
		taskStatus = '12';
		getTaskInstanceInfo(taskStatus);
		getUserTask(taskStatus);
	})
	// 已读
	$('.read').click(function(){
		taskStatus = '32';
		getTaskInstanceInfo(taskStatus);
		getUserTask(taskStatus);
	})
})

// 刷新
function refresh(){
	getUserTask(taskStatus);
	getTaskInstanceInfo(taskStatus);
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
					getTaskInstanceInfo(taskStatus);
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
		    elem: '#init-startTime-search'
		});
	});
	layui.use('laydate', function(){
		var laydate = layui.laydate;
		  	laydate.render({
		    elem: '#init-endTime-search'
		});
	});
	
	function getTaskInstanceInfo(taskStatus){
		$.ajax({
			url : 'taskInstance/queryTransfer',
			type : 'post',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
				createProcessUserName : pageConfig.createProcessUserName,
				taskPreviousUsrUsername: pageConfig.taskPreviousUsrUsername,
				insTitle : pageConfig.insTitle,
				startTime : pageConfig.startTime,
				endTime: pageConfig.endTime,
				taskStatus: taskStatus
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
		$("#transferBody").html('');
		if (pageInfo.total == 0) {
			return;
		}

		var list = pageInfo.list;
		var startSort = pageInfo.startRow;//开始序号
		var trs = "";
		var type = "";
		var status = "";
		var delegateFlag= "";
		for (var i = 0; i < list.length; i++) {
			var meta = list[i];
			var sortNum = startSort + i;
			if(meta.taskStatus==12){
				status = "待处理";
			}
			if(meta.taskDelegateUser != null && meta.taskDelegateUser !=""){
				delegateFlag = "是";
			}else{
				delegateFlag = "否";
			}
			var agentOdate = new Date(meta.taskInitDate);
			var InitDate = agentOdate.getFullYear()+"-"+(agentOdate.getMonth()+1)+"-"+agentOdate.getDate()+"   "+agentOdate.getHours()+":"+agentOdate.getMinutes()+":"+agentOdate.getSeconds();

			trs += '<tr ondblclick=openApproval("'+meta.taskUid+'")>'
					+'<td>' 
					+ sortNum 
					+ '</td>' 
					+ '<td style= "cursor:pointer;" onclick="openApproval(\'' + meta.taskUid + '\');">'
					+ meta.dhProcessInstance.insTitle
					+ '</td>' 
					+ '<td>'
					+ meta.taskTitle
					+ '</td>'
					+ '<td>'
					+ delegateFlag
					+ '</td>' 
					+ '<td>'
					+ status
					+ '</td>' 
					+ '<td>';
					if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
						trs += meta.taskPreviousUsrUsername;
					}
					trs += '</td>'					
					+ '<td>';
					if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
						trs += meta.sysUser.userName;
					}
					trs += '</td>'
					+ '<td>' 
					+ InitDate
					+'</td>' 
					+ '</tr>';
		}
		$("#transferBody").append(trs);

	}
	
	// 获取用户有多少未读/已读任务
	function getUserTask(taskStatus){
		$.ajax({
			url : 'taskInstance/queryTransferNumber',
			type : 'POST',
			dataType : 'text',
			data : {
				taskStatus: taskStatus
			},
			success : function(result){
				// 渲染到抄送
				if(result == 0){
					$("#daiban_icon").css("display", "none");
				}else{
					$("#daiban_icon").html(result);
				}
			}
		})
	}
	
	// 打开 抄送详情页面
	function openApproval(taskUid){
		updateTaskStatus(taskUid,taskStatus);
		window.location.href = 'menus/finshed_detail?taskUid='+taskUid;
	}
	//模糊查询
	function search(){
		pageConfig.createProcessUserName = $("#task-createProcessUserName-search").val();
		pageConfig.taskPreviousUsrUsername = $("#task-taskPreviousUsrUsername-search").val();
		pageConfig.insTitle = $("#task-insTitle-search").val();
		pageConfig.startTime = $("#init-startTime-search").val()==""?null:$("#init-startTime-search").val();
		pageConfig.endTime = $("#init-endTime-search").val()==""?null:$("#init-endTime-search").val();
		
		getTaskInstanceInfo(taskStatus);
	}
	//进入未读任务详情页面，则更改taskStatus
	function updateTaskStatus(taskUid,taskStatus){
		if (taskStatus = 12) {
			$.ajax({
				async: false,
				url: 'taskInstance/updateTaskStatusOfTransfer',
				type: 'post',
				dataType: 'json',
				data:{
					taskUid: taskUid
				},
				success: function(){
					
				}
			})
		}	
	}
</script>