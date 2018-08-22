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
		<div class="layui-fluid">
			<div class="backlog_div">
	            <div class="layui-row">
	                <div class="layui-col-md6" style="border-right: 1px solid #e8e8e8;">
	                    <div class="backlog_title">未阅通知</div>
	                    <div class="backlog_detail"><span id="weiyue_icon">0</span>个任务</div>
	                </div>
	                
	                <div class="layui-col-md6">
	                    <div class="backlog_title">已阅通知</div>
	                    <div class="backlog_detail"><span id="yiyue_icon">0</span>个任务</div>
	                </div>
	            </div>
	        </div>
	        
	        <div class="body_content">
	            <div class="search_area">
					<div class="layui-row layui-form">
						<div class="layui-col-md3">
							<label class="layui-form-label">流程创建人</label>
							<div class="layui-input-block">
								<input type="text" placeholder="流程创建人姓名"  class="layui-input" id="task-createProcessUserName-search">
							</div>
						</div>
						<div class="layui-col-md3">
							<label class="layui-form-label">上一环节处理人</label>
							<div class="layui-input-block">
								<input type="text" placeholder="上一环节处理人姓名" class="layui-input" id="task-taskPreviousUsrUsername-search">
							</div>
						</div>
						<div class="layui-col-md3">
							<label class="layui-form-label">流程主题</label>
							<div class="layui-input-block">
								<input type="text" placeholder="流程主题" class="layui-input" id="task-insTitle-search">
							</div>
						</div>
						<div class="layui-col-md3" style="text-align: center;"> 
							<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="search()">查询</button>
							<button class="layui-btn layui-btn-primary layui-btn-sm" onclick="resetSearch()">重置</button>
						</div>
					</div>
					<div class="layui-row layui-form">
						<div class="layui-col-md3">
							<label class="layui-form-label">流程名称</label>
							<div class="layui-input-block">
								<input type="text" placeholder="流程名称" class="layui-input"
									id="task-proName-search">
							</div>
						</div>
						<div class="layui-col-md3">
							<label class="layui-form-label">开始时间</label>
							<div class="layui-input-block">
								<input type="text" placeholder="开始时间" class="layui-input" id="init-startTime-search">
							</div>
						</div>
						<div class="layui-col-md3">
							<label class="layui-form-label">结束时间</label>
							<div class="layui-input-block">
								<input type="text" placeholder="结束时间" class="layui-input" id="init-endTime-search">
							</div>
						</div>
					</div>
				</div>
				
				<div style="margin-top:5px;">
					<div class="layui-tab">
					  	<ul class="layui-tab-title">
						    <li class="unread layui-this">未读通知</li>
						    <li class="read">已读通知</li>
					  	</ul>
					  	<div class="layui-tab-content">
						    <div class="layui-tab-item layui-show">
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
									    <col>
									</colgroup>
									<thead>
									    <tr>
									      	<th>序号</th>
									      	<th>流程名称</th>
				                            <th>流程主题</th>
				                            <th>环节名称</th>
				                            <th>抄送人员</th>
				                            <th>流程创建人</th>
									      	<th>抄送时间</th>
									    </tr> 
									</thead>
									<tbody id="transferBody" />
								</table>
						    </div>
						    <div id="lay_page"></div>
					  	</div>
					</div>			
				</div>
			</div>
		</div>
		<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
		<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
		<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>	
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
	proName : "",
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
	getUserTask("12","weiyue_icon");
	
	getUserTask("32","yiyue_icon");
	// 未读
	$('.unread').click(function(){
		taskStatus = '12';
		pageConfig.pageNum = 0;
		pageConfig.pageSize = 10;
		pageConfig.createProcessUserName = $("#task-createProcessUserName-search").val();
		pageConfig.taskPreviousUsrUsername = $("#task-taskPreviousUsrUsername-search").val();
		pageConfig.insTitle = $("#task-insTitle-search").val();
		pageConfig.proName = $("#task-proName-search").val();
		pageConfig.startTime = $("#init-startTime-search").val()==""?null:$("#init-startTime-search").val();
		pageConfig.endTime = $("#init-endTime-search").val()==""?null:$("#init-endTime-search").val();
		getTaskInstanceInfo(taskStatus);
	})
	// 已读
	$('.read').click(function(){
		taskStatus = '32';
		pageConfig.pageNum = 1;
		pageConfig.pageSize = 10;
		pageConfig.createProcessUserName = $("#task-createProcessUserName-search").val();
		pageConfig.taskPreviousUsrUsername = $("#task-taskPreviousUsrUsername-search").val();
		pageConfig.insTitle = $("#task-insTitle-search").val();
		pageConfig.proName = $("#task-proName-search").val();
		pageConfig.startTime = $("#init-startTime-search").val()==""?null:$("#init-startTime-search").val();
		pageConfig.endTime = $("#init-endTime-search").val()==""?null:$("#init-endTime-search").val();
		getTaskInstanceInfo(taskStatus);
	})
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
			url :common.getPath() + '/taskInstance/queryTransfer',
			type : 'post',
			dataType : 'json',
			beforeSend:function(){
				layer.load(1);
			},
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
				createProcessUserName : pageConfig.createProcessUserName,
				taskPreviousUsrUsername: pageConfig.taskPreviousUsrUsername,
				insTitle : pageConfig.insTitle,
				proName : pageConfig.proName,
				startTime : pageConfig.startTime,
				endTime: pageConfig.endTime,
				taskStatus: taskStatus
			},
			success : function(result){
				if (result.status == 0) {
					drawTable(result.data);
				}
				layer.closeAll("loading");
			},
			error:function(){
				layer.closeAll("loading");
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

			trs += '<tr style= "cursor:pointer;" onclick="openApproval(\'' + meta.taskUid + '\');">'
					+'<td>' 
					+ sortNum 
					+ '</td>' 
					+ '<td>' 
	                + meta.dhProcessInstance.proName 
					+ '</td>'
					+ '<td>'
					+ meta.dhProcessInstance.insTitle
					+ '</td>' 
					+ '<td>'
					+ meta.taskTitle
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
	function getUserTask(taskStatus,eleId){
		$.ajax({
			url : common.getPath() +'/taskInstance/queryTransferNumber',
			type : 'POST',
			dataType : 'text',
			data : {
				taskStatus: taskStatus
			},
			success : function(result){
				// 渲染到抄送
				$("#"+eleId).text(result);
			}
		})
	}
	//重置模糊查询的条件
    function resetSearch(){
    	 $("#task-createProcessUserName-search").val("");
         $("#task-taskPreviousUsrUsername-search").val("");
         $("#task-insTitle-search").val("");
         $("#init-startTime-search").val("");
         $("#init-endTime-search").val("");
    }
	// 打开 抄送详情页面
	function openApproval(taskUid){
		updateTaskStatus(taskUid,taskStatus);
		window.location.href = common.getPath() +'/menus/finshed_detail?taskUid='+taskUid;
	}
	//模糊查询
	function search(){
		pageConfig.createProcessUserName = $("#task-createProcessUserName-search").val();
		pageConfig.taskPreviousUsrUsername = $("#task-taskPreviousUsrUsername-search").val();
		pageConfig.insTitle = $("#task-insTitle-search").val();
		pageConfig.proName = $("#task-proName-search").val();
		pageConfig.startTime = $("#init-startTime-search").val()==""?null:$("#init-startTime-search").val();
		pageConfig.endTime = $("#init-endTime-search").val()==""?null:$("#init-endTime-search").val();
		
		getTaskInstanceInfo(taskStatus);
		getUserTask(taskStatus);
	}
	//进入未读任务详情页面，则更改taskStatus
	function updateTaskStatus(taskUid,taskStatus){
		if (taskStatus = 12) {
			$.ajax({
				async: false,
				url: common.getPath() +'/taskInstance/updateTaskStatusOfTransfer',
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