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
<title>门店生命周期-任务实例</title>
<link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
<link rel="stylesheet"
	href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
<style type="text/css">

.display_content_ins_business_key{
            color: #717171;
            padding: 20px;
            width: 50%;
            height: 60%;
            background: #fff;
            position: fixed;
            left: 18%;
            top: 16%;
            box-shadow: 0 0 10px #ccc;
        }

        .foot_ins_business_key{
            text-align: right;
            height: 50px;
            line-height: 50px;
            padding-right: 15px;
        }
        
        .upload_overflow_middle {
            height: 80%;
            width: 96%;
            border: 1px solid #ccc;
            position: relative;
            padding: 0 10px;
            overflow-y: auto;
            overflow-x: hidden;
        }
		
</style>
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
								<select id="processType" class="layui-input-block group_select" name="group"
									lay-verify="required">
									<option value=""></option>
									<option value="">所有</option>
									<option value="1">待处理</option>
									<option value="6">已结束</option>
									<option value="4">暂停中</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div style="display: none;">
				<input id="insUid" value="${insUid}" style="display: none;">
<%-- 					<input id="proUid" value="${proUid}" style="display: none;"> --%>
<%-- 					<input id="proAppId" value="${proAppId}" style="display: none;"> --%>
<%-- 					<input id="proVerUid" value="${proVerUid}" style="display: none;"> --%>
				</div>
				<div class="layui-col-xs2">
					<input id="processName" type="text" placeholder="流程标题" class="layui-input">
				</div>
				<div class="layui-col-xs2">
								<select id="insInitUser" class="layui-input-block group_select" name="group"
									lay-verify="required">
									<option value="">请选择发起人</option>
									<option value="allUser">全体人员</option>
									<option value="current">本人</option>
								</select>
				</div>
				<div class="layui-col-xs3" style="text-align: right;">
					<button class="layui-btn" onclick="queryProcess()">查询</button>
					<button class="layui-btn" onclick="checkedBusinesskey()">发起新流程</button>
					<button class="layui-btn" onclick="back()">返回</button>
				</div>
			</div>
		</div>
		<div>
			<p class="table_list">
				<i class="layui-icon">&#xe61d;</i>共<span id="number"></span>条任务
			</p>
			<table class="layui-table" lay-even lay-skin="nob">
				<colgroup>
					 	<col width="5%">
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
					    <th>任务类型</th>
					    <th>任务状态</th>
					    <th>上一环节提交人</th>
					    <th>流程发起人</th>
					    <th>接收时间</th>
					    <th>处理时间</th>
					</tr>
				</thead>
				<tbody id="processType_table_tbody"/>
			</table>
		</div>
		<div id="lay_page"></div>
	</div>
	<!-- 选择业务关键字 -->
	 <div id="checkedBusinessKey" style="display: none;" class="display_content_ins_business_key">
        <div class="top">选择业务关键字</div>
        <div class="upload_overflow_middle">
            <div class="layui-upload">
                <div class="layui-upload-list">
                    <table class="layui-table">
                        <colgroup>
                            <col width="10%">
                            <col>
                        </colgroup>
                        <thead>
                            <tr>
                            <th>序号</th>
                            <th style="text-align: center;">业务关键字</th>
                            </tr>
                        </thead>
                        <tbody class="showBusinessList">
                        	
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="foot_ins_business_key">
        	<button class="layui-btn layui-btn sure_btn" onclick="startProcess()">确定</button>
            <button class="layui-btn layui-btn cancel_btn" onclick="$('#checkedBusinessKey').hide()">取消</button>
        </div>
    </div>
