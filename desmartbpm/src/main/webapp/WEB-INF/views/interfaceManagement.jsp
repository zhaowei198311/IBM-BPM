<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>接口管理</title>
  		<%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
	</head>

	<body>
		<div class="layui-container" style="margin-top:20px;width:100%;">
			<div class="layui-row">
				<div class="layui-col-md10">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-md2">
								<input type="text" placeholder="接口名称" class="layui-input">
							</div>
							<div class="layui-col-md2" style="text-align:right;">
								<button class="layui-btn layui-btn-sm">查询</button>
								<button class="layui-btn layui-btn-sm create_btn">新增</button>
								<button class="layui-btn layui-btn-sm delete_btn">删除</button>
							</div>
						</div>
					</div>
					<div style="width:100%;overflow-x:auto;">
						<table class="layui-table backlog_table" lay-even lay-skin="nob">
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
									<th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
									<th>接口名称</th>
									<th>类型</th>
									<th>脚本</th>
									<th>创建时间</th>
									<th>创建人</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
									<td>接口名称</td>
									<td>类型1</td>
									<td></td>
									<td>2018-04-10 10：00：00</td>
									<td>zhangsan</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
									<td>接口名称</td>
									<td>类型1</td>
									<td></td>
									<td>2018-04-10 10：00：00</td>
									<td>zhangsan</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
									<td>接口名称</td>
									<td>类型1</td>
									<td></td>
									<td>2018-04-10 10：00：00</td>
									<td>zhangsan</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
									<td>接口名称</td>
									<td>类型1</td>
									<td></td>
									<td>2018-04-10 10：00：00</td>
									<td>zhangsan</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
									<td>接口名称</td>
									<td>类型1</td>
									<td></td>
									<td>2018-04-10 10：00：00</td>
									<td>zhangsan</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="demo7"></div>
				</div>
			</div>
		</div>
		<div class="display_container">
			<div class="display_content">
				<div class="top">
					新增接口
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
						<div class="layui-form-item">
							<label class="layui-form-label">接口名称</label>
							<div class="layui-input-block">
								<input type="text" name="title" required lay-verify="required" value="" autocomplete="off" class="layui-input">
							</div>	
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">类型</label>
							<div class="layui-input-block">
								<input type="text" name="title" required lay-verify="required" value="" autocomplete="off" class="layui-input">
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">脚本</label>
							<div class="layui-input-block">
								<input type="text" name="title" required lay-verify="required" value="" autocomplete="off" class="layui-input">
							</div>
						</div>
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
	</body>

</html>

<script type="text/javascript" src="<%=basePath%>/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/layui.all.js"></script>
<script>a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a'a
	//分页
	layui.use(['laypage', 'layer'], function() {
		var laypage = layui.laypage,
			layer = layui.layer;
		//完整功能
		laypage.render({
			elem: 'demo7',
			count: 50,
			limit: 10,
			layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
			jump: function(obj) {
				console.log(obj)
			}
		});
		laypage.render({
			elem: 'demo8',
			count: 50,
			limit: 5,
			layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
			jump: function(obj) {
				console.log(obj)
			}
		});
	});

	$(document).ready(function() {
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);

		$(".create_btn").click(function() {
			$(".display_container").css("display", "block");
		})
		$(".copy_btn").click(function() {
			$(".display_container3").css("display", "block");
		})
		$(".sure_btn").click(function() {
			$(".display_container").css("display", "none");
			$(".display_container3").css("display", "none");
		})
		$(".cancel_btn").click(function() {
			$(".display_container").css("display", "none");
			$(".display_container3").css("display", "none");
		})
	});
</script>