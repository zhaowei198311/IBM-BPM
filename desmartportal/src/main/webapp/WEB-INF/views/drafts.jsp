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
<link href="resources/css/layui.css" rel="stylesheet" media="all" />
<link href="resources/css/my.css" rel="stylesheet" media="all" />
<link href="resources/css/modules/laydate/default/laydate.css"
	rel="stylesheet" media="all"/>
<link href="resources/css/modules/layer/default/layer.css"
	rel="stylesheet" media="all"/>
<link href="resources/css/modules/code.css"
	rel="stylesheet" media="all"/>
<title>回收站页面</title>
</head>
<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md2">
					<input id="dfsTitle" type="text" placeholder="草稿名称" class="layui-input">
				</div>
				<div class="layui-col-md2" style="text-align: right; width: 80px">
					<button class="layui-btn select_btn" onclick="search()">查询</button>
				</div>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table" lay-even lay-skin="nob"
				lay-filter="demo">
				<colgroup>
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
						<th>草稿名称</th>
						<th>草稿状态</th>
						<th>草稿数据</th>
						<th>创建人</th>
						<th>创建日期</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="drafts_table_tbody" />
			</table>
		</div>
		<div id="lay_page"></div>
	</div>
</body>
</html>
<script type="text/javascript" src="resources/js/jquery-3.3.1.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/js/layui.all.js" charset="utf-8"></script>
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
		  getDraftsInfo();
	});

	 function getDraftsInfo() {
		$.ajax({
			url : 'drafts/queryDraftsByList',
			type : 'POST',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
			},
			success : function(result) {
				drawTable(result.data)
			}
		})
	}
	
	function drawTable(pageInfo, data){
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
		doPage();
		// 渲染数据
		$("#drafts_table_tbody").html('');
		if (pageInfo.total == 0) {
			return;
		}

		var list = pageInfo.list;
		var startSort = pageInfo.startRow;//开始序号
		var trs = "";
		for (var i = 0; i < list.length; i++) {
			var meta = list[i];
			var sortNum = startSort + i;
			trs += '<tr><td>'
					+ sortNum
					+ '</td>'
					+ '<td>'
					+ meta.dfsTitle
					+ '</td>'
					+ '<td>'
					+ meta.dfsStatus
					+ '</td>'
					+ '<td>'
					+ meta.dfsData
					+ '</td>'
					+ '<td>'
					+ meta.dfsCreatedate
					+ '</td>'
					+ '<td>'
					+ meta.dfsCreator
					+ '</td>'
					+ '<td>'
					+ '<i class="layui-icon"  title="查看详情"  onclick=info("'
					+ meta.dfsId + '")>&#xe60a;</i>'
					+ '<i class="layui-icon"  title="删除草稿"  onclick=del("'
					+ meta.dfsId + '") >&#xe640;</i>'
					+ '</td>'
					+ '</tr>';
		}
		$("#drafts_table_tbody").append(trs);
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
						getDraftsInfo();
					}
				}
			});
		});
	}
	
	function search(){
		var title = $("#dfsTitle").val();
		$.ajax({
			url : 'drafts/queryDraftsByTitle',
			type : 'POST',
			dataType : 'json',
			data : {
				dfsTitle : title
			},
			success : function(result){
				drawTable(result.data)
			}
		})
	}
	
	function del(id){
		layer.confirm('是否删除该草稿？', {
			btn : [ '确定', '取消' ], //按钮
			shade : false
		//不显示遮罩
		}, function(index) {
			// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
			$.ajax({
				url : 'drafts/deleteDraftsById',
				type : 'POST',
				dataType : 'text',
				data : {
					dfsId : id
				},
				success : function(result) {
					// 删除成功后 ajxa跳转 查询controller
					window.location.href = "drafts/index";
				}
			})
			layer.close(index);
		});
	}
</script>