</body>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js" ></script>
<script>

		// 为翻页提供支持
		var pageConfig = {
			pageNum : 1,
			pageSize : 10,
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
		//getProcessInfo();
		queryProcess();
	});
	
	/* function getProcessInfo() {
		$.ajax({
			url : 'processInstance/queryProcessByActive',
			type : 'POST',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize
			},
			success : function(result) {
				drawTable(result.data)
			}
		})
	} */
	var index;
	function queryProcess(){
		var insUid = $('#insUid').val();
// 		var processName = $('#processName').val();
// 		var processType = $('#processType').val();
// 		var proUid = $('#proUid').val();
// 		var proAppId = $('#proAppId').val();
// 		var insInitUser = $("#insInitUser").val();
		// 按条件查询 流程
		$.ajax({
			url : 'taskInstance/loadPageTaskByStartProcess',
			type : 'post',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
// 				insTitle : processName,
// 				insInitUser : insInitUser,
// 				insStatusId : processType,
// 				proUid : proUid,
// 				proAppId : proAppId
				insUid: insUid
			},
			beforeSend: function () {
		        index = layer.load(1);
		    },
			success : function(result){
				if(result.status==0){
				drawTable(result.data);
				}else{
					layer.alert(result.msg);
				}
				layer.close(index);
			},error : function(){
				layer.close(index);
			}
		})
	}

	function drawTable(pageInfo, data) {
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
		$('#number').text(pageConfig.total);
		doPage();
		// 渲染数据
		$("#processType_table_tbody").html('');
		if (pageInfo.total == 0) {
			return;
		}
		var list = pageInfo.list;
		var startSort = pageInfo.startRow;//开始序号
		var trs = "";
		for (var i = 0; i < list.length; i++) {
			var meta = list[i];
			var sortNum = startSort + i;
			var agentOdate = new Date(meta.taskInitDate);
			var InitDate = datetimeFormat_1(agentOdate);
			var agentOdate1 = new Date(meta.taskFinishDate);
			var finishDate = datetimeFormat_1(agentOdate1);
			trs += '<tr ondblclick=dbGoToTask(this)'
					+'><td id="'+meta.taskUid+'">'
					+ sortNum
					+ '</td>'
					+ '<td style = "cursor:pointer;" '
					+'data-taskstatus = "'+meta.taskStatus+'" onclick ="goToTask(this)" >'
					+ meta.dhProcessInstance.insTitle
					+ '</td>'
					+ '<td>'
					+ meta.dhProcessInstance.insStatus
					+ '</td>'
					+ '<td>'
					+ meta.taskTitle
					+ '</td><td>';
					if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
						trs += meta.taskPreviousUsrUsername;
					}
					trs += '</td><td>';
					if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
						trs += meta.sysUser.userName;
					}
					trs += '</td>'
						+ '<td>'
						+ InitDate
						+'</td>' 
						+'<td>'
						+finishDate
						+'</td>'
					+ '</tr>';
		}
		$("#processType_table_tbody").append(trs);
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
						//getProcessInfo();
						queryProcess();
					}
				}
			});
		});
	}
	
	function startProcess(insBusinessKey) {
		if(insBusinessKey==null||insBusinessKey==''||insBusinessKey == undefined){	
		insBusinessKey = $(".showBusinessList").find("input[name='insBusinessKey']:checked").val();
		if(insBusinessKey==null||insBusinessKey==''||insBusinessKey == undefined){
			layer.alert("请选择业务关键字");
			return;
		}
		}
		var proUid = $('#proUid').val();
		var proAppId = $('#proAppId').val();
		var verUid = $('#verUid').val();
		var proName = $('#proName').val();
		var categoryName = $('#categoryName').val();
		window.location.href = 'menus/startProcess?proUid=' + proUid
				+ '&proAppId=' + proAppId + '&verUid=' + verUid + '&proName='
				+ proName + '&categoryName=' + categoryName
				+'&insBusinessKey='+insBusinessKey;
	}
	
	function checkedBusinesskey(){
		var proUid = $('#proUid').val();
		var proAppId = $('#proAppId').val();
		var verUid = $('#verUid').val();
		$.ajax({
			url:"processInstance/checkedBusinesskey",
			type : 'POST',
			dataType : 'json',
			beforeSend: function() {
			    layer.load(1);
			},
			data : {
				proUid : proUid,
				proAppId : proAppId,
				proVerUid : verUid
			},
			success : function(result) {
				if(result.status == 0){
					if(result.data.flag==1){
						startProcess(result.data.stepBusinessKey);
					}else{
                        layer.closeAll('loading');
						$("#checkedBusinessKey").find(".showBusinessList").empty();
						for (var i = 0; i < result.data.length; i++) {
						var info = '<tr><td><input type="radio" name="insBusinessKey" '
						+'value="'
						+ result.data[i]
						+'" >'+(i+1)+'</td>'
	                    +'<td style="text-align: center;">'
	                   	+ result.data[i]
	                    +'</td></tr>';
	                    $("#checkedBusinessKey").find(".showBusinessList").append(info);
						}
						$("#checkedBusinessKey").show();
					}
				}else{
                    layer.closeAll('loading');
					layer.alert(result.msg);
				}
			},
			error : function(){
                layer.closeAll('loading');
				layer.alert("发起流程异常");
			}
		})
		
	}
	function goToTask(a){
		var taskUid = $(a).prev().attr("id");
		var taskStatus = $(a).data("taskstatus");
		if(taskStatus == 12 ){
			window.location.href = 'menus/approval?taskUid='+taskUid;
		}else if(taskStatus == -2 ){
			window.location.href = 'menus/approval?taskUid='+taskUid;
		}else if(taskStatus == 32){
			window.location.href = 'menus/finshed_detail?taskUid='+taskUid;
		}
	}
	function dbGoToTask(a){
		var taskUid = $(a).find("td").eq(0).attr("id");
		var taskStatus = $(a).find("td").eq(1).data("taskstatus");
		if(taskStatus == 12 ){
			window.location.href = 'menus/approval?taskUid='+taskUid;
		}else if(taskStatus == -2 ){
			window.location.href = 'menus/approval?taskUid='+taskUid;
		}else if(taskStatus == 32){
			window.location.href = 'menus/finshed_detail?taskUid='+taskUid;
		}
	}
	// 返回
	function back() {
		window.location.href = 'javascript:history.go(-1)';
	}
</script>
</html>