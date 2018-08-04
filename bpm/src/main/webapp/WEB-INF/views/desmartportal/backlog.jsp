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
    <link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
    <link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
    <link href="resources/desmartportal/css/my.css" rel="stylesheet" />
</head>

<body>
    <div class="layui-fluid">
        <div class="backlog_div">
            <div class="layui-row">
                <div class="layui-col-sm4" style="border-right: 1px solid #e8e8e8;">
                    <div class="backlog_title">我的待办</div>
                    <div class="backlog_detail"><span id="daiban_icon">0</span>个任务</div>
                </div>
                <div class="layui-col-sm4" style="border-right: 1px solid #e8e8e8;">
                    <div class="backlog_title">本周任务平均处理时间</div>
                    <div class="backlog_detail">0分钟</div>
                </div>
                <div class="layui-col-sm4">
                    <div class="backlog_title">本周完成任务数</div>
                    <div class="backlog_detail">0个任务</div>
                </div>
            </div>
        </div>
        <div class="body_content">
            <div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-sm3">
						<label class="layui-form-label">流程创建人</label>
						<div class="layui-input-block">
							<input type="text" placeholder="流程创建人姓名" class="layui-input" id="task-createProcessUserName-search">
						</div>
					</div>
					<div class="layui-col-sm3">
						<label class="layui-form-label">上一环节处理人</label>
						<div class="layui-input-block">
							<input type="text" placeholder="上一环节处理人姓名" class="layui-input" id="task-taskPreviousUsrUsername-search">
						</div>
					</div>
					<div class="layui-col-sm3">
						<label class="layui-form-label">流程标题</label>
						<div class="layui-input-block">
							<input type="text" placeholder="流程实例标题" class="layui-input" id="task-insTitle-search">
						</div>
					</div>
					<div class="layui-col-sm3" style="text-align: center;"> 
						<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="search()">查询</button>
						<button class="layui-btn layui-btn-primary layui-btn-sm" onclick="resetSearch()">重置</button>
					</div>
				</div>
				<div class="layui-row layui-form">
					<div class="layui-col-sm3">
						<label class="layui-form-label">流程名称</label>
						<div class="layui-input-block">
							<input type="text" placeholder="流程名称" class="layui-input" id="task-proName-search">
						</div>
					</div>
					<div class="layui-col-sm3">
						<label class="layui-form-label">开始时间</label>
						<div class="layui-input-block">
							<input type="text" placeholder="开始时间" class="layui-input" id="init-startTime-search">
						</div>
					</div>
					<div class="layui-col-sm3">
						<label class="layui-form-label">结束时间</label>
						<div class="layui-input-block">
							<input type="text" placeholder="结束时间" class="layui-input" id="init-endTime-search">
						</div>
					</div>
					<div class="layui-col-sm3">
						<label class="layui-form-label">代理人</label>
						<div class="layui-input-block">
							<select id="isAgent" class="layui-input-block group_select"
								name="group" lay-verify="required">
								<option value="0" selected>全部</option>
								<option value="false">无</option>
								<option value="1">本人</option>
								<option value="2">非本人</option>
							</select>
						</div>
					</div>
				</div>
			</div>
            <div style="margin-top: 5px;">
                <table class="layui-table" lay-even lay-skin="line">
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
                        <col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th>序号</th>
                            <th>流程名称</th>
                            <th>流程标题</th>
                            <th>环节名称</th>
                            <th>任务所属</th>
                            <th>代理人</th>
                            <th>任务状态</th>
                            <th>上一环节处理人</th>
                            <th>流程创建人</th>
                            <th>任务接收时间</th>
                        </tr>
                    </thead>
                    <tbody id="proMet_table_tbody" />
                </table>
            </div>
            <div id="lay_page"></div>
        </div>
    </div>
    <script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
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
    // 为翻页提供支持
    var pageConfig = {
        pageNum: 1,
        pageSize: 10,
        createProcessUserName: "",
        taskPreviousUsrUsername: "",
        insTitle: "",
        proName : "",
        isAgent:"",
        startTime: null,
        endTime: null,
        total: 0
    }

    layui.use(['laypage', 'layer'], function () {
        var laypage = layui.laypage,
            layer = layui.layer;
        //完整功能
        laypage.render({
            elem: 'lay_page',
            count: 50,
            limit: 10,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function (obj) {
                console.log(obj)
            }
        });
    });
    var form = null;
    $(document).ready(function () {
        // 加载数据
        getTaskInstanceInfo();
        //获得用户有多少待办
        getUserTask();
    })

    // 刷新
    function refresh() {
        getTaskInstanceInfo();
    }

    // 分页
    function doPage() {
        layui.use(['laypage', 'layer'], function () {
            var laypage = layui.laypage,
                layer = layui.layer;
            //完整功能
            laypage.render({
                elem: 'lay_page',
                curr: pageConfig.pageNum,
                count: pageConfig.total,
                limit: pageConfig.pageSize,
                layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                jump: function (obj, first) {
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

    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test1'
        });
    });
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test2'
        });
    });
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#init-startTime-search'
        });
    });
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#init-endTime-search'
        });
    });

    function getTaskInstanceInfo() {
        $.ajax({
            url: 'taskInstance/loadBackLog',
            type: 'post',
            dataType: 'json',
            beforeSend:function(){
            	layer.load(1);
            },
            data: {
                pageNum: pageConfig.pageNum,
                pageSize: pageConfig.pageSize,
                createProcessUserName: pageConfig.createProcessUserName,
                taskPreviousUsrUsername: pageConfig.taskPreviousUsrUsername,
               	proName: pageConfig.proName,
                insTitle: pageConfig.insTitle,
                isAgent:pageConfig.isAgent,
                startTime: pageConfig.startTime,
                endTime: pageConfig.endTime
            },
            success: function (result) {
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
        $("#proMet_table_tbody").html('');
        if (pageInfo.total == 0) {
            return;
        }

        var list = pageInfo.list;
        var startSort = pageInfo.startRow; //开始序号
        var trs = "";
        var type = "";
        var status = "";
        for (var i = 0; i < list.length; i++) {
            var meta = list[i];
            var sortNum = startSort + i;
            if (meta.taskStatus == 12) {
                status = "待处理";
            }
            if (meta.taskStatus == -2) {
                status = "等待加签结束";
            }
            var agentOdate = new Date(meta.taskInitDate);
            var InitDate = datetimeFormat_1(agentOdate);
            //agentOdate.getFullYear()+"-"+(agentOdate.getMonth()+1)+"-"+agentOdate.getDate()+"   "+agentOdate.getHours()+":"+agentOdate.getMinutes()+":"+agentOdate.getSeconds();
            /* var agentOdate2 = new Date(meta.dhProcessInstance.insCreateDate);
            var taskDueDate = agentOdate2.getFullYear()+"-"+(agentOdate2.getMonth()+1)+"-"+agentOdate2.getDate()+"   "+agentOdate.getHours()+":"+agentOdate.getMinutes()+":"+agentOdate.getSeconds(); */
            trs += '<tr ondblclick=openApproval("' + meta.taskUid + '")>' +
                '<td>' +
                sortNum +
                '</td>'+
                '<td>' +
                meta.dhProcessInstance.proName +
                '</td>'+
                '<td style= "cursor:pointer;" onclick="openApproval(\'' + meta.taskUid + '\');">' +
                meta.dhProcessInstance.insTitle +
                '</td>' +
                '<td>' +
                meta.taskTitle +
                '</td>' +
                '<td>' +
                meta.taskHandler +
                '</td>' +
                '<td>';
            if(meta.taskAgentUserName!=null && meta.taskAgentUserName!=""){
            	trs += meta.taskAgentUserName;
            }
            trs += '</td>' +
                '<td>' +
                status +
                '</td>' +
                '<td>';
            if (meta.taskPreviousUsrUsername != null && meta.taskPreviousUsrUsername != "") {
                trs += meta.taskPreviousUsrUsername;
            }
            trs += '</td>' +
                '<td>';
            if (meta.sysUser.userName != null && meta.sysUser.userName != "") {
                trs += meta.sysUser.userName;
            }
            trs += '</td>' +
                '<td>' +
                InitDate +
                '</td>' +
                '</tr>';
        }
        $("#proMet_table_tbody").append(trs);

    }

    // 打开 代办的 详细页面
    function openApproval(taskUid) {
        window.location.href = 'menus/approval?taskUid=' + taskUid;
    }
    //模糊查询
    function search() {
        pageConfig.createProcessUserName = $("#task-createProcessUserName-search").val();
        pageConfig.taskPreviousUsrUsername = $("#task-taskPreviousUsrUsername-search").val();
        pageConfig.insTitle = $("#task-insTitle-search").val();
        pageConfig.proName = $("#task-proName-search").val();  
        pageConfig.isAgent = $("#isAgent").val()==0 ? "" : $("#isAgent").val();
        pageConfig.startTime = $("#init-startTime-search").val() == "" ? null : $("#init-startTime-search").val();
        pageConfig.endTime = $("#init-endTime-search").val() == "" ? null : $("#init-endTime-search").val();
		
        getTaskInstanceInfo();
    }
    //重置模糊查询的条件
    function resetSearch(){
    	 $("#task-createProcessUserName-search").val("");
         $("#task-taskPreviousUsrUsername-search").val("");
         $("#task-insTitle-search").val("");
         $("#init-startTime-search").val("");
         $("#init-endTime-search").val("");
         $("#isAgent").val(0);
    }
    
 	// 获取用户有多少代办
	function getUserTask() {
		$.ajax({
			url: 'user/todoTask',
			type: 'POST',
			dataType: 'text',
			success: function (result) {
				// 渲染到待办
				if (result == 0) {
					$("#daiban_icon").css("display", "none");
					$("#daiban_icon").text(result);
				} else if (result > 0) {
					$("#daiban_icon").text(result);
				}
			}
		})
	}
</script>