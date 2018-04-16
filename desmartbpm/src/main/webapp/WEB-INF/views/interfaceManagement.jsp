<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>

<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>接口管理</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
</head>

<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="layui-row">
			<div class="layui-col-md10">
				<div class="search_area">
					<div class="layui-row layui-form">
						<div class="layui-col-md2">
							<input type="text" placeholder="接口名称" class="layui-input">
						</div>
						<div class="layui-col-md2" style="text-align: right;">
							<button class="layui-btn layui-btn-sm">查询</button>
							<button id="addInterfaces"
								class="layui-btn layui-btn-sm create_btn">新增</button>
							<button class="layui-btn layui-btn-sm delete_btn">删除</button>
						</div>
					</div>
				</div>
				<div style="width: 100%; overflow-x: auto;">
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
								<th><input type="checkbox" name="" title='全选'
									lay-skin="primary"> 序号</th>
								<th>接口名称</th>
								<th>接口描述</th>
								<th>接口类型</th>
								<th>创建时间</th>
								<th>创建人</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listAll}" var="interfaces">
								<tr>
									<td><input id="interfaceIdCheckbox" type="checkbox" title='全选'
										lay-skin="primary" > ${interfaces.interfaceId}</td>
									<td>${interfaces.interfaceName}</td>
									<td>${interfaces.interfaceDescribe}</td>
									<td>${interfaces.interfaceType}</td>
									<td>${interfaces.interfaceCreateDate}</td>
									<td>${interfaces.interfaceCreateUser}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div id="lay_page"></div>
			</div>
		</div>
	</div>
	<div class="display_container">
		<div class="display_content" style="height: 350px">
			<div class="top">新增接口</div>
			<div class="max">
				<form class="layui-form" action="" style="margin-top: 30px;">
					<div class="layui-form-item">
						<label class="layui-form-label">接口名称</label>
						<div class="layui-input-block">
							<input id="interfaceName" type="text" name="title" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口描述</label>
						<div class="layui-input-block">
							<input id="interfaceDescribe" type="text" name="title" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口类型</label>
						<div class="layui-input-block">
							<input id="interfaceType" type="text" name="title" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">创建人</label>
						<div class="layui-input-block">
							<input id="interfaceCreateUser" type="text" name="title" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
				</form>
			</div>
			<div class="foot">
				<button id="sure_btn" class="layui-btn layui-btn sure_btn">确定</button>
				<button id="cancel_btn"
					class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
</body>

</html>

<script type="text/javascript" src="<%=basePath%>/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/layui.js"></script>
<script>
	layui.use(['laypage', 'layer'], function() {
		var laypage = layui.laypage,
			layer = layui.layer;
		//完整功能
		laypage.render({
			elem: 'lay_page',
			count: 50,
			limit: 10,
			layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
			jump: function(obj) {
				console.log(obj)
			}
		});
		laypage.render({
			elem: 'lay_page',
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
	
	$("#addInterfaces").click(function(){
		$(".display_container").css("display","block")
		
		layui.use('laydate',function(){
			var laydate = layui.laydate
			laydate.render({
				elem : '#interfaceCreateDate'
			});
		})
	})
	
	$("#cancel_btn").click(function(){
		$(".display_container").css("display","none")
	})
	
	$("#sure_btn").click(function(){
		layui.use(['layer','form'],function(){
			var form = layui.form , layer = layui.layer , $ = layui.jquery;
			
			$.ajax({
				url : '<%=basePath%>/interfaces/add',
				type : 'POST',
				dataType : 'text',
				data : {
					interfaceName : $("#interfaceName").val(),
					interfaceDescribe : $("#interfaceDescribe").val(),
					interfaceType : $("#interfaceType").val(),
					interfaceCreateUser : $("#interfaceCreateUser").val()
				},
				success : function(data){
					// 添加成功后 ajxa跳转 查询controller
					layer.msg('添加成功'); 
					window.location.href="<%=basePath%>/interfaces/index";
				}
			})
		})
	})
	
	// table 监听
	layui.use(['table','layer'], function (){
		var table = layui.table , layer = layui.layer;
		// 监听表格复选框
		  table.on('checkbox(demo)', function(obj){
			    console.log(obj)
			  });
	})
	
	  	// 删除 行 监听表格	
 	$(".delete_btn").click(function() {
 		var chk = document.getElementById('interfaceIdCheckbox');
 		if(chk.checked==true){    
 			// ajax请求
 		<%-- 	$.ajax({
				url : '<%=basePath%>/interfaces/del',
				type : 'POST',
				dataType : 'text',
				data : {
					interfaceId : $("#interfaceIdCheckbox").val(),
				},
				success : function(data){
					// 添加成功后 ajxa跳转 查询controller
					layer.msg('删除成功'); 
					window.location.href="<%=basePath%>/interfaces/index";
				}
			}) --%>
 		}else{    
 			alert("未选中数据");
 		} 
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
						getMetaInfo();
					}
				}
			});
		});
	}
</script>