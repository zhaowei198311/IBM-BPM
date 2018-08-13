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
<link href="resources/desmartportal/css/layui.css" rel="stylesheet"
	media="all" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet"
	media="all" />
<link
	href="resources/desmartportal/css/modules/laydate/default/laydate.css"
	rel="stylesheet" media="all" />
<link href="resources/desmartportal/css/modules/layer/default/layer.css"
	rel="stylesheet" media="all" />
<link href="resources/desmartportal/css/modules/code.css"
	rel="stylesheet" media="all" />
<title>报表1</title>
</head>
<body>
	<div class="layui-fluid">
        <div class="body_content">
        	<div class="search_area">
        		<div class="layui-row layui-form">
        			<form action="#" method="post"  id="form" >
        				<div class="layui-form-item">
						    <div class="layui-inline">
						      <label class="layui-form-label">时间</label>
						      <div class="layui-input-inline" style="width: 120px;">
						      	<input type="text" placeholder="开始时间" class="layui-input" name="startTime" id="init-startTime-search" />
						      </div>
						      <div class="layui-form-mid">-</div>
						      <div class="layui-input-inline" style="width: 120px;">
						      <input type="text" placeholder="结束时间" class="layui-input" name="endTime" id="init-endTime-search" />
						      </div>
						    </div>
						      <div class="layui-inline">
						      	<label class="layui-form-label">环节名称</label>
								<div class="layui-input-block">
									<input type="text" placeholder="环节名称" class="layui-input"  name="taskTitle" />
								</div>
						      </div>
						     <div class="layui-inline" style="margin-top: -6px;"> 
								<button class="layui-btn layui-btn-normal layui-btn-sm" type="button" onclick="search();">查询</button>
								<button class="layui-btn layui-btn-primary layui-btn-sm" type="reset" >重置</button>
								<button class="layui-btn layui-btn-primary layui-btn-sm" type="button" onclick="exportData();" >导出</button>
							</div>
						 </div>
					</form>
				</div>
        	</div>
        	<div style="margin-top:5px;">
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
					      <th>上一环节提交人</th>
					      <th>环节名称</th>
					      <th>接收时间</th>
					      <th>处理时间</th>
					    </tr> 
					</thead>
					<tbody id="proMet_table_tbody" />
				</table>
			</div>
			<div id="lay_page"></div>
        </div>
       </div>
       <script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	   <script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
       <script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
      <script>
    // 为翻页提供支持
    var pageConfig = {
        pageNum: 1,
        pageSize: 10,
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
            url: 'projectStatement/queryTaskInstance',
            type: 'post',
            dataType: 'json',
            beforeSend:function(){
            	layer.load(1);
            },
            data:pageConfig,
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
            //ondblclick=openApproval("' + meta.taskUid + '")
            trs += '<tr>' +
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
    	var formArray = $('#form').serializeArray();
    	for (var i = 0; i < formArray.length; i++) {
			pageConfig[formArray[i].name]=formArray[i].value;
		}
       getTaskInstanceInfo();
    }
    
    function exportData() {
    	$('#form').attr('action',"projectStatement/queryExportTaskInstance");
    	$('#form').submit();
    	$('#form').attr('action',"#");
    }
    
</script>
</body>
</html>