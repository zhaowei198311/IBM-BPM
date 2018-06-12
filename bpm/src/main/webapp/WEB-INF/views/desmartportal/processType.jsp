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
									<option value="">请选择流程类型</option>
									<option value="">所有</option>
									<option value="1">运行中</option>
									<option value="2">已结束</option>
									<option value="6">暂停中</option>
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
					<input id="processName" type="text" placeholder="流程名称" class="layui-input">
				</div>
				<div class="layui-col-xs3" style="text-align: right;">
					<button class="layui-btn" onclick="queryProcess()">查询</button>
					<button class="layui-btn" onclick="checkedBusinesskey()">发起新流程</button>
				</div>
			</div>
		</div>
		<div>
			<p class="table_list">
				<i class="layui-icon">&#xe61d;</i>共3条任务
			</p>
			<table class="layui-table" lay-even lay-skin="nob">
				<colgroup>
					<col width="150">
					<col width="150">
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
</html>

<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
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
		getProcessInfo();
	});
	
	function getProcessInfo() {
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
	}
	
	function queryProcess(){
		var processName = $('#processName').val();
		var processType = $('#processType').val();
		// 按条件查询 流程
		$.ajax({
			url : 'processInstance/queryProcessByUserAndType',
			type : 'post',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
				insTitle : processName,
				insStatusId : processType
			},
			success : function(result){
				drawTable(result.data)
			}
		})
	}

	function drawTable(pageInfo, data) {
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
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
			var sortNum = startSort+1 + i;
			trs += '<tr><td id="aa">'
					+ sortNum
					+ '</td>'
					+ '<td>'
					+ meta.insTitle
					+ '</td>'
					+ '<td>'
					+ meta.insStatus
					+ '</td>'
					+ '<td>'
					+ meta.insId
					+ '</td>'
					+ '<td class=""><i id="backlog_td" class="layui-icon backlog_img">&#xe63c;</i>'
					+ meta.insId
					+ '</td>'
					+ '<td>'
					+ meta.insId
					+ '</td>'
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
						getProcessInfo();
					}
				}
			});
		});
	}
	
	
	function startProcess() {
		var insBusinessKey = $(".showBusinessList").find("input[name='insBusinessKey']:checked").val();
		if(insBusinessKey==null||insBusinessKey==''||insBusinessKey == undefined){
			layer.alert("请选择业务关键字");
			return;
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
			data : {
				proUid : proUid,
				proAppId : proAppId,
				proVerUid : verUid
			},
			success : function(result) {
				if(result.status == 0){
					if(result.data==1){
						
					}else{
						$("#checkedBusinessKey").find(".showBusinessList").empty();
						for (var i = 0; i < result.data.length; i++) {
						var info = '<tr><td><input type="radio" name="insBusinessKey" '
						+'value="'
						+ result.data[i].stepBusinessKey
						+'" >'+(i+1)+'</td>'
	                    +'<td style="text-align: center;">'
	                   	+ result.data[i].stepBusinessKey
	                    +'</td></tr>';
	                    $("#checkedBusinessKey").find(".showBusinessList").append(info);
						}
						$("#checkedBusinessKey").show();
					}
				}else{
					layer.alert(result.msg);
				}
			},
			error : function(){
				layer.alert("发起流程异常");
			}
		})
		
	}
</script>